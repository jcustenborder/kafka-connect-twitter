package com.github.jcustenborder.kafka.connect.twitter;

import com.google.common.base.CaseFormat;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import twitter4j.MediaEntity;
import twitter4j.TweetEntity;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SchemaGeneratorTest {

  <T> List<Class<? extends T>> list(Reflections reflections, Class<T> cls) {
    List<Class<? extends T>> classes = reflections.getSubTypesOf(cls)
        .stream()
        .filter(aClass -> Modifier.isInterface(aClass.getModifiers()))
        .collect(Collectors.toList());
    classes.sort(Comparator.comparing(Class::getName));
    return classes;
  }

  String schema(Class<?> cls) {
    String result;

    if (String.class.equals(cls)) {
      result = "SchemaBuilder.string().optional().doc(\"\").build()";
    } else if (int.class.equals(cls)) {
      result = "SchemaBuilder.int32().optional().doc(\"\").build()";
    } else if (long.class.equals(cls)) {
      result = "SchemaBuilder.int64().optional().doc(\"\").build()";
    } else if (cls.isArray()) {
      String childSchema = schema(cls.getComponentType());
      result = String.format("SchemaBuilder.array(%s).optional().doc(\"\").build()", childSchema);
    } else if (Map.class.isAssignableFrom(cls)) {
      result = "SchemaBuilder.map(Schema.STRING_SCHEMA, SCHEMA_MEDIA_ENTITY_SIZE)";

    } else {
      result = "SCHEMA_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, cls.getSimpleName()).replace('$', '_');
    }


    return result;
  }

  void processClass(Class<?> cls, StringBuilder builder) {

    final String schemaConstantName;
    final String schemaName;
    final String typeName;
    final String convertMethodName;

    if (null == cls.getDeclaringClass()) {
      schemaConstantName = "SCHEMA_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, cls.getSimpleName());
      schemaName = String.format("com.github.jcustenborder.kafka.connect.twitter.%s", cls.getSimpleName());
      typeName = cls.getSimpleName();
      convertMethodName = String.format("convert%s", cls.getSimpleName());
    } else {
      schemaConstantName = "SCHEMA_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, cls.getDeclaringClass().getSimpleName() + cls.getSimpleName());
      typeName = String.format("%s.%s", cls.getDeclaringClass().getSimpleName(), cls.getSimpleName());
      schemaName = String.format("com.github.jcustenborder.kafka.connect.twitter.%s.%s", cls.getSimpleName(), cls.getDeclaringClass().getSimpleName());
      convertMethodName = String.format("convert%s%s", cls.getDeclaringClass().getSimpleName(), cls.getSimpleName());
    }


    builder.append(String.format("public static final Schema  %s =SchemaBuilder.struct()\n", schemaConstantName));
    builder.append(String.format("  .name(\"%s\")\n", schemaName));
    builder.append("  .doc(\"\")\n");

    Set<String> methods = new HashSet<>();
    for (Method method : cls.getMethods()) {
      String methodName = method.getName().replace("get", "");
      if (!methods.add(methodName)) {
        continue;
      }
      String expectedSchema = schema(method.getReturnType());
      builder.append(String.format("  .field(\"%s\", %s)\n", methodName, expectedSchema));
    }
    builder.append("  .build();\n\n");

    methods.clear();
    String variableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, cls.getSimpleName());
    builder.append(String.format("static Struct %s(%s %s) {\n", convertMethodName, typeName, variableName));
    builder.append(String.format("  return new Struct(%s)", schemaConstantName));
    for (Method method : cls.getMethods()) {
      String methodName = method.getName().replace("get", "");
      if (!methods.add(methodName)) {
        continue;
      }
      builder.append(String.format("\n    .put(\"%s\", %s.%s())", methodName, variableName, method.getName()));
    }
    builder.append(";\n  }\n");

    builder.append("\n");
    builder.append(String.format("public static List<Struct> convert(%s[] items) {\n", typeName));
    builder.append("  List<Struct> result = new ArrayList<>();\n");
    builder.append("  if(null==items) {\n");
    builder.append("    return result;\n");
    builder.append("  }\n");
    builder.append(String.format("  for(%s item: items) {\n", typeName));
    builder.append(String.format("    Struct struct = %s(item);\n", convertMethodName));
    builder.append("    result.add(struct);\n");
    builder.append("  }\n");
    builder.append("  return result;\n");
    builder.append("}\n");

//      }
//    public static List<Struct> convert(UserMentionEntity[] userMentionEntities) {
//      List<Struct> result = new ArrayList<>();
//      if(null==userMentionEntities) {
//        return result;
//      }
//      for(UserMentionEntity item: userMentionEntities) {
//        Struct struct = convertUserMentionEntity(item);
//        result.add(struct);
//      }
//      return result;
//    }


  }

  @Test
  public void tweetEntities() {
    Reflections reflections = new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.forJavaClassPath())
        .forPackages(TweetEntity.class.getPackage().getName())
    );

    List<Class<?>> allClasses = new ArrayList<>();
    List<Class<? extends TweetEntity>> classes = list(reflections, TweetEntity.class);
    allClasses.add(MediaEntity.Variant.class);
    allClasses.add(MediaEntity.Size.class);
    allClasses.addAll(classes);


    for (Class<?> cls : allClasses) {
      StringBuilder builder = new StringBuilder();
      processClass(cls, builder);

      System.out.println(builder);
    }


  }


}
