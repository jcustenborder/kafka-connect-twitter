package io.confluent.kafka.connect.twitter;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.data.Timestamp;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;

import java.util.ArrayList;
import java.util.List;

public class StatusConverter {

  public static final Schema userSchema;

  static {
    userSchema = SchemaBuilder.struct()
        .name("io.confluent.examples.kafka.connect.twitter.User")
        .doc("Return the user associated with the status.\n" +
            "This can be null if the instance is from User.getStatus().")
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


  public final static Schema placeSchema;

  static {
    placeSchema = SchemaBuilder.struct()
        .name("io.confluent.examples.kafka.connect.twitter.Place")
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

  public final static Schema geoLocationSchema;

  static {
    geoLocationSchema = SchemaBuilder.struct()
        .name("io.confluent.examples.kafka.connect.twitter.GeoLocation")
        .optional()
        .doc("Returns The location that this tweet refers to if available.")
        .field("Latitude", Schema.FLOAT64_SCHEMA)
        .field("Longitude", Schema.FLOAT64_SCHEMA)
        .build();
  }

  public static void convert(GeoLocation geoLocation, Struct struct) {
    if (null == geoLocation) {
      return;
    }
    struct.put("Latitude", geoLocation.getLatitude())
        .put("Longitude", geoLocation.getLongitude());
  }

  static final Schema statusSchemaKey;

  static {
    statusSchemaKey = SchemaBuilder.struct()
        .name("io.confluent.examples.kafka.connect.twitter.StatusKey")
        .doc("Key for a twitter status.")
        .field("Id", Schema.OPTIONAL_INT64_SCHEMA)
        .build();
  }

  public static void convertKey(Status status, Struct struct) {
    struct.put("Id", status.getId());
  }


  static final Schema statusSchema;

  static {
    statusSchema = SchemaBuilder.struct()
        .name("io.confluent.examples.kafka.connect.twitter.Status")
        .field("CreatedAt", Timestamp.builder().doc("Return the created_at").optional().build())
        .field("Id", SchemaBuilder.int64().doc("Returns the id of the status").optional().build())
        .field("Text", SchemaBuilder.string().doc("Returns the text of the status").optional().build())
        .field("Source", SchemaBuilder.string().doc("Returns the source").optional().build())
        .field("Truncated", SchemaBuilder.bool().doc("Test if the status is truncated").optional().build())
        .field("InReplyToStatusId", SchemaBuilder.int64().doc("Returns the in_reply_tostatus_id").optional().build())
        .field("InReplyToUserId", SchemaBuilder.int64().doc("Returns the in_reply_user_id").optional().build())
        .field("InReplyToScreenName", SchemaBuilder.string().doc("Returns the in_reply_to_screen_name").optional().build())
        .field("GeoLocation", geoLocationSchema)
        .field("Place", placeSchema)
        .field("Favorited", SchemaBuilder.bool().doc("Test if the status is favorited").optional().build())
        .field("Retweeted", SchemaBuilder.bool().doc("Test if the status is retweeted").optional().build())
        .field("FavoriteCount", SchemaBuilder.int32().doc("Indicates approximately how many times this Tweet has been \"favorited\" by Twitter users.").optional().build())
        .field("User", userSchema)
        .field("Retweet", SchemaBuilder.bool().optional().build())
        .field("Contributors", SchemaBuilder.array(Schema.INT64_SCHEMA).doc("Returns an array of contributors, or null if no contributor is associated with this status.").build())
        .field("RetweetCount", SchemaBuilder.int32().doc("Returns the number of times this tweet has been retweeted, or -1 when the tweet was created before this feature was enabled.").optional().build())
        .field("RetweetedByMe", SchemaBuilder.bool().optional().build())
        .field("CurrentUserRetweetId", SchemaBuilder.int64().doc("Returns the authenticating user's retweet's id of this tweet, or -1L when the tweet was created before this feature was enabled.").optional().build())
        .field("PossiblySensitive", SchemaBuilder.bool().optional().build())
        .field("Lang", SchemaBuilder.string().doc("Returns the lang of the status text if available.").optional().build())
        .field("WithheldInCountries", SchemaBuilder.array(Schema.STRING_SCHEMA).doc("Returns the list of country codes where the tweet is withheld").build())
        .build();
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
      userStruct = new Struct(userSchema);
      convert(status.getUser(), userStruct);
    } else {
      userStruct = null;
    }
    struct.put("User", userStruct);

    Struct placeStruct;
    if (null != status.getPlace()) {
      placeStruct = new Struct(placeSchema);
      convert(status.getPlace(), placeStruct);
    } else {
      placeStruct = null;
    }
    struct.put("Place", placeStruct);

    Struct geoLocationStruct;
    if (null != status.getGeoLocation()) {
      geoLocationStruct = new Struct(geoLocationSchema);
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
  }

  public static final Schema schemaStatusDeletionNotice;

  static {
    schemaStatusDeletionNotice = SchemaBuilder.struct()
        .name("io.confluent.examples.kafka.connect.twitter.StatusDeletionNotice")
        .field("StatusId", Schema.INT64_SCHEMA)
        .field("UserId", Schema.INT64_SCHEMA)
        .build();
  }

  public static void convert(StatusDeletionNotice statusDeletionNotice, Struct struct) {
    struct.put("StatusId", statusDeletionNotice.getStatusId());
    struct.put("UserId", statusDeletionNotice.getUserId());
  }

  public static final Schema schemaStatusDeletionNoticeKey;

  static {
    schemaStatusDeletionNoticeKey = SchemaBuilder.struct()
        .name("io.confluent.examples.kafka.connect.twitter.StatusDeletionNoticeKey")
        .field("StatusId", Schema.INT64_SCHEMA)
        .build();
  }

  public static void convertKey(StatusDeletionNotice statusDeletionNotice, Struct struct) {
    struct.put("StatusId", statusDeletionNotice.getStatusId());
  }
}
