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

import com.github.jcustenborder.kafka.connect.utils.config.ConfigKeyBuilder;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.types.Password;

import java.util.Map;

public class TwitterSourceConnectorConfig extends AbstractConfig {

  public static final String TWITTER_BEARER_TOKEN_CONF = "twitter.bearerToken";
  public static final String FILTER_RULE_CONF = "filter.rule";
  public static final String TWEET_FIELDS_CONF = "tweet.fields";
  public static final String KAFKA_TWEETS_TOPIC_CONF = "kafka.tweets.topic";
  public static final String KAFKA_TWEETS_TOPIC_DOC = "Kafka topic to write the tweets to.";
  public static final String QUEUE_EMPTY_MS_CONF = "queue.empty.ms";
  public static final String QUEUE_BATCH_SIZE_CONF = "queue.batch.size";
  private static final String TWITTER_BEARER_TOKEN_DOC = "Bearer token";
  private static final String FILTER_RULE_DOC = "Twitter rule used in filtering.";

  private static final String TWEET_FIELDS_DOC = "Fields that will be returned for tweet.";
  public static final String QUEUE_EMPTY_MS_DOC = "The amount of time to wait if there are no records in the queue.";
  public static final String QUEUE_BATCH_SIZE_DOC = "The number of records to return in a single batch.";


  public final String topic;
  public final String filterRule;

  public final String tweetFields;
  public final int queueEmptyMs;
  public final int queueBatchSize;
  public final Password bearerToken;

  public TwitterSourceConnectorConfig(Map<String, String> parsedConfig) {
    super(conf(), parsedConfig);
    this.topic = getString(KAFKA_TWEETS_TOPIC_CONF);
    this.filterRule = getString(FILTER_RULE_CONF);
    this.tweetFields = getString(TWEET_FIELDS_CONF);
    this.queueBatchSize = getInt(QUEUE_BATCH_SIZE_CONF);
    this.queueEmptyMs = getInt(QUEUE_EMPTY_MS_CONF);
    this.bearerToken = getPassword(TWITTER_BEARER_TOKEN_CONF);
  }

  public static ConfigDef conf() {
    return new ConfigDef()
        .define(TWITTER_BEARER_TOKEN_CONF, Type.PASSWORD, Importance.HIGH, TWITTER_BEARER_TOKEN_DOC)
        .define(FILTER_RULE_CONF, Type.STRING, null, Importance.HIGH, FILTER_RULE_DOC)
        .define(TWEET_FIELDS_CONF, Type.STRING, null, Importance.HIGH, TWEET_FIELDS_CONF)
        .define(KAFKA_TWEETS_TOPIC_CONF, Type.STRING, Importance.HIGH, KAFKA_TWEETS_TOPIC_DOC)
        .define(
            ConfigKeyBuilder.of(QUEUE_EMPTY_MS_CONF, Type.INT)
                .importance(Importance.LOW)
                .documentation(QUEUE_EMPTY_MS_DOC)
                .defaultValue(100)
                .validator(ConfigDef.Range.atLeast(10))
                .build()
        )
        .define(
            ConfigKeyBuilder.of(QUEUE_BATCH_SIZE_CONF, Type.INT)
                .importance(Importance.LOW)
                .documentation(QUEUE_BATCH_SIZE_DOC)
                .defaultValue(100)
                .validator(ConfigDef.Range.atLeast(1))
                .build()
        );
  }

}
