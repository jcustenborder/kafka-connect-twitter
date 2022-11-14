/**
 * Copyright © 2016 Jeremy Custenborder (jcustenborder@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jcustenborder.kafka.connect.twitter;

import com.github.jcustenborder.kafka.connect.utils.VersionUtil;
import com.github.jcustenborder.kafka.connect.utils.data.SourceRecordDeque;
import com.github.jcustenborder.kafka.connect.utils.data.SourceRecordDequeBuilder;
import com.google.common.collect.ImmutableMap;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TweetsApi;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.AddOrDeleteRulesRequest;
import com.twitter.clientlib.model.AddOrDeleteRulesResponse;
import com.twitter.clientlib.model.AddRulesRequest;
import com.twitter.clientlib.model.DeleteRulesRequest;
import com.twitter.clientlib.model.DeleteRulesRequestDelete;
import com.twitter.clientlib.model.FilteredStreamingTweetResponse;
import com.twitter.clientlib.model.Get2TweetsSampleStreamResponse;
import com.twitter.clientlib.model.Rule;
import com.twitter.clientlib.model.RuleNoId;
import com.twitter.clientlib.model.Tweet;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TwitterSourceTask extends SourceTask {

  private static final Logger log = LoggerFactory.getLogger(TwitterSourceTask.class);
  private static final int RETRIES = 10;
  private SourceRecordDeque messageQueue;

  private volatile boolean running;

  private TwitterSourceConnectorConfig config;

  @Override
  public String version() {
    return VersionUtil.version(getClass());
  }

  @Override
  public void start(Map<String, String> map) {
    config = new TwitterSourceConnectorConfig(map);
    messageQueue = SourceRecordDequeBuilder.of()
        .emptyWaitMs(config.queueEmptyMs)
        .batchSize(config.queueBatchSize)
        .build();

    TwitterApi apiInstance = new TwitterApi(new TwitterCredentialsBearer(config.bearerToken.value()));
    InputStream twitterStream;
    try {
      twitterStream = initTweetsStreamProcessing(apiInstance);
    } catch (ApiException e) {
      throw new RuntimeException(e);
    }
    running = true;
    Thread readingThread = new TweetsStreamProcessingThread(apiInstance, twitterStream);
    readingThread.start();
  }

  private class TweetsStreamProcessingThread extends Thread {

    private final TwitterApi apiInstance;

    private InputStream twitterStream;

    public TweetsStreamProcessingThread(TwitterApi apiInstance, InputStream initialTwitterStream) {
      this.apiInstance = apiInstance;
      this.twitterStream = initialTwitterStream;
    }

    @Override
    public void run() {
      while (running) {
        try {
          BufferedReader reader = new BufferedReader(new InputStreamReader(twitterStream));
          String line = reader.readLine();
          while (running && line != null) {
            if (config.filterRule != null) {
              processFilteredStreamingTweetResponse(line);
            } else {
              processGet2TweetsSampleStreamResponse(line);
            }
            line = reader.readLine();
          }
          closeTwitterStreamGracefully();
        } catch (Exception ex) {
          log.error("Exception during tweets stream processing. Restarting stream processing...", ex);
          try {
            closeTwitterStreamGracefully();
            Thread.sleep(1000);
            twitterStream = initTweetsStreamProcessing(apiInstance);
          } catch (Exception exx) {
            log.error("Exception during restart of stream processing. Stopping job...");
            throw new RuntimeException(exx);
          }
        }
      }
    }

    private void closeTwitterStreamGracefully() {
      try {
        twitterStream.close();
      } catch (IOException ex) {
        log.error("Exception during tweets stream closing", ex);
      }
    }
  }

  private InputStream initTweetsStreamProcessing(TwitterApi apiInstance) throws ApiException {
    InputStream twitterStream;
    if (config.filterRule != null) {
      log.info("Setting up filter rule = {}", config.filterRule);
      setFilterRule(apiInstance);
      log.info("Starting tweets search stream.");
      TweetsApi.APIsearchStreamRequest builder = apiInstance.tweets().searchStream();
      if (config.tweetFields != null) {
        log.info("Setting up tweet fields = {}", config.tweetFields);
        builder = builder.tweetFields(Arrays.stream(config.tweetFields.split(",")).collect(Collectors.toSet()));
      }
      twitterStream = builder.execute(RETRIES);
    } else {
      log.info("Starting tweets sample stream.");
      TweetsApi.APIsampleStreamRequest builder = apiInstance.tweets().sampleStream();
      if (config.tweetFields != null) {
        log.info("Setting up tweet fields = {}", config.tweetFields);
        builder = builder.tweetFields(Arrays.stream(config.tweetFields.split(",")).collect(Collectors.toSet()));
      }
      twitterStream = builder.execute(RETRIES);
    }
    return twitterStream;
  }

  private void setFilterRule(TwitterApi apiInstance) throws ApiException {
    List<Rule> currentRules = apiInstance.tweets().getRules().execute(RETRIES).getData();
    if (currentRules != null && !currentRules.isEmpty()) {
      List<String> currentNotMatchingRulesIds = currentRules.stream()
          .filter(rule -> !rule.getValue().equals(config.filterRule))
          .map(Rule::getId).collect(Collectors.toList());
      if (!currentNotMatchingRulesIds.isEmpty()) {
        DeleteRulesRequest delete = new DeleteRulesRequest().delete(new DeleteRulesRequestDelete().ids(currentNotMatchingRulesIds));
        AddOrDeleteRulesResponse deleteRulesResult = apiInstance.tweets().addOrDeleteRules(new AddOrDeleteRulesRequest(delete)).execute(RETRIES);
        log.debug("Delete rules result: " + deleteRulesResult);
      }
    }
    if (currentRules == null || currentRules.stream().noneMatch(rule -> rule.getValue().equals(config.filterRule))) {
      RuleNoId rule = new RuleNoId().value(config.filterRule);
      AddRulesRequest add = new AddRulesRequest().addAddItem(rule);
      AddOrDeleteRulesResponse addRulesResult = apiInstance.tweets().addOrDeleteRules(new AddOrDeleteRulesRequest(add)).execute(RETRIES);
      log.debug("Add rules result: " + addRulesResult);
    } else {
      log.debug("Filter rule already configured");
    }
  }

  private void processFilteredStreamingTweetResponse(String line) {
    try {
      FilteredStreamingTweetResponse tweetResponse = FilteredStreamingTweetResponse.fromJson(line);
      if (tweetResponse != null) {
        onTweet(tweetResponse.getData());
      }
    } catch (Exception ex) {
      log.error("Exception during TweetsSampleStreamResponse processing - will be skipped", ex);
    }
  }

  private void processGet2TweetsSampleStreamResponse(String line) {
    try {
      Get2TweetsSampleStreamResponse tweetResponse = Get2TweetsSampleStreamResponse.fromJson(line);
      if (tweetResponse != null) {
        onTweet(tweetResponse.getData());
      }
    } catch (Exception ex) {
      log.error("Exception during Get2TweetsSampleStreamResponse processing - will be skipped", ex);
    }
  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {
    return messageQueue.getBatch();
  }

  @Override
  public void stop() {
    log.info("Shutting down twitter stream.");
    running = false;
  }

  public void onTweet(Tweet tweet) {
    try {
      Struct value = TweetConverter.convert(tweet);

      Map<String, ?> sourcePartition = ImmutableMap.of();
      Map<String, ?> sourceOffset = ImmutableMap.of();

      SourceRecord record = new SourceRecord(sourcePartition, sourceOffset, config.topic, Schema.STRING_SCHEMA, tweet.getId(), TweetConverter.TWEET_SCHEMA, value);
      messageQueue.add(record);
    } catch (Exception ex) {
      log.error("Exception thrown", ex);
    }
  }

}
