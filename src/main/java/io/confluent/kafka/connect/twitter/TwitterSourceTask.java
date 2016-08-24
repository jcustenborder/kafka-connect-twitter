package io.confluent.kafka.connect.twitter;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TwitterSourceTask extends SourceTask implements StatusListener {
  static final Logger log = LoggerFactory.getLogger(TwitterSourceTask.class);
  final ConcurrentLinkedDeque<SourceRecord> messageQueue = new ConcurrentLinkedDeque<>();

  TwitterStream twitterStream;

  @Override
  public String version() {
    return VersionUtil.getVersion();
  }

  TwitterSourceConnectorConfig config;

  @Override
  public void start(Map<String, String> map) {
    this.config = new TwitterSourceConnectorConfig(map);

    TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(this.config.configuration());
    this.twitterStream = twitterStreamFactory.getInstance();

    String[] keywords = this.config.filterKeywords();

    if (log.isInfoEnabled()) {
      log.info("Setting up filters. Keywords = {}", Joiner.on(", ").join(keywords));
    }

    FilterQuery filterQuery = new FilterQuery();
    filterQuery.track(keywords);

    if (log.isInfoEnabled()) {
      log.info("Starting the twitter stream.");
    }
    twitterStream.addListener(this);
    twitterStream.filter(filterQuery);
  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {
    List<SourceRecord> records = new ArrayList<>(256);

    while (records.isEmpty()) {
      int size = messageQueue.size();

      for (int i = 0; i < size; i++) {
        SourceRecord record = this.messageQueue.poll();

        if (null == record) {
          break;
        }

        records.add(record);
      }

      if (records.isEmpty()) {
        Thread.sleep(100);
      }
    }

    return records;
  }

  @Override
  public void stop() {
    if (log.isInfoEnabled()) {
      log.info("Shutting down twitter stream.");
    }
    twitterStream.shutdown();
  }

  @Override
  public void onStatus(Status status) {
    try {
      Struct keyStruct = new Struct(StatusConverter.statusSchemaKey);
      Struct valueStruct = new Struct(StatusConverter.statusSchema);

      StatusConverter.convertKey(status, keyStruct);
      StatusConverter.convert(status, valueStruct);

      Map<String, ?> sourcePartition = ImmutableMap.of();
      Map<String, ?> sourceOffset = ImmutableMap.of();

      SourceRecord record = new SourceRecord(sourcePartition, sourceOffset, this.config.kafkaStatusTopic(), StatusConverter.statusSchemaKey, keyStruct, StatusConverter.statusSchema, valueStruct);
      this.messageQueue.add(record);
    } catch (Exception ex) {
      if (log.isErrorEnabled()) {
        log.error("Exception thrown", ex);
      }
    }
  }

  @Override
  public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
    if (!this.config.processDeletes()) {
      return;
    }

    try {
      Struct keyStruct = new Struct(StatusConverter.schemaStatusDeletionNoticeKey);
      Struct valueStruct = new Struct(StatusConverter.schemaStatusDeletionNotice);

      StatusConverter.convertKey(statusDeletionNotice, keyStruct);
      StatusConverter.convert(statusDeletionNotice, valueStruct);

      Map<String, ?> sourcePartition = ImmutableMap.of();
      Map<String, ?> sourceOffset = ImmutableMap.of();

      SourceRecord record = new SourceRecord(sourcePartition, sourceOffset, this.config.kafkaDeleteTopic(), StatusConverter.schemaStatusDeletionNoticeKey, keyStruct, StatusConverter.schemaStatusDeletionNotice, valueStruct);
      this.messageQueue.add(record);
    } catch (Exception ex) {
      if (log.isErrorEnabled()) {
        log.error("Exception thrown", ex);
      }
    }
  }

  @Override
  public void onTrackLimitationNotice(int i) {

  }

  @Override
  public void onScrubGeo(long l, long l1) {

  }

  @Override
  public void onStallWarning(StallWarning stallWarning) {
    if (log.isWarnEnabled()) {
      log.warn("code = '{}' percentFull = '{}' - {}", stallWarning.getCode(), stallWarning.getPercentFull(), stallWarning.getMessage());
    }
  }

  @Override
  public void onException(Exception e) {
    if (log.isErrorEnabled()) {
      log.error("onException", e);
    }
  }
}