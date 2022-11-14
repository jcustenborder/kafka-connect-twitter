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

import com.twitter.clientlib.model.CashtagEntity;
import com.twitter.clientlib.model.ContextAnnotation;
import com.twitter.clientlib.model.ContextAnnotationDomainFields;
import com.twitter.clientlib.model.ContextAnnotationEntityFields;
import com.twitter.clientlib.model.FullTextEntities;
import com.twitter.clientlib.model.HashtagEntity;
import com.twitter.clientlib.model.MentionEntity;
import com.twitter.clientlib.model.Point;
import com.twitter.clientlib.model.ReplySettings;
import com.twitter.clientlib.model.Tweet;
import com.twitter.clientlib.model.TweetAttachments;
import com.twitter.clientlib.model.TweetEditControls;
import com.twitter.clientlib.model.TweetGeo;
import com.twitter.clientlib.model.TweetNonPublicMetrics;
import com.twitter.clientlib.model.TweetOrganicMetrics;
import com.twitter.clientlib.model.TweetPromotedMetrics;
import com.twitter.clientlib.model.TweetPublicMetrics;
import com.twitter.clientlib.model.TweetReferencedTweets;
import com.twitter.clientlib.model.TweetWithheld;
import com.twitter.clientlib.model.UrlEntity;
import com.twitter.clientlib.model.UrlImage;
import org.apache.kafka.connect.data.Decimal;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.data.Timestamp;

import javax.annotation.Nonnull;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

public class TweetConverter {

  /*{
      "type" : "object",
      "required" : [ "type", "id" ],
      "properties" : {
        "type" : {
          "type" : "string",
          "enum" : [ "retweeted", "quoted", "replied_to" ]
        },
        "id" : {
          "type" : "string",
          "pattern" : "^[0-9]{1,19}$"
        }
      }
    }*/
  public static final Schema TWEET_REFERENCED_TWEETS_ITEM_SCHEMA = SchemaBuilder.struct()
      .field(TweetReferencedTweets.SERIALIZED_NAME_TYPE, Schema.STRING_SCHEMA)
      .field(TweetReferencedTweets.SERIALIZED_NAME_ID, Schema.STRING_SCHEMA)
      .build();


  public static Struct convert(@Nonnull TweetReferencedTweets input) {
    return new Struct(TWEET_REFERENCED_TWEETS_ITEM_SCHEMA)
        .put(TweetReferencedTweets.SERIALIZED_NAME_TYPE, input.getType().getValue())
        .put(TweetReferencedTweets.SERIALIZED_NAME_ID, input.getId());
  }

  /*{
      "type" : "object",
      "properties" : {
        "media_keys" : {
          "type" : "array",
          "items" : {
            "type" : "string",
            "pattern" : "^([0-9]+)_([0-9]+)$"
          },
          "minItems" : 1
        },
        "poll_ids" : {
          "type" : "array",
          "items" : {
            "type" : "string",
            "pattern" : "^[0-9]{1,19}$"
          },
          "minItems" : 1
        }
      }
    }*/
  public static final Schema TWEET_ATTACHMENTS_SCHEMA = SchemaBuilder.struct()
      .optional()
      .field(TweetAttachments.SERIALIZED_NAME_MEDIA_KEYS, SchemaBuilder.array(Schema.STRING_SCHEMA).optional())
      .field(TweetAttachments.SERIALIZED_NAME_POLL_IDS, SchemaBuilder.array(Schema.STRING_SCHEMA).optional())
      .build();

  public static Struct convert(@Nonnull TweetAttachments input) {
    return new Struct(TWEET_ATTACHMENTS_SCHEMA)
        .put(TweetAttachments.SERIALIZED_NAME_MEDIA_KEYS, input.getMediaKeys())
        .put(TweetAttachments.SERIALIZED_NAME_POLL_IDS, input.getPollIds());
  }

  /*{
      "type" : "object",
      "required" : [ "id" ],
      "properties" : {
        "id" : {
          "type" : "string",
          "pattern" : "^[0-9]{1,19}$"
        },
        "name" : {
          "type" : "string"
        },
        "description" : {
          "type" : "string"
        }
      }
    }*/
  public static final Schema CONTEXT_ANNOTATION_DOMAIN_FIELDS_SCHEMA = SchemaBuilder.struct()
      .field(ContextAnnotationDomainFields.SERIALIZED_NAME_ID, Schema.STRING_SCHEMA)
      .field(ContextAnnotationDomainFields.SERIALIZED_NAME_NAME, Schema.OPTIONAL_STRING_SCHEMA)
      .field(ContextAnnotationDomainFields.SERIALIZED_NAME_DESCRIPTION, Schema.OPTIONAL_STRING_SCHEMA)
      .build();

