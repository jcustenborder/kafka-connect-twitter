package com.github.jcustenborder.kafka.connect.twitter;

import com.github.jcustenborder.kafka.connect.utils.BaseDocumentationTest;
import org.apache.kafka.connect.data.Schema;

import java.util.Arrays;
import java.util.List;

public class DocumentationTest extends BaseDocumentationTest {
  @Override
  protected String[] packages() {
    return new String[]{this.getClass().getPackage().getName()};
  }

  @Override
  protected List<Schema> schemas() {
    return Arrays.asList(
        StatusConverter.STATUS_SCHEMA,
        StatusConverter.STATUS_SCHEMA_KEY,
        StatusConverter.GEO_LOCATION_SCHEMA,
        StatusConverter.PLACE_SCHEMA,
        StatusConverter.SCHEMA_STATUS_DELETION_NOTICE,
        StatusConverter.SCHEMA_STATUS_DELETION_NOTICE_KEY,
        StatusConverter.USER_SCHEMA
    );
  }
}
