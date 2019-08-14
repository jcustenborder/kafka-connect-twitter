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
import com.github.jcustenborder.kafka.connect.utils.config.ConfigUtils;
import com.google.common.primitives.Longs;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigException;
import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;


public class TwitterSourceConnectorConfig extends AbstractConfig {

  public static final String TWITTER_DEBUG_CONF = "twitter.debug";
  public static final String TWITTER_OAUTH_CONSUMER_KEY_CONF = "twitter.oauth.consumerKey";
  public static final String TWITTER_OAUTH_SECRET_KEY_CONF = "twitter.oauth.consumerSecret";
  public static final String TWITTER_OAUTH_ACCESS_TOKEN_CONF = "twitter.oauth.accessToken";
  public static final String TWITTER_OAUTH_ACCESS_TOKEN_SECRET_CONF = "twitter.oauth.accessTokenSecret";
  public static final String FILTER_KEYWORDS_CONF = "filter.keywords";
  public static final String FILTER_USER_IDS_CONF = "filter.userIds";
  public static final String KAFKA_STATUS_TOPIC_CONF = "kafka.status.topic";
  public static final String KAFKA_STATUS_TOPIC_DOC = "Kafka topic to write the statuses to.";
  public static final String PROCESS_DELETES_CONF = "process.deletes";
  public static final String PROCESS_DELETES_DOC = "Should this connector process deletes.";
  public static final String QUEUE_EMPTY_MS_CONF = "queue.empty.ms";
  public static final String QUEUE_BATCH_SIZE_CONF = "queue.batch.size";
  private static final String TWITTER_DEBUG_DOC = "Flag to enable debug logging for the twitter api.";
  private static final String TWITTER_OAUTH_CONSUMER_KEY_DOC = "OAuth consumer key";
  private static final String TWITTER_OAUTH_SECRET_KEY_DOC = "OAuth consumer secret";
  private static final String TWITTER_OAUTH_ACCESS_TOKEN_DOC = "OAuth access token";
  private static final String TWITTER_OAUTH_ACCESS_TOKEN_SECRET_DOC = "OAuth access token secret";
  private static final String FILTER_KEYWORDS_DOC = "Twitter keywords to filter for.";
  private static final String FILTER_USER_IDS_DOC = "Twitter user IDs to follow.";
  public static final String QUEUE_EMPTY_MS_DOC = "The amount of time to wait if there are no records in the queue.";
  public static final String QUEUE_BATCH_SIZE_DOC = "The number of records to return in a single batch.";


  public final String topic;
  public final boolean twitterDebug;
  public final boolean processDeletes;
  public final Set<String> filterKeywords;
  public final Set<Long> filterUserIds;
  public final int queueEmptyMs;
  public final int queueBatchSize;


  public TwitterSourceConnectorConfig(Map<String, String> parsedConfig) {
    super(conf(), parsedConfig);
    this.topic = this.getString(KAFKA_STATUS_TOPIC_CONF);
    this.twitterDebug = this.getBoolean(TWITTER_DEBUG_CONF);
    this.processDeletes = this.getBoolean(PROCESS_DELETES_CONF);
    this.filterKeywords = ConfigUtils.getSet(this, FILTER_KEYWORDS_CONF);
    this.filterUserIds = ConfigUtils.getSet(this, FILTER_USER_IDS_CONF)
        .stream()
        .map(Long::parseLong)
        .collect(Collectors.toSet());
    this.queueBatchSize = getInt(QUEUE_BATCH_SIZE_CONF);
    this.queueEmptyMs = getInt(QUEUE_EMPTY_MS_CONF);
  }

  static class UserIdValidator implements ConfigDef.Validator {
    @Override
    public void ensureValid(String key, Object o) {
      if (o instanceof List) {
        List<String> userIds = (List<String>) o;
        for (String userId : userIds) {
          if (null == Longs.tryParse(userId)) {
            throw new ConfigException(key, userId, "Could not parse to long.");
          }
        }
      }
    }
  }

  static final ConfigDef.Validator USERID_VALIDATOR = new UserIdValidator();

  public static ConfigDef conf() {
    return new ConfigDef()
        .define(TWITTER_DEBUG_CONF, Type.BOOLEAN, false, Importance.LOW, TWITTER_DEBUG_DOC)
        .define(TWITTER_OAUTH_CONSUMER_KEY_CONF, Type.PASSWORD, Importance.HIGH, TWITTER_OAUTH_CONSUMER_KEY_DOC)
        .define(TWITTER_OAUTH_SECRET_KEY_CONF, Type.PASSWORD, Importance.HIGH, TWITTER_OAUTH_SECRET_KEY_DOC)
        .define(TWITTER_OAUTH_ACCESS_TOKEN_CONF, Type.PASSWORD, Importance.HIGH, TWITTER_OAUTH_ACCESS_TOKEN_DOC)
        .define(TWITTER_OAUTH_ACCESS_TOKEN_SECRET_CONF, Type.PASSWORD, Importance.HIGH, TWITTER_OAUTH_ACCESS_TOKEN_SECRET_DOC)
        .define(FILTER_KEYWORDS_CONF, Type.LIST, Importance.HIGH, FILTER_KEYWORDS_DOC)
        .define(
            ConfigKeyBuilder.of(FILTER_USER_IDS_CONF, Type.LIST)
                .importance(Importance.HIGH)
                .documentation(FILTER_USER_IDS_DOC)
                .defaultValue(Collections.emptyList())
                .validator(USERID_VALIDATOR)
                .build()
        )
        .define(KAFKA_STATUS_TOPIC_CONF, Type.STRING, Importance.HIGH, KAFKA_STATUS_TOPIC_DOC)
        .define(PROCESS_DELETES_CONF, Type.BOOLEAN, Importance.HIGH, PROCESS_DELETES_DOC)
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


  public Configuration configuration() {
    Properties properties = new Properties();
    /*
      Grab all of the key/values that have a key that starts with twitter. This will strip 'twitter.' from beginning of
      each key. This aligns with what the twitter4j framework is expecting.
     */
    properties.putAll(this.originalsWithPrefix("twitter."));
    return new PropertyConfiguration(properties);
  }
}