  public static Struct convert(@Nonnull ContextAnnotationDomainFields input) {
    return new Struct(CONTEXT_ANNOTATION_DOMAIN_FIELDS_SCHEMA)
        .put(ContextAnnotationDomainFields.SERIALIZED_NAME_ID, input.getId())
        .put(ContextAnnotationDomainFields.SERIALIZED_NAME_NAME, input.getName())
        .put(ContextAnnotationDomainFields.SERIALIZED_NAME_DESCRIPTION, input.getDescription());
  }

  /*{
      "type" : "object",
      "required" : [ "id" ],
      "properties" : {
        "id" : {
          "type" : "string",
          "pattern" : "^[0-9]{1,19}$"
        },
        "name" : {
          "type" : "string"
        },
        "description" : {
          "type" : "string"
        }
      }
    }*/
  public static final Schema CONTEXT_ANNOTATION_DOMAIN_ENTITY_SCHEMA = SchemaBuilder.struct()
      .field(ContextAnnotationEntityFields.SERIALIZED_NAME_ID, Schema.STRING_SCHEMA)
      .field(ContextAnnotationEntityFields.SERIALIZED_NAME_NAME, Schema.OPTIONAL_STRING_SCHEMA)
      .field(ContextAnnotationEntityFields.SERIALIZED_NAME_DESCRIPTION, Schema.OPTIONAL_STRING_SCHEMA)
      .build();

  public static Struct convert(@Nonnull ContextAnnotationEntityFields input) {
    return new Struct(CONTEXT_ANNOTATION_DOMAIN_ENTITY_SCHEMA)
        .put(ContextAnnotationEntityFields.SERIALIZED_NAME_ID, input.getId())
        .put(ContextAnnotationEntityFields.SERIALIZED_NAME_NAME, input.getName())
        .put(ContextAnnotationEntityFields.SERIALIZED_NAME_DESCRIPTION, input.getDescription());
  }

  /*{
      "type" : "object",
      "required" : [ "domain", "entity" ],
      "properties" : {
        "domain" : {
          "$ref" : "#/components/schemas/ContextAnnotationDomainFields"
        },
        "entity" : {
          "$ref" : "#/components/schemas/ContextAnnotationEntityFields"
        }
      }
    }*/
  public static final Schema CONTEXT_ANNOTATION_SCHEMA = SchemaBuilder.struct()
      .optional()
      .field(ContextAnnotation.SERIALIZED_NAME_DOMAIN, CONTEXT_ANNOTATION_DOMAIN_FIELDS_SCHEMA)
      .field(ContextAnnotation.SERIALIZED_NAME_ENTITY, CONTEXT_ANNOTATION_DOMAIN_ENTITY_SCHEMA)
      .build();

  public static Struct convert(@Nonnull ContextAnnotation input) {
    return new Struct(CONTEXT_ANNOTATION_SCHEMA)
        .put(ContextAnnotation.SERIALIZED_NAME_DOMAIN, convert(input.getDomain()))
        .put(ContextAnnotation.SERIALIZED_NAME_ENTITY, convert(input.getEntity()));
  }

  /*{
      "type" : "object",
      "required" : [ "copyright", "country_codes" ],
      "properties" : {
        "copyright" : {
          "type" : "boolean"
        },
        "country_codes" : {
          "type" : "array",
          "items" : {
            "type" : "string",
            "pattern" : "^[A-Z]{2}$"
          },
          "uniqueItems" : true,
          "minItems" : 1
        },
        "scope" : {
          "type" : "string",
          "enum" : [ "tweet", "user" ]
        }
      }
    }*/
  public static final Schema TWEET_WITHHELD_SCHEMA = SchemaBuilder.struct()
      .optional()
      .field(TweetWithheld.SERIALIZED_NAME_COPYRIGHT, Schema.BOOLEAN_SCHEMA)
      .field(TweetWithheld.SERIALIZED_NAME_COUNTRY_CODES, SchemaBuilder.array(Schema.STRING_SCHEMA))
      .field(TweetWithheld.SERIALIZED_NAME_SCOPE, Schema.OPTIONAL_STRING_SCHEMA)
      .build();

  public static Struct convert(@Nonnull TweetWithheld input) {
    return new Struct(TWEET_WITHHELD_SCHEMA)
        .put(TweetWithheld.SERIALIZED_NAME_COPYRIGHT, input.getCopyright())
        .put(TweetWithheld.SERIALIZED_NAME_COUNTRY_CODES, new ArrayList<>(input.getCountryCodes()))
        .put(TweetWithheld.SERIALIZED_NAME_SCOPE,
            Optional.ofNullable(input.getScope())
                .map(TweetWithheld.ScopeEnum::getValue)
                .orElse(null));
  }

  private static final int POINTS_COORDINATES_SCALE = 8;

