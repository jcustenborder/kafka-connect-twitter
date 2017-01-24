/**
 * Copyright © 2016 Jeremy Custenborder (jcustenborder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jcustenborder.kafka.connect.twitter;

import org.apache.kafka.connect.data.Struct;
import org.junit.jupiter.api.Test;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatusConverterTest {

  public static GeoLocation mockGeoLocation() {
    return new GeoLocation(30.2672D, 97.7431D);
  }

  public static Place mockPlace() {
    Place place = mock(Place.class);
    when(place.getName()).thenReturn("Example place");
    when(place.getStreetAddress()).thenReturn("123 Example St");
    when(place.getCountryCode()).thenReturn("US");
    when(place.getId()).thenReturn("asdfaisdfasd");
    when(place.getCountry()).thenReturn("United States");
    when(place.getPlaceType()).thenReturn("ADF");
    when(place.getURL()).thenReturn("http://www.example.com/");
    when(place.getFullName()).thenReturn("Example place");
    return place;
  }

  public static Status mockStatus() {
    Status status = mock(Status.class);
    User user = mockUser();
    GeoLocation geoLocation = mockGeoLocation();
    Place place = mockPlace();

    when(status.getCreatedAt()).thenReturn(new Date(1471667709998L));
    when(status.getId()).thenReturn(9823452L);
    when(status.getText()).thenReturn("This is a twit");
    when(status.getSource()).thenReturn("foo");
    when(status.isTruncated()).thenReturn(false);
    when(status.getInReplyToStatusId()).thenReturn(2345234L);
    when(status.getInReplyToUserId()).thenReturn(8756786L);
    when(status.getInReplyToScreenName()).thenReturn("foo");
    when(status.getGeoLocation()).thenReturn(geoLocation);
    when(status.getPlace()).thenReturn(place);
    when(status.isFavorited()).thenReturn(true);
    when(status.isRetweeted()).thenReturn(false);
    when(status.getFavoriteCount()).thenReturn(1234);
    when(status.getUser()).thenReturn(user);
    when(status.isRetweet()).thenReturn(false);
    when(status.getContributors()).thenReturn(new long[]{431234L, 986789678L});
    when(status.getRetweetCount()).thenReturn(1234);
    when(status.isRetweetedByMe()).thenReturn(false);
    when(status.getCurrentUserRetweetId()).thenReturn(653456345L);
    when(status.isPossiblySensitive()).thenReturn(false);
    when(status.getLang()).thenReturn("en-US");
    when(status.getWithheldInCountries()).thenReturn(new String[]{"CN"});

    return status;
  }

  public static User mockUser() {
    User user = mock(User.class);

    when(user.getId()).thenReturn(1234L);
    when(user.getName()).thenReturn("Example User");
    when(user.getScreenName()).thenReturn("example");
    when(user.getLocation()).thenReturn("Austin, TX");
    when(user.getDescription()).thenReturn("This is a description");
    when(user.isContributorsEnabled()).thenReturn(true);
    when(user.getProfileImageURL()).thenReturn("http://i.twittercdn.com/profile.jpg");
    when(user.getBiggerProfileImageURL()).thenReturn("http://i.twittercdn.com/biggerprofile.jpg");
    when(user.getMiniProfileImageURL()).thenReturn("http://i.twittercdn.com/mini.profile.jpg");
    when(user.getOriginalProfileImageURL()).thenReturn("http://i.twittercdn.com/original.profile.jpg");
    when(user.getProfileImageURLHttps()).thenReturn("https://i.twittercdn.com/profile.jpg");
    when(user.getBiggerProfileImageURLHttps()).thenReturn("https://i.twittercdn.com/bigger.profile.jpg");
    when(user.getMiniProfileImageURLHttps()).thenReturn("https://i.twittercdn.com/mini.profile.jpg");
    when(user.getOriginalProfileImageURLHttps()).thenReturn("https://i.twittercdn.com/original.profile.jpg");
    when(user.isDefaultProfileImage()).thenReturn(true);
    when(user.getURL()).thenReturn("https://www.twitter.com/example");
    when(user.isProtected()).thenReturn(false);
    when(user.getFollowersCount()).thenReturn(54245);
    when(user.getProfileBackgroundColor()).thenReturn("#ffffff");
    when(user.getProfileTextColor()).thenReturn("#000000");
    when(user.getProfileLinkColor()).thenReturn("#aaaaaa");
    when(user.getProfileSidebarFillColor()).thenReturn("#333333");
    when(user.getProfileSidebarBorderColor()).thenReturn("#555555");
    when(user.isProfileUseBackgroundImage()).thenReturn(true);
    when(user.isDefaultProfile()).thenReturn(true);
    when(user.isShowAllInlineMedia()).thenReturn(true);
    when(user.getFriendsCount()).thenReturn(452345234);
    when(user.getCreatedAt()).thenReturn(new Date(1471665653209L));
    when(user.getFavouritesCount()).thenReturn(12341);
    when(user.getUtcOffset()).thenReturn(8);
    when(user.getTimeZone()).thenReturn("UTC");
    when(user.getProfileBackgroundImageURL()).thenReturn("https://i.twittercdn.com/original.background.jpg");
    when(user.getProfileBackgroundImageUrlHttps()).thenReturn("https://i.twittercdn.com/original.background.jpg");
    when(user.getProfileBannerURL()).thenReturn("https://i.twittercdn.com/original.banner.jpg");
    when(user.getProfileBannerRetinaURL()).thenReturn("https://i.twittercdn.com/original.banner.jpg");
    when(user.getProfileBannerIPadURL()).thenReturn("https://i.twittercdn.com/original.banner.jpg");
    when(user.getProfileBannerIPadRetinaURL()).thenReturn("https://i.twittercdn.com/original.banner.jpg");
    when(user.getProfileBannerMobileURL()).thenReturn("https://i.twittercdn.com/original.banner.jpg");
    when(user.getProfileBannerMobileRetinaURL()).thenReturn("https://i.twittercdn.com/original.banner.jpg");
    when(user.isProfileBackgroundTiled()).thenReturn(false);
    when(user.getLang()).thenReturn("en-us");
    when(user.getStatusesCount()).thenReturn(543);
    when(user.isGeoEnabled()).thenReturn(true);
    when(user.isVerified()).thenReturn(true);
    when(user.isTranslator()).thenReturn(false);
    when(user.getListedCount()).thenReturn(4);
    when(user.isFollowRequestSent()).thenReturn(false);
    when(user.getWithheldInCountries()).thenReturn(new String[]{"CN"});


    return user;
  }

  public static StatusDeletionNotice mockStatusDeletionNotice() {
    StatusDeletionNotice statusDeletionNotice = mock(StatusDeletionNotice.class);
    when(statusDeletionNotice.getStatusId()).thenReturn(1234565345L);
    when(statusDeletionNotice.getUserId()).thenReturn(6543456354L);
    return statusDeletionNotice;
  }

  List<Long> convert(long[] values) {
    List<Long> list = new ArrayList<>();
    for (Long l : values) {
      list.add(l);
    }
    return list;
  }

  List<String> convert(String[] values) {
    List<String> list = new ArrayList<>();
    for (String l : values) {
      list.add(l);
    }
    return list;
  }

  void assertStatus(Status status, Struct struct) {
    assertEquals(status.getCreatedAt(), struct.get("CreatedAt"), "CreatedAt does not match.");
    assertEquals(status.getId(), struct.get("Id"), "Id does not match.");
    assertEquals(status.getText(), struct.get("Text"), "Text does not match.");
    assertEquals(status.getSource(), struct.get("Source"), "Source does not match.");
    assertEquals(status.isTruncated(), struct.get("Truncated"), "Truncated does not match.");
    assertEquals(status.getInReplyToStatusId(), struct.get("InReplyToStatusId"), "InReplyToStatusId does not match.");
    assertEquals(status.getInReplyToUserId(), struct.get("InReplyToUserId"), "InReplyToUserId does not match.");
    assertEquals(status.getInReplyToScreenName(), struct.get("InReplyToScreenName"), "InReplyToScreenName does not match.");
    assertEquals(status.isFavorited(), struct.get("Favorited"), "Favorited does not match.");
    assertEquals(status.isRetweeted(), struct.get("Retweeted"), "Retweeted does not match.");
    assertEquals(status.getFavoriteCount(), struct.get("FavoriteCount"), "FavoriteCount does not match.");
    assertEquals(status.isRetweet(), struct.get("Retweet"), "Retweet does not match.");
    assertEquals(status.getRetweetCount(), struct.get("RetweetCount"), "RetweetCount does not match.");
    assertEquals(status.isRetweetedByMe(), struct.get("RetweetedByMe"), "RetweetedByMe does not match.");
    assertEquals(status.getCurrentUserRetweetId(), struct.get("CurrentUserRetweetId"), "CurrentUserRetweetId does not match.");
    assertEquals(status.isPossiblySensitive(), struct.get("PossiblySensitive"), "PossiblySensitive does not match.");
    assertEquals(status.getLang(), struct.get("Lang"), "Lang does not match.");

    assertUser(status.getUser(), struct.getStruct("User"));
    assertPlace(status.getPlace(), struct.getStruct("Place"));
    assertGeoLocation(status.getGeoLocation(), struct.getStruct("GeoLocation"));

    assertEquals(convert(status.getContributors()), struct.getArray("Contributors"), "Contributors does not match.");
    assertEquals(convert(status.getWithheldInCountries()), struct.get("WithheldInCountries"), "WithheldInCountries does not match.");
  }

  void assertGeoLocation(GeoLocation geoLocation, Struct struct) {
    assertEquals(struct.getFloat64("Latitude"), 1, geoLocation.getLatitude());
    assertEquals(struct.getFloat64("Longitude"), 1, geoLocation.getLongitude());
  }

  void assertPlace(Place place, Struct struct) {
    assertEquals(place.getName(), struct.get("Name"), "Name does not match.");
    assertEquals(place.getStreetAddress(), struct.get("StreetAddress"), "StreetAddress does not match.");
    assertEquals(place.getCountryCode(), struct.get("CountryCode"), "CountryCode does not match.");
    assertEquals(place.getId(), struct.get("Id"), "Id does not match.");
    assertEquals(place.getCountry(), struct.get("Country"), "Country does not match.");
    assertEquals(place.getPlaceType(), struct.get("PlaceType"), "PlaceType does not match.");
    assertEquals(place.getURL(), struct.get("URL"), "URL does not match.");
    assertEquals(place.getFullName(), struct.get("FullName"), "FullName does not match.");
  }

  void assertUser(User user, Struct struct) {
    assertNotNull(struct, "struct should not be null.");
    assertEquals(user.getId(), struct.get("Id"), "Id does not match.");
    assertEquals(user.getName(), struct.get("Name"), "Name does not match.");
    assertEquals(user.getScreenName(), struct.get("ScreenName"), "ScreenName does not match.");
    assertEquals(user.getLocation(), struct.get("Location"), "Location does not match.");
    assertEquals(user.getDescription(), struct.get("Description"), "Description does not match.");
    assertEquals(user.isContributorsEnabled(), struct.get("ContributorsEnabled"), "ContributorsEnabled does not match.");
    assertEquals(user.getProfileImageURL(), struct.get("ProfileImageURL"), "ProfileImageURL does not match.");
    assertEquals(user.getBiggerProfileImageURL(), struct.get("BiggerProfileImageURL"), "BiggerProfileImageURL does not match.");
    assertEquals(user.getMiniProfileImageURL(), struct.get("MiniProfileImageURL"), "MiniProfileImageURL does not match.");
    assertEquals(user.getOriginalProfileImageURL(), struct.get("OriginalProfileImageURL"), "OriginalProfileImageURL does not match.");
    assertEquals(user.getProfileImageURLHttps(), struct.get("ProfileImageURLHttps"), "ProfileImageURLHttps does not match.");
    assertEquals(user.getBiggerProfileImageURLHttps(), struct.get("BiggerProfileImageURLHttps"), "BiggerProfileImageURLHttps does not match.");
    assertEquals(user.getMiniProfileImageURLHttps(), struct.get("MiniProfileImageURLHttps"), "MiniProfileImageURLHttps does not match.");
    assertEquals(user.getOriginalProfileImageURLHttps(), struct.get("OriginalProfileImageURLHttps"), "OriginalProfileImageURLHttps does not match.");
    assertEquals(user.isDefaultProfileImage(), struct.get("DefaultProfileImage"), "DefaultProfileImage does not match.");
    assertEquals(user.getURL(), struct.get("URL"), "URL does not match.");
    assertEquals(user.isProtected(), struct.get("Protected"), "Protected does not match.");
    assertEquals(user.getFollowersCount(), struct.get("FollowersCount"), "FollowersCount does not match.");
    assertEquals(user.getProfileBackgroundColor(), struct.get("ProfileBackgroundColor"), "ProfileBackgroundColor does not match.");
    assertEquals(user.getProfileTextColor(), struct.get("ProfileTextColor"), "ProfileTextColor does not match.");
    assertEquals(user.getProfileLinkColor(), struct.get("ProfileLinkColor"), "ProfileLinkColor does not match.");
    assertEquals(user.getProfileSidebarFillColor(), struct.get("ProfileSidebarFillColor"), "ProfileSidebarFillColor does not match.");
    assertEquals(user.getProfileSidebarBorderColor(), struct.get("ProfileSidebarBorderColor"), "ProfileSidebarBorderColor does not match.");
    assertEquals(user.isProfileUseBackgroundImage(), struct.get("ProfileUseBackgroundImage"), "ProfileUseBackgroundImage does not match.");
    assertEquals(user.isDefaultProfile(), struct.get("DefaultProfile"), "DefaultProfile does not match.");
    assertEquals(user.isShowAllInlineMedia(), struct.get("ShowAllInlineMedia"), "ShowAllInlineMedia does not match.");
    assertEquals(user.getFriendsCount(), struct.get("FriendsCount"), "FriendsCount does not match.");
    assertEquals(user.getCreatedAt(), struct.get("CreatedAt"), "CreatedAt does not match.");
    assertEquals(user.getFavouritesCount(), struct.get("FavouritesCount"), "FavouritesCount does not match.");
    assertEquals(user.getUtcOffset(), struct.get("UtcOffset"), "UtcOffset does not match.");
    assertEquals(user.getTimeZone(), struct.get("TimeZone"), "TimeZone does not match.");
    assertEquals(user.getProfileBackgroundImageURL(), struct.get("ProfileBackgroundImageURL"), "ProfileBackgroundImageURL does not match.");
    assertEquals(user.getProfileBackgroundImageUrlHttps(), struct.get("ProfileBackgroundImageUrlHttps"), "ProfileBackgroundImageUrlHttps does not match.");
    assertEquals(user.getProfileBannerURL(), struct.get("ProfileBannerURL"), "ProfileBannerURL does not match.");
    assertEquals(user.getProfileBannerRetinaURL(), struct.get("ProfileBannerRetinaURL"), "ProfileBannerRetinaURL does not match.");
    assertEquals(user.getProfileBannerIPadURL(), struct.get("ProfileBannerIPadURL"), "ProfileBannerIPadURL does not match.");
    assertEquals(user.getProfileBannerIPadRetinaURL(), struct.get("ProfileBannerIPadRetinaURL"), "ProfileBannerIPadRetinaURL does not match.");
    assertEquals(user.getProfileBannerMobileURL(), struct.get("ProfileBannerMobileURL"), "ProfileBannerMobileURL does not match.");
    assertEquals(user.getProfileBannerMobileRetinaURL(), struct.get("ProfileBannerMobileRetinaURL"), "ProfileBannerMobileRetinaURL does not match.");
    assertEquals(user.isProfileBackgroundTiled(), struct.get("ProfileBackgroundTiled"), "ProfileBackgroundTiled does not match.");
    assertEquals(user.getLang(), struct.get("Lang"), "Lang does not match.");
    assertEquals(user.getStatusesCount(), struct.get("StatusesCount"), "StatusesCount does not match.");
    assertEquals(user.isGeoEnabled(), struct.get("GeoEnabled"), "GeoEnabled does not match.");
    assertEquals(user.isVerified(), struct.get("Verified"), "Verified does not match.");
    assertEquals(user.isTranslator(), struct.get("Translator"), "Translator does not match.");
    assertEquals(user.getListedCount(), struct.get("ListedCount"), "ListedCount does not match.");
    assertEquals(user.isFollowRequestSent(), struct.get("FollowRequestSent"), "FollowRequestSent does not match.");
  }

  void assertKey(Status status, Struct struct) {
    assertEquals(status.getId(), struct.get("Id"), "Id does not match.");
  }

  @Test
  public void convertStatus() {
    Status status = mockStatus();
    Struct struct = new Struct(StatusConverter.STATUS_SCHEMA);
    StatusConverter.convert(status, struct);
    assertStatus(status, struct);
  }

  @Test
  public void convertUser() {
    User user = mockUser();
    Struct struct = new Struct(StatusConverter.USER_SCHEMA);
    StatusConverter.convert(user, struct);
    assertUser(user, struct);
  }

  @Test
  public void convertPlace() {
    Place place = mockPlace();
    Struct struct = new Struct(StatusConverter.PLACE_SCHEMA);
    StatusConverter.convert(place, struct);
    assertPlace(place, struct);
  }

  @Test
  public void convertGeoLocation() {
    GeoLocation geoLocation = mockGeoLocation();
    Struct struct = new Struct(StatusConverter.GEO_LOCATION_SCHEMA);
    StatusConverter.convert(geoLocation, struct);
    assertGeoLocation(geoLocation, struct);
  }

  @Test
  public void convertStatusKey() {
    Status status = mockStatus();
    Struct struct = new Struct(StatusConverter.STATUS_SCHEMA_KEY);
    StatusConverter.convertKey(status, struct);
    assertKey(status, struct);
  }

  void assertStatusDeletionNotice(StatusDeletionNotice statusDeletionNotice, Struct struct) {
    assertEquals(statusDeletionNotice.getStatusId(), struct.get("StatusId"), "StatusId does not match.");
    assertEquals(statusDeletionNotice.getUserId(), struct.get("UserId"), "UserId does not match.");
  }

  void assertStatusDeletionNoticeKey(StatusDeletionNotice statusDeletionNotice, Struct struct) {
    assertEquals(statusDeletionNotice.getStatusId(), struct.get("StatusId"), "StatusId does not match.");
  }

  @Test
  public void convertStatusDeletionNotice() {
    StatusDeletionNotice statusDeletionNotice = mockStatusDeletionNotice();
    Struct struct = new Struct(StatusConverter.SCHEMA_STATUS_DELETION_NOTICE);
    StatusConverter.convert(statusDeletionNotice, struct);
    assertStatusDeletionNotice(statusDeletionNotice, struct);
  }

  @Test
  public void convertKeyStatusDeletionNotice() {
    StatusDeletionNotice statusDeletionNotice = mockStatusDeletionNotice();
    Struct struct = new Struct(StatusConverter.SCHEMA_STATUS_DELETION_NOTICE_KEY);
    StatusConverter.convertKey(statusDeletionNotice, struct);
    assertStatusDeletionNoticeKey(statusDeletionNotice, struct);
  }
}
