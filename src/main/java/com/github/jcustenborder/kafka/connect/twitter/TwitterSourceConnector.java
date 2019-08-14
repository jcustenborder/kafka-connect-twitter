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

import com.github.jcustenborder.kafka.connect.utils.VersionUtil;
import com.github.jcustenborder.kafka.connect.utils.config.Description;
import com.github.jcustenborder.kafka.connect.utils.config.Title;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Title("Twitter")
@Description("This Twitter Source connector is used to pull data from Twitter in realtime.")
public class TwitterSourceConnector extends SourceConnector {
  private static Logger log = LoggerFactory.getLogger(TwitterSourceConnector.class);
  Map<String, String> settings;
  private TwitterSourceConnectorConfig config;

  @Override
  public String version() {
    return VersionUtil.version(this.getClass());
  }

  @Override
  public void start(Map<String, String> map) {
    this.config = new TwitterSourceConnectorConfig(map);
    this.settings = map;
  }

  @Override
  public Class<? extends Task> taskClass() {
    return TwitterSourceTask.class;
  }

  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    Preconditions.checkState(maxTasks > 0, "MaxTasks must be greater than 0");
    final int tasks = Math.min(maxTasks, this.config.filterKeywords.size());


    Multimap<Integer, String> taskToKeywords = ArrayListMultimap.create();
    int index = 0;
    for (String keyword : this.config.filterKeywords) {
      final int taskID = index % tasks;
      taskToKeywords.put(taskID, keyword);
      index++;
    }
    final List<Map<String, String>> taskConfigs = new ArrayList<>(tasks);

    for (Integer taskID : taskToKeywords.keySet()) {
      Collection<String> keywords = taskToKeywords.get(taskID);
      Map<String, String> taskSettings = new LinkedHashMap<>(this.settings);
      taskSettings.put(TwitterSourceConnectorConfig.FILTER_KEYWORDS_CONF, Joiner.on(',').join(keywords));
      taskConfigs.add(taskSettings);
    }

    return taskConfigs;
  }

  @Override
  public void stop() {

  }

  @Override
  public ConfigDef config() {
    return TwitterSourceConnectorConfig.conf();
  }
}