  /*{
      "type" : "object",
      "required" : [ "type", "coordinates" ],
      "properties" : {
        "type" : {
          "type" : "string",
          "enum" : [ "Point" ]
        },
        "coordinates" : {
          "type" : "array",
          "items" : {
            "type" : "number"
          },
          "minItems" : 2,
          "maxItems" : 2
        }
      }
    }*/
  public static final Schema POINT_SCHEMA = SchemaBuilder.struct()
      .optional()
      .field(Point.SERIALIZED_NAME_TYPE, Schema.STRING_SCHEMA)
      .field(Point.SERIALIZED_NAME_COORDINATES, SchemaBuilder.array(Decimal.schema(POINTS_COORDINATES_SCALE)))
      .build();

  public static Struct convert(@Nonnull Point input) {
    return new Struct(POINT_SCHEMA)
        .put(Point.SERIALIZED_NAME_TYPE, input.getType().getValue())
        .put(Point.SERIALIZED_NAME_COORDINATES, input.getCoordinates().stream()
            .map(d -> d.setScale(POINTS_COORDINATES_SCALE, RoundingMode.UNNECESSARY))
            .collect(Collectors.toList()));
  }

  /*{
      "type" : "object",
      "properties" : {
        "coordinates" : {
          "$ref" : "#/components/schemas/Point"
        },
        "place_id" : {
          "type" : "string"
        }
      }
    }*/
  public static final Schema TWEET_GEO_SCHEMA = SchemaBuilder.struct()
      .optional()
      .field(TweetGeo.SERIALIZED_NAME_COORDINATES, POINT_SCHEMA)
      .field(TweetGeo.SERIALIZED_NAME_PLACE_ID, Schema.OPTIONAL_STRING_SCHEMA)
      .build();

  public static Struct convert(@Nonnull TweetGeo input) {
    return new Struct(TWEET_GEO_SCHEMA)
        .put(TweetGeo.SERIALIZED_NAME_COORDINATES,
            Optional.ofNullable(input.getCoordinates())
                .map(TweetConverter::convert)
                .orElse(null))
        .put(TweetGeo.SERIALIZED_NAME_PLACE_ID, input.getPlaceId());
  }

  /*{
      "type" : "object",
      "properties" : {
        "url" : {
          "type" : "string",
          "format" : "uri"
        },
        "height" : {
          "type" : "integer",
          "minimum" : 0
        },
        "width" : {
          "type" : "integer",
          "minimum" : 0
        }
      }
    }*/
  public static final Schema URL_IMAGE_SCHEMA = SchemaBuilder.struct()
      .field(UrlImage.SERIALIZED_NAME_URL, Schema.STRING_SCHEMA)
      .field(UrlImage.SERIALIZED_NAME_HEIGHT, Schema.INT32_SCHEMA)
      .field(UrlImage.SERIALIZED_NAME_WIDTH, Schema.INT32_SCHEMA)
      .build();

  public static Struct convert(@Nonnull UrlImage input) {
    return new Struct(URL_IMAGE_SCHEMA)
        .put(UrlImage.SERIALIZED_NAME_URL,
            Optional.ofNullable(input.getUrl())
                .map(URL::toString)
                .orElse(null))
        .put(UrlImage.SERIALIZED_NAME_HEIGHT, input.getHeight())
        .put(UrlImage.SERIALIZED_NAME_WIDTH, input.getWidth());
  }

  /*{
      "type" : "object",
      "required" : [ "start", "end", "url" ],
      "properties" : {
        "start" : {
          "type" : "integer",
          "minimum" : 0
        },
        "end" : {
          "type" : "integer",
          "minimum" : 0
        },
        "url" : {
          "type" : "string",
          "format" : "uri"
        },
        "expanded_url" : {
          "type" : "string",
          "format" : "uri"
        },
        "display_url" : {
          "type" : "string"
        },
        "unwound_url" : {
          "type" : "string",
          "format" : "uri"
        },
        "status" : {
          "type" : "integer",
          "minimum" : 100,
          "maximum" : 599
        },
        "title" : {
          "type" : "string"
        },
        "description" : {
          "type" : "string"
        },
        "images" : {
          "type" : "array",
          "items" : {
            "$ref" : "#/components/schemas/URLImage"
          },
          "minItems" : 1
        }
      }
    }*/
  public static final Schema URL_ENTITY_SCHEMA = SchemaBuilder.struct()
      .field(UrlEntity.SERIALIZED_NAME_START, Schema.INT32_SCHEMA)
      .field(UrlEntity.SERIALIZED_NAME_END, Schema.INT32_SCHEMA)
      .field(UrlEntity.SERIALIZED_NAME_URL, Schema.STRING_SCHEMA)
      .field(UrlEntity.SERIALIZED_NAME_EXPANDED_URL, Schema.OPTIONAL_STRING_SCHEMA)
      .field(UrlEntity.SERIALIZED_NAME_DISPLAY_URL, Schema.OPTIONAL_STRING_SCHEMA)
      .field(UrlEntity.SERIALIZED_NAME_UNWOUND_URL, Schema.OPTIONAL_STRING_SCHEMA)
      .field(UrlEntity.SERIALIZED_NAME_STATUS, Schema.OPTIONAL_INT32_SCHEMA)
      .field(UrlEntity.SERIALIZED_NAME_TITLE, Schema.OPTIONAL_STRING_SCHEMA)
      .field(UrlEntity.SERIALIZED_NAME_DESCRIPTION, Schema.OPTIONAL_STRING_SCHEMA)
      .field(UrlEntity.SERIALIZED_NAME_IMAGES, SchemaBuilder.array(URL_IMAGE_SCHEMA).optional().build())
      .build();

