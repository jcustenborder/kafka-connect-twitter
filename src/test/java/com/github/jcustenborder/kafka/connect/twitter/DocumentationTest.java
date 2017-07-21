package com.github.jcustenborder.kafka.connect.twitter;

import com.github.jcustenborder.kafka.connect.utils.BaseDocumentationTest;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.kafka.connect.data.Schema;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentationTest extends BaseDocumentationTest {
  @Override
  protected String[] packages() {
    return new String[]{this.getClass().getPackage().getName()};
  }

  static Schema schema(Field field) {
    try {
      return (Schema) field.get(null);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  protected List<Schema> schemas() {
    return Arrays.stream(StatusConverter.class.getFields())
        .filter(field -> Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && Schema.class.equals(field.getType()))
        .map(field -> schema(field))
        .collect(Collectors.toList());
  }
}
