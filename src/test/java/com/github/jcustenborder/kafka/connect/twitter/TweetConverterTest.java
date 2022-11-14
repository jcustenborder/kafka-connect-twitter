/**
 * Copyright © 2022 Arek Burdach (arek.burdach@gmail.com)
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

import com.twitter.clientlib.JSON;
import com.twitter.clientlib.model.FullTextEntities;
import com.twitter.clientlib.model.HashtagEntity;
import com.twitter.clientlib.model.Point;
import com.twitter.clientlib.model.Tweet;
import com.twitter.clientlib.model.TweetEditControls;
import com.twitter.clientlib.model.TweetGeo;
import com.twitter.clientlib.model.UrlEntity;
import io.confluent.connect.avro.AvroConverter;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.connect.data.Struct;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TweetConverterTest {

  {
    // init twitter json deserializers
    new JSON();
  }

  @Test
  public void shouldConvertTweetWithPhoto() throws IOException {
    Tweet tweet = Tweet.fromJson(IOUtils.resourceToString("/sample_tweets/with-photo.json", StandardCharsets.UTF_8));
    Struct result = TweetConverter.convert(tweet);
    assertEquals("How times have changed https://t.co/gCxUkZ4kZC", result.getString(Tweet.SERIALIZED_NAME_TEXT));
    assertEquals(5, result
        .getStruct(Tweet.SERIALIZED_NAME_EDIT_CONTROLS)
        .getInt32(TweetEditControls.SERIALIZED_NAME_EDITS_REMAINING));
    assertEquals("https://t.co/gCxUkZ4kZC", result
        .getStruct(Tweet.SERIALIZED_NAME_ENTITIES)
        .<Struct>getArray(FullTextEntities.SERIALIZED_NAME_URLS)
        .get(0)
        .getString(UrlEntity.SERIALIZED_NAME_URL));
    assertEquals("everyone", result.getString(Tweet.SERIALIZED_NAME_REPLY_SETTINGS));
  }

  @Test
  public void shouldConvertTweetWithReference() throws IOException {
    Tweet tweet = Tweet.fromJson(IOUtils.resourceToString("/sample_tweets/with-reference.json", StandardCharsets.UTF_8));
    TweetConverter.convert(tweet);
  }

  @Test
  public void shouldConvertReplyTweet() throws IOException {
    Tweet tweet = Tweet.fromJson(IOUtils.resourceToString("/sample_tweets/reply.json", StandardCharsets.UTF_8));
    TweetConverter.convert(tweet);
  }

  @Test
  public void shouldConvertTweetWithHashtag() throws IOException {
    Tweet tweet = Tweet.fromJson(IOUtils.resourceToString("/sample_tweets/with-hashtag.json", StandardCharsets.UTF_8));
    Struct result = TweetConverter.convert(tweet);
    assertEquals("Bitcoin", result
        .getStruct(Tweet.SERIALIZED_NAME_ENTITIES)
        .<Struct>getArray(FullTextEntities.SERIALIZED_NAME_HASHTAGS)
        .get(0)
        .getString(HashtagEntity.SERIALIZED_NAME_TAG));
  }

  @Test
  public void shouldConvertDecimalToDesiredScale() {
    Tweet tweet = new Tweet();
    tweet.setId("foo");
    tweet.setText("foo");
    TweetGeo geo = new TweetGeo();
    Point point = new Point();
    point.setType(Point.TypeEnum.POINT);
    point.setCoordinates(Arrays.asList(new BigDecimal("12.12345678"), new BigDecimal("12.1234567")));
    geo.setCoordinates(point);
    tweet.setGeo(geo);
    Struct result = TweetConverter.convert(tweet);
    AvroConverter converter = new AvroConverter(new MockSchemaRegistryClient());
    converter.configure(Collections.singletonMap("schema.registry.url", "http://localhost:8080/not_used"), false);
    converter.fromConnectData("foo", TweetConverter.TWEET_SCHEMA, result);
  }

}