  public static Struct convert(@Nonnull UrlEntity input) {
    return new Struct(URL_ENTITY_SCHEMA)
        .put(UrlEntity.SERIALIZED_NAME_START, input.getStart())
        .put(UrlEntity.SERIALIZED_NAME_END, input.getEnd())
        .put(UrlEntity.SERIALIZED_NAME_URL, input.getUrl().toString())
        .put(UrlEntity.SERIALIZED_NAME_EXPANDED_URL,
            Optional.ofNullable(input.getExpandedUrl())
                .map(URL::toString)
                .orElse(null))
        .put(UrlEntity.SERIALIZED_NAME_DISPLAY_URL, input.getDisplayUrl())
        .put(UrlEntity.SERIALIZED_NAME_UNWOUND_URL,
            Optional.ofNullable(input.getUnwoundUrl())
                .map(URL::toString)
                .orElse(null))
        .put(UrlEntity.SERIALIZED_NAME_STATUS, input.getStatus())
        .put(UrlEntity.SERIALIZED_NAME_TITLE, input.getTitle())
        .put(UrlEntity.SERIALIZED_NAME_DESCRIPTION, input.getDescription())
        .put(UrlEntity.SERIALIZED_NAME_IMAGES,
            Optional.ofNullable(input.getImages())
                .map(list -> list.stream().map(TweetConverter::convert).collect(Collectors.toList()))
                .orElse(null));
  }

  /*{
      "type" : "object",
      "required" : [ "start", "end", "tag" ],
      "properties" : {
        "start" : {
          "type" : "integer",
          "minimum" : 0
        },
        "end" : {
          "type" : "integer",
          "minimum" : 0
        },
        "tag" : {
          "type" : "string"
        }
      }
    }*/
  public static final Schema HASHTAG_ENTITY_SCHEMA = SchemaBuilder.struct()
      .field(HashtagEntity.SERIALIZED_NAME_START, Schema.INT32_SCHEMA)
      .field(HashtagEntity.SERIALIZED_NAME_END, Schema.INT32_SCHEMA)
      .field(HashtagEntity.SERIALIZED_NAME_TAG, Schema.STRING_SCHEMA)
      .build();

  public static Struct convert(@Nonnull HashtagEntity input) {
    return new Struct(HASHTAG_ENTITY_SCHEMA)
        .put(HashtagEntity.SERIALIZED_NAME_START, input.getStart())
        .put(HashtagEntity.SERIALIZED_NAME_END, input.getEnd())
        .put(HashtagEntity.SERIALIZED_NAME_TAG, input.getTag());
  }

  /*{
      "type" : "object",
      "required" : [ "start", "end", "username", "id" ],
      "properties" : {
        "start" : {
          "type" : "integer",
          "minimum" : 0
        },
        "end" : {
          "type" : "integer",
          "minimum" : 0
        },
        "username" : {
          "type" : "string",
          "pattern" : "^[A-Za-z0-9_]{1,15}$"
        },
        "id" : {
          "type" : "string",
          "pattern" : "^[0-9]{1,19}$"
        }
      }
    }*/
  public static final Schema MENTION_ENTITY_SCHEMA = SchemaBuilder.struct()
      .field(MentionEntity.SERIALIZED_NAME_START, Schema.INT32_SCHEMA)
      .field(MentionEntity.SERIALIZED_NAME_END, Schema.INT32_SCHEMA)
      .field(MentionEntity.SERIALIZED_NAME_USERNAME, Schema.STRING_SCHEMA)
      .field(MentionEntity.SERIALIZED_NAME_ID, Schema.STRING_SCHEMA)
      .build();

  public static Struct convert(@Nonnull MentionEntity input) {
    return new Struct(MENTION_ENTITY_SCHEMA)
        .put(MentionEntity.SERIALIZED_NAME_START, input.getStart())
        .put(MentionEntity.SERIALIZED_NAME_END, input.getEnd())
        .put(MentionEntity.SERIALIZED_NAME_USERNAME, input.getUsername())
        .put(MentionEntity.SERIALIZED_NAME_ID, input.getId());
  }

