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

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.data.Timestamp;
import twitter4j.ExtendedMediaEntity;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Place;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.SymbolEntity;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatusConverter {


  public final static Schema PLACE_SCHEMA;
  public final static Schema GEO_LOCATION_SCHEMA;
  public static final Schema SCHEMA_STATUS_DELETION_NOTICE;
  public static final Schema SCHEMA_STATUS_DELETION_NOTICE_KEY;
  public static final Schema STATUS_SCHEMA_KEY;
  public static final Schema STATUS_SCHEMA;

  public static final Schema USER_SCHEMA = SchemaBuilder.struct()
      .name("com.github.jcustenborder.kafka.connect.twitter.User")
      .doc("Return the user associated with the status. This can be null if the instance is from User.getStatus().")
      .field("Id", SchemaBuilder.int64().doc("Returns the id of the user").optional().build())
      .field("Name", SchemaBuilder.string().doc("Returns the name of the user").optional().build())
      .field("ScreenName", SchemaBuilder.string().doc("Returns the screen name of the user").optional().build())
      .field("Location", SchemaBuilder.string().doc("Returns the location of the user").optional().build())
      .field("Description", SchemaBuilder.string().doc("Returns the description of the user").optional().build())
      .field("ContributorsEnabled", SchemaBuilder.bool().doc("Tests if the user is enabling contributors").optional().build())
      .field("ProfileImageURL", SchemaBuilder.string().doc("Returns the profile image url of the user").optional().build())
      .field("BiggerProfileImageURL", SchemaBuilder.string().optional().build())
      .field("MiniProfileImageURL", SchemaBuilder.string().optional().build())
      .field("OriginalProfileImageURL", SchemaBuilder.string().optional().build())
      .field("ProfileImageURLHttps", SchemaBuilder.string().optional().build())
      .field("BiggerProfileImageURLHttps", SchemaBuilder.string().optional().build())
      .field("MiniProfileImageURLHttps", SchemaBuilder.string().optional().build())
      .field("OriginalProfileImageURLHttps", SchemaBuilder.string().optional().build())
      .field("DefaultProfileImage", SchemaBuilder.bool().doc("Tests if the user has not uploaded their own avatar").optional().build())
      .field("URL", SchemaBuilder.string().doc("Returns the url of the user").optional().build())
      .field("Protected", SchemaBuilder.bool().doc("Test if the user status is protected").optional().build())
      .field("FollowersCount", SchemaBuilder.int32().doc("Returns the number of followers").optional().build())
      .field("ProfileBackgroundColor", SchemaBuilder.string().optional().build())
      .field("ProfileTextColor", SchemaBuilder.string().optional().build())
      .field("ProfileLinkColor", SchemaBuilder.string().optional().build())
      .field("ProfileSidebarFillColor", SchemaBuilder.string().optional().build())
      .field("ProfileSidebarBorderColor", SchemaBuilder.string().optional().build())
      .field("ProfileUseBackgroundImage", SchemaBuilder.bool().optional().build())
      .field("DefaultProfile", SchemaBuilder.bool().doc("Tests if the user has not altered the theme or background").optional().build())
      .field("ShowAllInlineMedia", SchemaBuilder.bool().optional().build())
      .field("FriendsCount", SchemaBuilder.int32().doc("Returns the number of users the user follows (AKA \"followings\")").optional().build())
      .field("CreatedAt", Timestamp.builder().optional().build())
      .field("FavouritesCount", SchemaBuilder.int32().optional().build())
      .field("UtcOffset", SchemaBuilder.int32().optional().build())
      .field("TimeZone", SchemaBuilder.string().optional().build())
      .field("ProfileBackgroundImageURL", SchemaBuilder.string().optional().build())
      .field("ProfileBackgroundImageUrlHttps", SchemaBuilder.string().optional().build())
      .field("ProfileBannerURL", SchemaBuilder.string().optional().build())
      .field("ProfileBannerRetinaURL", SchemaBuilder.string().optional().build())
      .field("ProfileBannerIPadURL", SchemaBuilder.string().optional().build())
      .field("ProfileBannerIPadRetinaURL", SchemaBuilder.string().optional().build())
      .field("ProfileBannerMobileURL", SchemaBuilder.string().optional().build())
      .field("ProfileBannerMobileRetinaURL", SchemaBuilder.string().optional().build())
      .field("ProfileBackgroundTiled", SchemaBuilder.bool().optional().build())
      .field("Lang", SchemaBuilder.string().doc("Returns the preferred language of the user").optional().build())
      .field("StatusesCount", SchemaBuilder.int32().optional().build())
      .field("GeoEnabled", SchemaBuilder.bool().optional().build())
      .field("Verified", SchemaBuilder.bool().optional().build())
      .field("Translator", SchemaBuilder.bool().optional().build())
      .field("ListedCount", SchemaBuilder.int32().doc("Returns the number of public lists the user is listed on, or -1 if the count is unavailable.").optional().build())
      .field("FollowRequestSent", SchemaBuilder.bool().doc("Returns true if the authenticating user has requested to follow this user, otherwise false.").optional().build())
      .field("WithheldInCountries", SchemaBuilder.array(Schema.STRING_SCHEMA).doc("Returns the list of country codes where the user is withheld").build())
      .build();

  static {
    PLACE_SCHEMA = SchemaBuilder.struct()
        .name("com.github.jcustenborder.kafka.connect.twitter.Place")
        .optional()
        .doc("Returns the place attached to this status")
        .field("Name", SchemaBuilder.string().optional().build())
        .field("StreetAddress", SchemaBuilder.string().optional().build())
        .field("CountryCode", SchemaBuilder.string().optional().build())
        .field("Id", SchemaBuilder.string().optional().build())
        .field("Country", SchemaBuilder.string().optional().build())
        .field("PlaceType", SchemaBuilder.string().optional().build())
        .field("URL", SchemaBuilder.string().optional().build())
        .field("FullName", SchemaBuilder.string().optional().build())
        .build();
  }

  static {
    GEO_LOCATION_SCHEMA = SchemaBuilder.struct()
        .name("com.github.jcustenborder.kafka.connect.twitter.GeoLocation")
        .optional()
        .doc("Returns The location that this tweet refers to if available.")
        .field("Latitude", SchemaBuilder.float64().doc("returns the latitude of the geo location").build())
        .field("Longitude", SchemaBuilder.float64().doc("returns the longitude of the geo location").build())
        .build();
  }

  static {
    STATUS_SCHEMA_KEY = SchemaBuilder.struct()
        .name("com.github.jcustenborder.kafka.connect.twitter.StatusKey")
        .doc("Key for a twitter status.")
        .field("Id", Schema.OPTIONAL_INT64_SCHEMA)
        .build();
  }

  public static final Schema SCHEMA_MEDIA_ENTITY_VARIANT = SchemaBuilder.struct()
      .name("com.github.jcustenborder.kafka.connect.twitter.ExtendedMediaEntity.Variant")
      .doc("")
      .field("Url", SchemaBuilder.string().optional().doc("").build())
      .field("Bitrate", SchemaBuilder.int32().optional().doc("").build())
      .field("ContentType", SchemaBuilder.string().optional().doc("").build())
      .build();
  public static final Schema SCHEMA_MEDIA_ENTITY_SIZE = SchemaBuilder.struct()
      .name("com.github.jcustenborder.kafka.connect.twitter.MediaEntity.Size")
      .doc("")
      .field("Resize", SchemaBuilder.int32().optional().doc("").build())
      .field("Width", SchemaBuilder.int32().optional().doc("").build())
      .field("Height", SchemaBuilder.int32().optional().doc("").build())
      .build();
  public static final Schema SCHEMA_EXTENDED_MEDIA_ENTITY = SchemaBuilder.struct()
      .name("com.github.jcustenborder.kafka.connect.twitter.ExtendedMediaEntity")
      .doc("")
      .field("VideoAspectRatioWidth", SchemaBuilder.int32().optional().doc("").build())
      .field("VideoAspectRatioHeight", SchemaBuilder.int32().optional().doc("").build())
      .field("VideoDurationMillis", SchemaBuilder.int64().optional().doc("").build())
      .field("VideoVariants", SchemaBuilder.array(SCHEMA_MEDIA_ENTITY_VARIANT).optional().doc("").build())
      .field("ExtAltText", SchemaBuilder.string().optional().doc("").build())
      .field("Id", SchemaBuilder.int64().optional().doc("Returns the id of the media.").build())
      .field("Type", SchemaBuilder.string().optional().doc("Returns the media type photo, video, animated_gif.").build())
      .field("MediaURL", SchemaBuilder.string().optional().doc("Returns the media URL.").build())
      .field("Sizes", SchemaBuilder.map(Schema.INT32_SCHEMA, SCHEMA_MEDIA_ENTITY_SIZE).doc("Returns size variations of the media.").build())
      .field("MediaURLHttps", SchemaBuilder.string().optional().doc("Returns the media secure URL.").build())
      .field("URL", SchemaBuilder.string().optional().doc("Returns the URL mentioned in the tweet.").build())
      .field("Text", SchemaBuilder.string().optional().doc("Returns the URL mentioned in the tweet.").build())
      .field("ExpandedURL", SchemaBuilder.string().optional().doc("Returns the expanded URL if mentioned URL is shorten.").build())
      .field("Start", SchemaBuilder.int32().optional().doc("Returns the index of the start character of the URL mentioned in the tweet.").build())
      .field("End", SchemaBuilder.int32().optional().doc("Returns the index of the end character of the URL mentioned in the tweet.").build())
      .field("DisplayURL", SchemaBuilder.string().optional().doc("Returns the display URL if mentioned URL is shorten.").build())
      .build();
  public static final Schema SCHEMA_HASHTAG_ENTITY = SchemaBuilder.struct()
      .name("com.github.jcustenborder.kafka.connect.twitter.HashtagEntity")
      .doc("")
      .field("Text", SchemaBuilder.string().optional().doc("Returns the text of the hashtag without #.").build())
      .field("Start", SchemaBuilder.int32().optional().doc("Returns the index of the start character of the hashtag.").build())
      .field("End", SchemaBuilder.int32().optional().doc("Returns the index of the end character of the hashtag.").build())
      .build();
  public static final Schema SCHEMA_MEDIA_ENTITY = SchemaBuilder.struct()
      .name("com.github.jcustenborder.kafka.connect.twitter.MediaEntity")
      .doc("")
      .field("Id", SchemaBuilder.int64().optional().doc("Returns the id of the media.").build())
      .field("Type", SchemaBuilder.string().optional().doc("Returns the media type photo, video, animated_gif.").build())
      .field("MediaURL", SchemaBuilder.string().optional().doc("Returns the media URL.").build())
      .field("Sizes", SchemaBuilder.map(Schema.INT32_SCHEMA, SCHEMA_MEDIA_ENTITY_SIZE))
      .field("MediaURLHttps", SchemaBuilder.string().optional().doc("Returns the media secure URL.").build())
      .field("VideoAspectRatioWidth", SchemaBuilder.int32().optional().doc("").build())
      .field("VideoAspectRatioHeight", SchemaBuilder.int32().optional().doc("").build())
      .field("VideoDurationMillis", SchemaBuilder.int64().optional().doc("").build())
      .field("VideoVariants", SchemaBuilder.array(SCHEMA_MEDIA_ENTITY_VARIANT).optional().doc("Returns size variations of the media.").build())
      .field("ExtAltText", SchemaBuilder.string().optional().doc("").build())
      .field("URL", SchemaBuilder.string().optional().doc("Returns the URL mentioned in the tweet.").build())
      .field("Text", SchemaBuilder.string().optional().doc("Returns the URL mentioned in the tweet.").build())
      .field("ExpandedURL", SchemaBuilder.string().optional().doc("Returns the expanded URL if mentioned URL is shorten.").build())
      .field("Start", SchemaBuilder.int32().optional().doc("Returns the index of the start character of the URL mentioned in the tweet.").build())
      .field("End", SchemaBuilder.int32().optional().doc("Returns the index of the end character of the URL mentioned in the tweet.").build())
      .field("DisplayURL", SchemaBuilder.string().optional().doc("Returns the display URL if mentioned URL is shorten.").build())
      .build();
  public static final Schema SCHEMA_SYMBOL_ENTITY = SchemaBuilder.struct()
      .name("com.github.jcustenborder.kafka.connect.twitter.SymbolEntity")
      .doc("")
      .field("Start", SchemaBuilder.int32().optional().doc("Returns the index of the start character of the symbol.").build())
      .field("End", SchemaBuilder.int32().optional().doc("Returns the index of the end character of the symbol.").build())
      .field("Text", SchemaBuilder.string().optional().doc("Returns the text of the entity").build())
      .build();
  public static final Schema SCHEMA_URL_ENTITY = SchemaBuilder.struct()
      .name("com.github.jcustenborder.kafka.connect.twitter.URLEntity")
      .doc("")
      .field("URL", SchemaBuilder.string().optional().doc("Returns the URL mentioned in the tweet.").build())
      .field("Text", SchemaBuilder.string().optional().doc("Returns the URL mentioned in the tweet.").build())
      .field("ExpandedURL", SchemaBuilder.string().optional().doc("Returns the expanded URL if mentioned URL is shorten.").build())
      .field("Start", SchemaBuilder.int32().optional().doc("Returns the index of the start character of the URL mentioned in the tweet.").build())
      .field("End", SchemaBuilder.int32().optional().doc("Returns the index of the end character of the URL mentioned in the tweet.").build())
      .field("DisplayURL", SchemaBuilder.string().optional().doc("Returns the display URL if mentioned URL is shorten.").build())
      .build();
  public static final Schema SCHEMA_USER_MENTION_ENTITY = SchemaBuilder.struct()
      .name("com.github.jcustenborder.kafka.connect.twitter.UserMentionEntity")
      .doc("")
      .field("Name", SchemaBuilder.string().optional().doc("Returns the name mentioned in the status.").build())
      .field("Id", SchemaBuilder.int64().optional().doc("Returns the user id mentioned in the status.").build())
      .field("Text", SchemaBuilder.string().optional().doc("Returns the screen name mentioned in the status.").build())
      .field("ScreenName", SchemaBuilder.string().optional().doc("Returns the screen name mentioned in the status.").build())
      .field("Start", SchemaBuilder.int32().optional().doc("Returns the index of the start character of the user mention.").build())
      .field("End", SchemaBuilder.int32().optional().doc("Returns the index of the end character of the user mention.").build())
      .build();

  static {
    STATUS_SCHEMA = SchemaBuilder.struct()
        .name("com.github.jcustenborder.kafka.connect.twitter.Status")
        .doc("Twitter status message.")
        .field("CreatedAt", Timestamp.builder().doc("Return the created_at").optional().build())
        .field("Id", SchemaBuilder.int64().doc("Returns the id of the status").optional().build())
        .field("Text", SchemaBuilder.string().doc("Returns the text of the status").optional().build())
        .field("Source", SchemaBuilder.string().doc("Returns the source").optional().build())
        .field("Truncated", SchemaBuilder.bool().doc("Test if the status is truncated").optional().build())
        .field("InReplyToStatusId", SchemaBuilder.int64().doc("Returns the in_reply_tostatus_id").optional().build())
        .field("InReplyToUserId", SchemaBuilder.int64().doc("Returns the in_reply_user_id").optional().build())
        .field("InReplyToScreenName", SchemaBuilder.string().doc("Returns the in_reply_to_screen_name").optional().build())
        .field("GeoLocation", GEO_LOCATION_SCHEMA)
        .field("Place", PLACE_SCHEMA)
        .field("Favorited", SchemaBuilder.bool().doc("Test if the status is favorited").optional().build())
        .field("Retweeted", SchemaBuilder.bool().doc("Test if the status is retweeted").optional().build())
        .field("FavoriteCount", SchemaBuilder.int32().doc("Indicates approximately how many times this Tweet has been \"favorited\" by Twitter users.").optional().build())
        .field("User", USER_SCHEMA)
        .field("Retweet", SchemaBuilder.bool().optional().build())
        .field("Contributors", SchemaBuilder.array(Schema.INT64_SCHEMA).doc("Returns an array of contributors, or null if no contributor is associated with this status.").build())
        .field("RetweetCount", SchemaBuilder.int32().doc("Returns the number of times this tweet has been retweeted, or -1 when the tweet was created before this feature was enabled.").optional().build())
        .field("RetweetedByMe", SchemaBuilder.bool().optional().build())
        .field("CurrentUserRetweetId", SchemaBuilder.int64().doc("Returns the authenticating user's retweet's id of this tweet, or -1L when the tweet was created before this feature was enabled.").optional().build())
        .field("PossiblySensitive", SchemaBuilder.bool().optional().build())
        .field("Lang", SchemaBuilder.string().doc("Returns the lang of the status text if available.").optional().build())
        .field("WithheldInCountries", SchemaBuilder.array(Schema.STRING_SCHEMA).doc("Returns the list of country codes where the tweet is withheld").build())
        .field("HashtagEntities", SchemaBuilder.array(SCHEMA_HASHTAG_ENTITY).doc("Returns an array if hashtag mentioned in the tweet.").optional().build())
        .field("UserMentionEntities", SchemaBuilder.array(SCHEMA_USER_MENTION_ENTITY).doc("Returns an array of user mentions in the tweet.").optional().build())
        .field("MediaEntities", SchemaBuilder.array(SCHEMA_MEDIA_ENTITY).doc("Returns an array of MediaEntities if medias are available in the tweet.").optional().build())
        .field("SymbolEntities", SchemaBuilder.array(SCHEMA_SYMBOL_ENTITY).doc("Returns an array of SymbolEntities if medias are available in the tweet.").optional().build())
        .field("URLEntities", SchemaBuilder.array(SCHEMA_URL_ENTITY).doc("Returns an array if URLEntity mentioned in the tweet.").optional().build())

        .build();
  }

  static {
    SCHEMA_STATUS_DELETION_NOTICE = SchemaBuilder.struct()
        .name("com.github.jcustenborder.kafka.connect.twitter.StatusDeletionNotice")
        .doc("Message that is received when a status is deleted from Twitter.")
        .field("StatusId", Schema.INT64_SCHEMA)
        .field("UserId", Schema.INT64_SCHEMA)
        .build();
  }

  static {
    SCHEMA_STATUS_DELETION_NOTICE_KEY = SchemaBuilder.struct()
        .name("com.github.jcustenborder.kafka.connect.twitter.StatusDeletionNoticeKey")
        .doc("Key for a message that is received when a status is deleted from Twitter.")
        .field("StatusId", Schema.INT64_SCHEMA)
        .build();
  }

  static Map<Integer, Struct> convertSizes(Map<Integer, MediaEntity.Size> items) {
    Map<Integer, Struct> results = new LinkedHashMap<>();

    if (items == null) {
      return results;
    }

    for (Map.Entry<Integer, MediaEntity.Size> kvp : items.entrySet()) {
      results.put(kvp.getKey(), convertMediaEntitySize(kvp.getValue()));
    }

    return results;
  }

  public static void convert(User user, Struct struct) {
    struct
        .put("Id", user.getId())
        .put("Name", user.getName())
        .put("ScreenName", user.getScreenName())
        .put("Location", user.getLocation())
        .put("Description", user.getDescription())
        .put("ContributorsEnabled", user.isContributorsEnabled())
        .put("ProfileImageURL", user.getProfileImageURL())
        .put("BiggerProfileImageURL", user.getBiggerProfileImageURL())
        .put("MiniProfileImageURL", user.getMiniProfileImageURL())
        .put("OriginalProfileImageURL", user.getOriginalProfileImageURL())
        .put("ProfileImageURLHttps", user.getProfileImageURLHttps())
        .put("BiggerProfileImageURLHttps", user.getBiggerProfileImageURLHttps())
        .put("MiniProfileImageURLHttps", user.getMiniProfileImageURLHttps())
        .put("OriginalProfileImageURLHttps", user.getOriginalProfileImageURLHttps())
        .put("DefaultProfileImage", user.isDefaultProfileImage())
        .put("URL", user.getURL())
        .put("Protected", user.isProtected())
        .put("FollowersCount", user.getFollowersCount())
        .put("ProfileBackgroundColor", user.getProfileBackgroundColor())
        .put("ProfileTextColor", user.getProfileTextColor())
        .put("ProfileLinkColor", user.getProfileLinkColor())
        .put("ProfileSidebarFillColor", user.getProfileSidebarFillColor())
        .put("ProfileSidebarBorderColor", user.getProfileSidebarBorderColor())
        .put("ProfileUseBackgroundImage", user.isProfileUseBackgroundImage())
        .put("DefaultProfile", user.isDefaultProfile())
        .put("ShowAllInlineMedia", user.isShowAllInlineMedia())
        .put("FriendsCount", user.getFriendsCount())
        .put("CreatedAt", user.getCreatedAt())
        .put("FavouritesCount", user.getFavouritesCount())
        .put("UtcOffset", user.getUtcOffset())
        .put("TimeZone", user.getTimeZone())
        .put("ProfileBackgroundImageURL", user.getProfileBackgroundImageURL())
        .put("ProfileBackgroundImageUrlHttps", user.getProfileBackgroundImageUrlHttps())
        .put("ProfileBannerURL", user.getProfileBannerURL())
        .put("ProfileBannerRetinaURL", user.getProfileBannerRetinaURL())
        .put("ProfileBannerIPadURL", user.getProfileBannerIPadURL())
        .put("ProfileBannerIPadRetinaURL", user.getProfileBannerIPadRetinaURL())
        .put("ProfileBannerMobileURL", user.getProfileBannerMobileURL())
        .put("ProfileBannerMobileRetinaURL", user.getProfileBannerMobileRetinaURL())
        .put("ProfileBackgroundTiled", user.isProfileBackgroundTiled())
        .put("Lang", user.getLang())
        .put("StatusesCount", user.getStatusesCount())
        .put("GeoEnabled", user.isGeoEnabled())
        .put("Verified", user.isVerified())
        .put("Translator", user.isTranslator())
        .put("ListedCount", user.getListedCount())
        .put("FollowRequestSent", user.isFollowRequestSent());

    List<String> withheldInCountries = new ArrayList<>();
    if (null != user.getWithheldInCountries()) {
      for (String s : user.getWithheldInCountries()) {
        withheldInCountries.add(s);
      }
    }
    struct.put("WithheldInCountries", withheldInCountries);

  }

  public static void convert(Place place, Struct struct) {
    if (null == place) {
      return;
    }
    struct.put("Name", place.getName())
        .put("StreetAddress", place.getStreetAddress())
        .put("CountryCode", place.getCountryCode())
        .put("Id", place.getId())
        .put("Country", place.getCountry())
        .put("PlaceType", place.getPlaceType())
        .put("URL", place.getURL())
        .put("FullName", place.getFullName());
  }

  public static void convert(GeoLocation geoLocation, Struct struct) {
    if (null == geoLocation) {
      return;
    }
    struct.put("Latitude", geoLocation.getLatitude())
        .put("Longitude", geoLocation.getLongitude());
  }


  static Struct convertMediaEntityVariant(MediaEntity.Variant variant) {
    return new Struct(SCHEMA_MEDIA_ENTITY_VARIANT)
        .put("Url", variant.getUrl())
        .put("Bitrate", variant.getBitrate())
        .put("ContentType", variant.getContentType());
  }

  public static List<Struct> convert(MediaEntity.Variant[] items) {
    List<Struct> result = new ArrayList<>();
    if (null == items) {
      return result;
    }
    for (MediaEntity.Variant item : items) {
      Struct struct = convertMediaEntityVariant(item);
      result.add(struct);
    }
    return result;
  }


  static Struct convertMediaEntitySize(MediaEntity.Size size) {
    return new Struct(SCHEMA_MEDIA_ENTITY_SIZE)
        .put("Resize", size.getResize())
        .put("Width", size.getWidth())
        .put("Height", size.getHeight());
  }

  public static List<Struct> convert(MediaEntity.Size[] items) {
    List<Struct> result = new ArrayList<>();
    if (null == items) {
      return result;
    }
    for (MediaEntity.Size item : items) {
      Struct struct = convertMediaEntitySize(item);
      result.add(struct);
    }
    return result;
  }


  static Struct convertExtendedMediaEntity(ExtendedMediaEntity extendedMediaEntity) {
    return new Struct(SCHEMA_EXTENDED_MEDIA_ENTITY)
        .put("VideoAspectRatioWidth", extendedMediaEntity.getVideoAspectRatioWidth())
        .put("VideoAspectRatioHeight", extendedMediaEntity.getVideoAspectRatioHeight())
        .put("VideoDurationMillis", extendedMediaEntity.getVideoDurationMillis())
        .put("VideoVariants", extendedMediaEntity.getVideoVariants())
        .put("ExtAltText", extendedMediaEntity.getExtAltText())
        .put("Id", extendedMediaEntity.getId())
        .put("Type", extendedMediaEntity.getType())
        .put("MediaURL", extendedMediaEntity.getMediaURL())
        .put("Sizes", extendedMediaEntity.getSizes())
        .put("MediaURLHttps", extendedMediaEntity.getMediaURLHttps())
        .put("URL", extendedMediaEntity.getURL())
        .put("Text", extendedMediaEntity.getText())
        .put("ExpandedURL", extendedMediaEntity.getExpandedURL())
        .put("Start", extendedMediaEntity.getStart())
        .put("End", extendedMediaEntity.getEnd())
        .put("DisplayURL", extendedMediaEntity.getDisplayURL());
  }

  public static List<Struct> convert(ExtendedMediaEntity[] items) {
    List<Struct> result = new ArrayList<>();
    if (null == items) {
      return result;
    }
    for (ExtendedMediaEntity item : items) {
      Struct struct = convertExtendedMediaEntity(item);
      result.add(struct);
    }
    return result;
  }


  static Struct convertHashtagEntity(HashtagEntity hashtagEntity) {
    return new Struct(SCHEMA_HASHTAG_ENTITY)
        .put("Text", hashtagEntity.getText())
        .put("Start", hashtagEntity.getStart())
        .put("End", hashtagEntity.getEnd());
  }

  public static List<Struct> convert(HashtagEntity[] items) {
    List<Struct> result = new ArrayList<>();
    if (null == items) {
      return result;
    }
    for (HashtagEntity item : items) {
      Struct struct = convertHashtagEntity(item);
      result.add(struct);
    }
    return result;
  }


  static Struct convertMediaEntity(MediaEntity mediaEntity) {
    return new Struct(SCHEMA_MEDIA_ENTITY)
        .put("Id", mediaEntity.getId())
        .put("Type", mediaEntity.getType())
        .put("MediaURL", mediaEntity.getMediaURL())
        .put("Sizes", convertSizes(mediaEntity.getSizes()))
        .put("MediaURLHttps", mediaEntity.getMediaURLHttps())
        .put("VideoAspectRatioWidth", mediaEntity.getVideoAspectRatioWidth())
        .put("VideoAspectRatioHeight", mediaEntity.getVideoAspectRatioHeight())
        .put("VideoDurationMillis", mediaEntity.getVideoDurationMillis())
        .put("VideoVariants", convert(mediaEntity.getVideoVariants()))
        .put("ExtAltText", mediaEntity.getExtAltText())
        .put("URL", mediaEntity.getURL())
        .put("Text", mediaEntity.getText())
        .put("ExpandedURL", mediaEntity.getExpandedURL())
        .put("Start", mediaEntity.getStart())
        .put("End", mediaEntity.getEnd())
        .put("DisplayURL", mediaEntity.getDisplayURL());
  }

  public static List<Struct> convert(MediaEntity[] items) {
    List<Struct> result = new ArrayList<>();
    if (null == items) {
      return result;
    }
    for (MediaEntity item : items) {
      Struct struct = convertMediaEntity(item);
      result.add(struct);
    }
    return result;
  }


  static Struct convertSymbolEntity(SymbolEntity symbolEntity) {
    return new Struct(SCHEMA_SYMBOL_ENTITY)
        .put("Start", symbolEntity.getStart())
        .put("End", symbolEntity.getEnd())
        .put("Text", symbolEntity.getText());
  }

  public static List<Struct> convert(SymbolEntity[] items) {
    List<Struct> result = new ArrayList<>();
    if (null == items) {
      return result;
    }
    for (SymbolEntity item : items) {
      Struct struct = convertSymbolEntity(item);
      result.add(struct);
    }
    return result;
  }


  static Struct convertURLEntity(URLEntity uRLEntity) {
    return new Struct(SCHEMA_URL_ENTITY)
        .put("URL", uRLEntity.getURL())
        .put("Text", uRLEntity.getText())
        .put("ExpandedURL", uRLEntity.getExpandedURL())
        .put("Start", uRLEntity.getStart())
        .put("End", uRLEntity.getEnd())
        .put("DisplayURL", uRLEntity.getDisplayURL());
  }

  public static List<Struct> convert(URLEntity[] items) {
    List<Struct> result = new ArrayList<>();
    if (null == items) {
      return result;
    }
    for (URLEntity item : items) {
      Struct struct = convertURLEntity(item);
      result.add(struct);
    }
    return result;
  }


  static Struct convertUserMentionEntity(UserMentionEntity userMentionEntity) {
    return new Struct(SCHEMA_USER_MENTION_ENTITY)
        .put("Name", userMentionEntity.getName())
        .put("Id", userMentionEntity.getId())
        .put("Text", userMentionEntity.getText())
        .put("ScreenName", userMentionEntity.getScreenName())
        .put("Start", userMentionEntity.getStart())
        .put("End", userMentionEntity.getEnd());
  }

  public static List<Struct> convert(UserMentionEntity[] items) {
    List<Struct> result = new ArrayList<>();
    if (null == items) {
      return result;
    }
    for (UserMentionEntity item : items) {
      Struct struct = convertUserMentionEntity(item);
      result.add(struct);
    }
    return result;
  }


  public static void convertKey(Status status, Struct struct) {
    struct.put("Id", status.getId());
  }

  public static void convert(Status status, Struct struct) {
    struct
        .put("CreatedAt", status.getCreatedAt())
        .put("Id", status.getId())
        .put("Text", status.getText())
        .put("Source", status.getSource())
        .put("Truncated", status.isTruncated())
        .put("InReplyToStatusId", status.getInReplyToStatusId())
        .put("InReplyToUserId", status.getInReplyToUserId())
        .put("InReplyToScreenName", status.getInReplyToScreenName())
        .put("Favorited", status.isFavorited())
        .put("Retweeted", status.isRetweeted())
        .put("FavoriteCount", status.getFavoriteCount())
        .put("Retweet", status.isRetweet())
        .put("RetweetCount", status.getRetweetCount())
        .put("RetweetedByMe", status.isRetweetedByMe())
        .put("CurrentUserRetweetId", status.getCurrentUserRetweetId())
        .put("PossiblySensitive", status.isPossiblySensitive())
        .put("Lang", status.getLang());

    Struct userStruct;
    if (null != status.getUser()) {
      userStruct = new Struct(USER_SCHEMA);
      convert(status.getUser(), userStruct);
    } else {
      userStruct = null;
    }
    struct.put("User", userStruct);

    Struct placeStruct;
    if (null != status.getPlace()) {
      placeStruct = new Struct(PLACE_SCHEMA);
      convert(status.getPlace(), placeStruct);
    } else {
      placeStruct = null;
    }
    struct.put("Place", placeStruct);

    Struct geoLocationStruct;
    if (null != status.getGeoLocation()) {
      geoLocationStruct = new Struct(GEO_LOCATION_SCHEMA);
      convert(status.getGeoLocation(), geoLocationStruct);
    } else {
      geoLocationStruct = null;
    }
    struct.put("GeoLocation", geoLocationStruct);
    List<Long> contributers = new ArrayList<>();

    if (null != status.getContributors()) {
      for (Long l : status.getContributors()) {
        contributers.add(l);
      }
    }
    struct.put("Contributors", contributers);

    List<String> withheldInCountries = new ArrayList<>();
    if (null != status.getWithheldInCountries()) {
      for (String s : status.getWithheldInCountries()) {
        withheldInCountries.add(s);
      }
    }
    struct.put("WithheldInCountries", withheldInCountries);

    struct.put("HashtagEntities", convert(status.getHashtagEntities()));
    struct.put("UserMentionEntities", convert(status.getUserMentionEntities()));
    struct.put("MediaEntities", convert(status.getMediaEntities()));
    struct.put("SymbolEntities", convert(status.getSymbolEntities()));
    struct.put("URLEntities", convert(status.getURLEntities()));
  }

  public static void convert(StatusDeletionNotice statusDeletionNotice, Struct struct) {
    struct.put("StatusId", statusDeletionNotice.getStatusId());
    struct.put("UserId", statusDeletionNotice.getUserId());
  }

  public static void convertKey(StatusDeletionNotice statusDeletionNotice, Struct struct) {
    struct.put("StatusId", statusDeletionNotice.getStatusId());
  }
}
