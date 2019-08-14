/**
 * Copyright © 2016 Jeremy Custenborder (jcustenborder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jcustenborder.kafka.connect.twitter;


import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class TwitterSourceConnectorTest {

  TwitterSourceConnector connector;
  Map<String, String> defaultSettings;

  @BeforeEach
  public void setup() {
    this.connector = new TwitterSourceConnector();
    this.defaultSettings = new LinkedHashMap<>();
    this.defaultSettings.put(TwitterSourceConnectorConfig.TWITTER_OAUTH_ACCESS_TOKEN_CONF, "xxxxxx");
    this.defaultSettings.put(TwitterSourceConnectorConfig.TWITTER_OAUTH_SECRET_KEY_CONF, "xxxxxx");
    this.defaultSettings.put(TwitterSourceConnectorConfig.TWITTER_OAUTH_CONSUMER_KEY_CONF, "xxxxxx");
    this.defaultSettings.put(TwitterSourceConnectorConfig.TWITTER_OAUTH_ACCESS_TOKEN_SECRET_CONF, "xxxxxx");
    this.defaultSettings.put(TwitterSourceConnectorConfig.KAFKA_STATUS_TOPIC_CONF, "xxxxxx");
    this.defaultSettings.put(TwitterSourceConnectorConfig.PROCESS_DELETES_CONF, "false");

  }

  List<Map<String, String>> expectedSettings(List<String>... keywords) {
    List<Map<String, String>> result = new ArrayList<>();
    for (List<String> keywordSet : keywords) {
      Map<String, String> settings = new LinkedHashMap<>(this.defaultSettings);
      settings.put(TwitterSourceConnectorConfig.FILTER_KEYWORDS_CONF, Joiner.on(',').join(keywordSet));
      result.add(settings);
    }
    return result;
  }

  @TestFactory
  public Stream<DynamicTest> taskConfigs() {

    Map<Integer, List<Map<String, String>>> testCases = ImmutableMap.of(
        1, expectedSettings(Arrays.asList("one", "two", "three")),
        2, expectedSettings(Arrays.asList("one", "three"), Arrays.asList("two")),
        3, expectedSettings(Arrays.asList("one"), Arrays.asList("two"), Arrays.asList("three"))
    );

    return testCases.entrySet().stream()
        .map(e -> dynamicTest(e.getKey().toString(), () -> {
          this.defaultSettings.put(TwitterSourceConnectorConfig.FILTER_KEYWORDS_CONF, "one,two,three");
          this.connector.start(this.defaultSettings);
          List<Map<String, String>> taskConfigs = this.connector.taskConfigs(e.getKey());
          assertEquals(
              e.getValue(),
              taskConfigs
          );
        }));


  }

}