  /*{
      "type" : "object",
      "required" : [ "start", "end", "tag" ],
      "properties" : {
        "start" : {
          "type" : "integer",
          "minimum" : 0
        },
        "end" : {
          "type" : "integer",
          "minimum" : 0
        },
        "tag" : {
          "type" : "string"
        }
      }
    }*/
  public static final Schema CASHTAG_ENTITY_SCHEMA = SchemaBuilder.struct()
      .field(CashtagEntity.SERIALIZED_NAME_START, Schema.INT32_SCHEMA)
      .field(CashtagEntity.SERIALIZED_NAME_END, Schema.INT32_SCHEMA)
      .field(CashtagEntity.SERIALIZED_NAME_TAG, Schema.STRING_SCHEMA)
      .build();

  public static Struct convert(@Nonnull CashtagEntity input) {
    return new Struct(CASHTAG_ENTITY_SCHEMA)
        .put(CashtagEntity.SERIALIZED_NAME_START, input.getStart())
        .put(CashtagEntity.SERIALIZED_NAME_END, input.getEnd())
        .put(CashtagEntity.SERIALIZED_NAME_TAG, input.getTag());
  }

  /*{
      "type" : "object",
      "properties" : {
        "urls" : {
          "type" : "array",
          "items" : {
            "$ref" : "#/components/schemas/UrlEntity"
          },
          "minItems" : 1
        },
        "hashtags" : {
          "type" : "array",
          "items" : {
            "$ref" : "#/components/schemas/HashtagEntity"
          },
          "minItems" : 1
        },
        "mentions" : {
          "type" : "array",
          "items" : {
            "$ref" : "#/components/schemas/MentionEntity"
          },
          "minItems" : 1
        },
        "cashtags" : {
          "type" : "array",
          "items" : {
            "$ref" : "#/components/schemas/CashtagEntity"
          },
          "minItems" : 1
        }
      }
    }*/
  public static final Schema FULL_TEXT_ENTITIES_SCHEMA = SchemaBuilder.struct()
      .optional()
      .field(FullTextEntities.SERIALIZED_NAME_URLS, SchemaBuilder.array(URL_ENTITY_SCHEMA).optional().build())
      .field(FullTextEntities.SERIALIZED_NAME_HASHTAGS, SchemaBuilder.array(HASHTAG_ENTITY_SCHEMA).optional().build())
      .field(FullTextEntities.SERIALIZED_NAME_MENTIONS, SchemaBuilder.array(MENTION_ENTITY_SCHEMA).optional().build())
      .field(FullTextEntities.SERIALIZED_NAME_CASHTAGS, SchemaBuilder.array(CASHTAG_ENTITY_SCHEMA).optional().build())
      .build();

  public static Struct convert(@Nonnull FullTextEntities input) {
    return new Struct(FULL_TEXT_ENTITIES_SCHEMA)
        .put(FullTextEntities.SERIALIZED_NAME_URLS,
            Optional.ofNullable(input.getUrls())
                .map(list -> list.stream().map(TweetConverter::convert).collect(Collectors.toList()))
                .orElse(null))
        .put(FullTextEntities.SERIALIZED_NAME_HASHTAGS,
            Optional.ofNullable(input.getHashtags())
                .map(list -> list.stream().map(TweetConverter::convert).collect(Collectors.toList()))
                .orElse(null))
        .put(FullTextEntities.SERIALIZED_NAME_MENTIONS,
            Optional.ofNullable(input.getMentions())
                .map(list -> list.stream().map(TweetConverter::convert).collect(Collectors.toList()))
                .orElse(null))
        .put(FullTextEntities.SERIALIZED_NAME_CASHTAGS,
            Optional.ofNullable(input.getCashtags())
                .map(list -> list.stream().map(TweetConverter::convert).collect(Collectors.toList()))
                .orElse(null));
  }

  /*{
      "type" : "object",
      "required" : [ "retweet_count", "reply_count", "like_count" ],
      "properties" : {
        "retweet_count" : {
          "type" : "integer"
        },
        "reply_count" : {
          "type" : "integer"
        },
        "like_count" : {
          "type" : "integer"
        },
        "quote_count" : {
          "type" : "integer"
        }
      }
    }*/
  public static final Schema TWEET_PUBLIC_METRICS_SCHEMA = SchemaBuilder.struct()
      .optional()
      .field(TweetPublicMetrics.SERIALIZED_NAME_RETWEET_COUNT, Schema.INT32_SCHEMA)
      .field(TweetPublicMetrics.SERIALIZED_NAME_REPLY_COUNT, Schema.INT32_SCHEMA)
      .field(TweetPublicMetrics.SERIALIZED_NAME_LIKE_COUNT, Schema.INT32_SCHEMA)
      .field(TweetPublicMetrics.SERIALIZED_NAME_QUOTE_COUNT, Schema.OPTIONAL_INT32_SCHEMA)
      .build();

