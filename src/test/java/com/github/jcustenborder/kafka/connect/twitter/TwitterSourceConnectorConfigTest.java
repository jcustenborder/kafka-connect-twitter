package com.github.jcustenborder.kafka.connect.twitter;

import io.confluent.kafka.connect.utils.config.MarkdownFormatter;
import org.junit.jupiter.api.Test;

public class TwitterSourceConnectorConfigTest {
  @Test
  public void doc() {
    System.out.println(MarkdownFormatter.toMarkdown(TwitterSourceConnectorConfig.conf()));
  }
}