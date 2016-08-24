package io.confluent.kafka.connect.twitter;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwitterSourceConnector extends SourceConnector {
  private static Logger log = LoggerFactory.getLogger(TwitterSourceConnector.class);
  private TwitterSourceConnectorConfig config;

  @Override
  public String version() {
    return VersionUtil.getVersion();
  }

  Map<String, String> settings;

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

    String[] keywords = this.config.filterKeywords();

    final int tasks = Math.min(maxTasks, keywords.length);

    List<List<String>> taskGroups = new ArrayList<>();

    for (int i = 0; i < tasks; i++) {
      taskGroups.add(new ArrayList<String>());
    }

    int index = 0;

    for (String keyword : this.config.filterKeywords()) {
      int taskGroupIndex = index % taskGroups.size();
      List<String> taskList = taskGroups.get(taskGroupIndex);
      taskList.add(keyword);
      index++;
    }

    List<Map<String, String>> taskConfigs = new ArrayList<>();

    for (List<String> k : taskGroups) {
      Map<String, String> taskSettings = new HashMap<>();
      taskSettings.putAll(this.settings);

      if(!k.isEmpty()){
        taskSettings.put(TwitterSourceConnectorConfig.FILTER_KEYWORDS_CONF, Joiner.on(',').join(k));
        taskConfigs.add(taskSettings);
      }
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