  public static Struct convert(@Nonnull TweetPublicMetrics input) {
    return new Struct(TWEET_PUBLIC_METRICS_SCHEMA)
        .put(TweetPublicMetrics.SERIALIZED_NAME_RETWEET_COUNT, input.getRetweetCount())
        .put(TweetPublicMetrics.SERIALIZED_NAME_REPLY_COUNT, input.getReplyCount())
        .put(TweetPublicMetrics.SERIALIZED_NAME_LIKE_COUNT, input.getLikeCount())
        .put(TweetPublicMetrics.SERIALIZED_NAME_QUOTE_COUNT, input.getQuoteCount());
  }

  /*{
      "type" : "object",
      "properties" : {
        "impression_count" : {
          "type" : "integer",
          "format" : "int32"
        }
      }
    }*/
  public static final Schema TWEET_NON_PUBLIC_METRICS_SCHEMA = SchemaBuilder.struct()
      .optional()
      .field(TweetNonPublicMetrics.SERIALIZED_NAME_IMPRESSION_COUNT, Schema.OPTIONAL_INT32_SCHEMA)
      .build();

  public static Struct convert(@Nonnull TweetNonPublicMetrics input) {
    return new Struct(TWEET_NON_PUBLIC_METRICS_SCHEMA)
        .put(TweetNonPublicMetrics.SERIALIZED_NAME_IMPRESSION_COUNT, input.getImpressionCount());
  }

  /*{
      "type" : "object",
      "properties" : {
        "impression_count" : {
          "type" : "integer",
          "format" : "int32"
        },
        "like_count" : {
          "type" : "integer",
          "format" : "int32"
        },
        "reply_count" : {
          "type" : "integer",
          "format" : "int32"
        },
        "retweet_count" : {
          "type" : "integer",
          "format" : "int32"
        }
      }
    }*/
  public static final Schema TWEET_PROMOTED_METRICS_SCHEMA = SchemaBuilder.struct()
      .optional()
      .field(TweetPromotedMetrics.SERIALIZED_NAME_IMPRESSION_COUNT, Schema.OPTIONAL_INT32_SCHEMA)
      .field(TweetPromotedMetrics.SERIALIZED_NAME_LIKE_COUNT, Schema.OPTIONAL_INT32_SCHEMA)
      .field(TweetPromotedMetrics.SERIALIZED_NAME_REPLY_COUNT, Schema.OPTIONAL_INT32_SCHEMA)
      .field(TweetPromotedMetrics.SERIALIZED_NAME_RETWEET_COUNT, Schema.OPTIONAL_INT32_SCHEMA)
      .build();

  public static Struct convert(@Nonnull TweetPromotedMetrics input) {
    return new Struct(TWEET_PROMOTED_METRICS_SCHEMA)
        .put(TweetPromotedMetrics.SERIALIZED_NAME_IMPRESSION_COUNT, input.getImpressionCount())
        .put(TweetPromotedMetrics.SERIALIZED_NAME_LIKE_COUNT, input.getLikeCount())
        .put(TweetPromotedMetrics.SERIALIZED_NAME_REPLY_COUNT, input.getReplyCount())
        .put(TweetPromotedMetrics.SERIALIZED_NAME_RETWEET_COUNT, input.getRetweetCount());
  }

  /*{
      "type" : "object",
      "required" : [ "impression_count", "retweet_count", "reply_count", "like_count", "user_profile_clicks" ],
      "properties" : {
        "impression_count" : {
          "type" : "integer"
        },
        "retweet_count" : {
          "type" : "integer"
        },
        "reply_count" : {
          "type" : "integer"
        },
        "like_count" : {
          "type" : "integer"
        }
      }
    }*/
  public static final Schema TWEET_ORGANIC_METRICS_SCHEMA = SchemaBuilder.struct()
      .optional()
      .field(TweetOrganicMetrics.SERIALIZED_NAME_IMPRESSION_COUNT, Schema.INT32_SCHEMA)
      .field(TweetOrganicMetrics.SERIALIZED_NAME_RETWEET_COUNT, Schema.INT32_SCHEMA)
      .field(TweetOrganicMetrics.SERIALIZED_NAME_REPLY_COUNT, Schema.INT32_SCHEMA)
      .field(TweetOrganicMetrics.SERIALIZED_NAME_LIKE_COUNT, Schema.INT32_SCHEMA)
      .build();

  public static Struct convert(@Nonnull TweetOrganicMetrics input) {
    return new Struct(TWEET_ORGANIC_METRICS_SCHEMA)
        .put(TweetOrganicMetrics.SERIALIZED_NAME_IMPRESSION_COUNT, input.getImpressionCount())
        .put(TweetOrganicMetrics.SERIALIZED_NAME_RETWEET_COUNT, input.getRetweetCount())
        .put(TweetOrganicMetrics.SERIALIZED_NAME_REPLY_COUNT, input.getReplyCount())
        .put(TweetOrganicMetrics.SERIALIZED_NAME_LIKE_COUNT, input.getLikeCount());
  }

  /*{
      "type" : "object",
      "required" : [
        "is_edit_eligible",
        "editable_until",
        "edits_remaining"
      ],
      "properties" : {
        "editable_until" : {
          "type" : "string",
          "format" : "date-time"
        },
        "edits_remaining" : {
          "type" : "integer"
        },
        "is_edit_eligible" : {
          "type" : "boolean"
        }
      }
    }*/
  public static final Schema TWEET_EDIT_CONTROLS_SCHEMA = SchemaBuilder.struct()
      .optional()
      .field(TweetEditControls.SERIALIZED_NAME_EDITABLE_UNTIL, Timestamp.SCHEMA)
      .field(TweetEditControls.SERIALIZED_NAME_EDITS_REMAINING, Schema.INT32_SCHEMA)
      .field(TweetEditControls.SERIALIZED_NAME_IS_EDIT_ELIGIBLE, Schema.BOOLEAN_SCHEMA)
      .build();

  public static Struct convert(@Nonnull TweetEditControls input) {
    return new Struct(TWEET_EDIT_CONTROLS_SCHEMA)
        .put(TweetEditControls.SERIALIZED_NAME_EDITABLE_UNTIL, Date.from(input.getEditableUntil().toInstant()))
        .put(TweetEditControls.SERIALIZED_NAME_EDITS_REMAINING, input.getEditsRemaining())
        .put(TweetEditControls.SERIALIZED_NAME_IS_EDIT_ELIGIBLE, input.getIsEditEligible());
  }

  /*{
      "type" : "object",
      "required" : [ "id", "text", "edit_history_tweet_ids" ],
      "properties" : {
        "id" : {
          "type" : "string",
          "pattern" : "^[0-9]{1,19}$"
        },
        "created_at" : {
          "type" : "string",
          "format" : "date-time"
        },
        "text" : {
          "type" : "string"
        },
        "author_id" : {
          "type" : "string",
          "pattern" : "^[0-9]{1,19}$"
        },
        "in_reply_to_user_id" : {
          "type" : "string",
          "pattern" : "^[0-9]{1,19}$"
        },
        "referenced_tweets" : {
          "type" : "array",
          "items" : {
            "$ref" : "#/components/schemas/TweetReferencedTweetsItem"
          },
          "minItems" : 1
        },
        "attachments" : {
          "$ref" : "#/components/schemas/TweetAttachments"
        },
        "context_annotations" : {
          "type" : "array",
          "items" : {
            "$ref" : "#/components/schemas/ContextAnnotation"
          },
          "minItems" : 1
        },
        "withheld" : {
          "$ref" : "#/components/schemas/TweetWithheld"
        },
        "geo" : {
          "$ref" : "#/components/schemas/TweetGeo"
        },
        "entities" : {
          "$ref" : "#/components/schemas/FullTextEntities"
        },
        "public_metrics" : {
          "$ref" : "#/components/schemas/TweetPublicMetrics"
        },
        "possibly_sensitive" : {
          "type" : "boolean"
        },
        "lang" : {
          "type" : "string"
        },
        "source" : {
          "type" : "string"
        },
        "non_public_metrics" : {
          "$ref" : "#/components/schemas/TweetNonPublicMetrics"
        },
        "promoted_metrics" : {
          "$ref" : "#/components/schemas/TweetPromotedMetrics"
        },
        "organic_metrics" : {
          "$ref" : "#/components/schemas/TweetOrganicMetrics"
        },
        "conversation_id" : {
          "type" : "string",
          "pattern" : "^[0-9]{1,19}$"
        },
        "edit_controls" : {
          "$ref" : "#/components/schemas/TweetEditControls"
        },
        "edit_history_tweet_ids" : {
          "type" : "array",
          "minItems" : 1,
          "items" : {
            "type" : "string",
            "pattern" : "^[0-9]{1,19}$"
          }
        },
        "reply_settings" : {
          "type" : "string",
          "pattern" : "^[A-Za-z]{1,12}$",
          "enum" : [
            "everyone",
            "mentionedUsers",
            "following",
            "other"
          ]
        }
      }
    }*/
  public static final Schema TWEET_SCHEMA = SchemaBuilder.struct()
      .field(Tweet.SERIALIZED_NAME_ID, Schema.STRING_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_CREATED_AT, Timestamp.builder().optional().build())
      .field(Tweet.SERIALIZED_NAME_TEXT, Schema.STRING_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_AUTHOR_ID, Schema.OPTIONAL_STRING_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_IN_REPLY_TO_USER_ID, Schema.OPTIONAL_STRING_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_REFERENCED_TWEETS, SchemaBuilder.array(TWEET_REFERENCED_TWEETS_ITEM_SCHEMA).optional().build())
      .field(Tweet.SERIALIZED_NAME_ATTACHMENTS, TWEET_ATTACHMENTS_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_CONTEXT_ANNOTATIONS, SchemaBuilder.array(CONTEXT_ANNOTATION_SCHEMA).optional().build())
      .field(Tweet.SERIALIZED_NAME_WITHHELD, TWEET_WITHHELD_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_GEO, TWEET_GEO_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_ENTITIES, FULL_TEXT_ENTITIES_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_PUBLIC_METRICS, TWEET_PUBLIC_METRICS_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_POSSIBLY_SENSITIVE, Schema.OPTIONAL_BOOLEAN_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_LANG, Schema.OPTIONAL_STRING_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_SOURCE, Schema.OPTIONAL_STRING_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_NON_PUBLIC_METRICS, TWEET_NON_PUBLIC_METRICS_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_PROMOTED_METRICS, TWEET_PROMOTED_METRICS_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_ORGANIC_METRICS, TWEET_ORGANIC_METRICS_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_CONVERSATION_ID, Schema.OPTIONAL_STRING_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_EDIT_CONTROLS, TWEET_EDIT_CONTROLS_SCHEMA)
      .field(Tweet.SERIALIZED_NAME_EDIT_HISTORY_TWEET_IDS, SchemaBuilder.array(Schema.STRING_SCHEMA))
      .field(Tweet.SERIALIZED_NAME_REPLY_SETTINGS, Schema.OPTIONAL_STRING_SCHEMA);

  public static Struct convert(@Nonnull Tweet input) {
    return new Struct(TWEET_SCHEMA)
        .put(Tweet.SERIALIZED_NAME_ID, input.getId())
        .put(Tweet.SERIALIZED_NAME_CREATED_AT,
            Optional.ofNullable(input.getCreatedAt())
                .map(offset -> Date.from(offset.toInstant()))
                .orElse(null))
        .put(Tweet.SERIALIZED_NAME_TEXT, input.getText())
        .put(Tweet.SERIALIZED_NAME_AUTHOR_ID, input.getAuthorId())
        .put(Tweet.SERIALIZED_NAME_IN_REPLY_TO_USER_ID, input.getInReplyToUserId())
        .put(Tweet.SERIALIZED_NAME_REFERENCED_TWEETS,
            Optional.ofNullable(input.getReferencedTweets())
                .map(list -> list.stream().map(TweetConverter::convert).collect(Collectors.toList()))
                .orElse(null))
        .put(Tweet.SERIALIZED_NAME_ATTACHMENTS,
            Optional.ofNullable(input.getAttachments())
                .map(TweetConverter::convert)
                .orElse(null))
        .put(Tweet.SERIALIZED_NAME_CONTEXT_ANNOTATIONS,
            Optional.ofNullable(input.getContextAnnotations())
                .map(list -> list.stream().map(TweetConverter::convert).collect(Collectors.toList()))
                .orElse(null))
        .put(Tweet.SERIALIZED_NAME_WITHHELD,
            Optional.ofNullable(input.getWithheld())
                .map(TweetConverter::convert)
                .orElse(null))
        .put(Tweet.SERIALIZED_NAME_GEO,
            Optional.ofNullable(input.getGeo())
                .map(TweetConverter::convert)
                .orElse(null))
        .put(Tweet.SERIALIZED_NAME_ENTITIES,
            Optional.ofNullable(input.getEntities())
                .map(TweetConverter::convert)
                .orElse(null))
        .put(Tweet.SERIALIZED_NAME_PUBLIC_METRICS,
            Optional.ofNullable(input.getPublicMetrics())
                .map(TweetConverter::convert)
                .orElse(null))
        .put(Tweet.SERIALIZED_NAME_POSSIBLY_SENSITIVE, input.getPossiblySensitive())
        .put(Tweet.SERIALIZED_NAME_LANG, input.getLang())
        .put(Tweet.SERIALIZED_NAME_SOURCE, input.getSource())
        .put(Tweet.SERIALIZED_NAME_NON_PUBLIC_METRICS,
            Optional.ofNullable(input.getNonPublicMetrics())
                .map(TweetConverter::convert)
                .orElse(null))
        .put(Tweet.SERIALIZED_NAME_PROMOTED_METRICS,
            Optional.ofNullable(input.getPromotedMetrics())
                .map(TweetConverter::convert)
                .orElse(null))
        .put(Tweet.SERIALIZED_NAME_ORGANIC_METRICS,
            Optional.ofNullable(input.getOrganicMetrics())
                .map(TweetConverter::convert)
                .orElse(null))
        .put(Tweet.SERIALIZED_NAME_CONVERSATION_ID, input.getConversationId())
        .put(Tweet.SERIALIZED_NAME_EDIT_CONTROLS,
            Optional.ofNullable(input.getEditControls())
                .map(TweetConverter::convert)
                .orElse(null))
        .put(Tweet.SERIALIZED_NAME_EDIT_HISTORY_TWEET_IDS, input.getEditHistoryTweetIds())
        .put(Tweet.SERIALIZED_NAME_REPLY_SETTINGS,
            Optional.ofNullable(input.getReplySettings())
                .map(ReplySettings::getValue)
                .orElse(null));
  }

}
