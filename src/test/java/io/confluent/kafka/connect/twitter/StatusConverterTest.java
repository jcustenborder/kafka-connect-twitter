package io.confluent.kafka.connect.twitter;

import org.apache.kafka.connect.data.Struct;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    assertEquals("CreatedAt does not match.", status.getCreatedAt(), struct.get("CreatedAt"));
    assertEquals("Id does not match.", status.getId(), struct.get("Id"));
    assertEquals("Text does not match.", status.getText(), struct.get("Text"));
    assertEquals("Source does not match.", status.getSource(), struct.get("Source"));
    assertEquals("Truncated does not match.", status.isTruncated(), struct.get("Truncated"));
    assertEquals("InReplyToStatusId does not match.", status.getInReplyToStatusId(), struct.get("InReplyToStatusId"));
    assertEquals("InReplyToUserId does not match.", status.getInReplyToUserId(), struct.get("InReplyToUserId"));
    assertEquals("InReplyToScreenName does not match.", status.getInReplyToScreenName(), struct.get("InReplyToScreenName"));
    assertEquals("Favorited does not match.", status.isFavorited(), struct.get("Favorited"));
    assertEquals("Retweeted does not match.", status.isRetweeted(), struct.get("Retweeted"));
    assertEquals("FavoriteCount does not match.", status.getFavoriteCount(), struct.get("FavoriteCount"));
    assertEquals("Retweet does not match.", status.isRetweet(), struct.get("Retweet"));
    assertEquals("RetweetCount does not match.", status.getRetweetCount(), struct.get("RetweetCount"));
    assertEquals("RetweetedByMe does not match.", status.isRetweetedByMe(), struct.get("RetweetedByMe"));
    assertEquals("CurrentUserRetweetId does not match.", status.getCurrentUserRetweetId(), struct.get("CurrentUserRetweetId"));
    assertEquals("PossiblySensitive does not match.", status.isPossiblySensitive(), struct.get("PossiblySensitive"));
    assertEquals("Lang does not match.", status.getLang(), struct.get("Lang"));

    assertUser(status.getUser(), struct.getStruct("User"));
    assertPlace(status.getPlace(), struct.getStruct("Place"));
    assertGeoLocation(status.getGeoLocation(), struct.getStruct("GeoLocation"));

    assertEquals("Contributors does not match.", convert(status.getContributors()), struct.getArray("Contributors"));
    assertEquals("WithheldInCountries does not match.", convert(status.getWithheldInCountries()), struct.get("WithheldInCountries"));
  }

  void assertGeoLocation(GeoLocation geoLocation, Struct struct) {
    assertEquals(geoLocation.getLatitude(), struct.getFloat64("Latitude"), 1);
    assertEquals(geoLocation.getLongitude(), struct.getFloat64("Longitude"), 1);
  }

  void assertPlace(Place place, Struct struct) {
    assertEquals("Name does not match.", place.getName(), struct.get("Name"));
    assertEquals("StreetAddress does not match.", place.getStreetAddress(), struct.get("StreetAddress"));
    assertEquals("CountryCode does not match.", place.getCountryCode(), struct.get("CountryCode"));
    assertEquals("Id does not match.", place.getId(), struct.get("Id"));
    assertEquals("Country does not match.", place.getCountry(), struct.get("Country"));
    assertEquals("PlaceType does not match.", place.getPlaceType(), struct.get("PlaceType"));
    assertEquals("URL does not match.", place.getURL(), struct.get("URL"));
    assertEquals("FullName does not match.", place.getFullName(), struct.get("FullName"));
  }

  void assertUser(User user, Struct struct) {
    assertNotNull("struct should not be null.", struct);
    assertEquals("Id does not match.", user.getId(), struct.get("Id"));
    assertEquals("Name does not match.", user.getName(), struct.get("Name"));
    assertEquals("ScreenName does not match.", user.getScreenName(), struct.get("ScreenName"));
    assertEquals("Location does not match.", user.getLocation(), struct.get("Location"));
    assertEquals("Description does not match.", user.getDescription(), struct.get("Description"));
    assertEquals("ContributorsEnabled does not match.", user.isContributorsEnabled(), struct.get("ContributorsEnabled"));
    assertEquals("ProfileImageURL does not match.", user.getProfileImageURL(), struct.get("ProfileImageURL"));
    assertEquals("BiggerProfileImageURL does not match.", user.getBiggerProfileImageURL(), struct.get("BiggerProfileImageURL"));
    assertEquals("MiniProfileImageURL does not match.", user.getMiniProfileImageURL(), struct.get("MiniProfileImageURL"));
    assertEquals("OriginalProfileImageURL does not match.", user.getOriginalProfileImageURL(), struct.get("OriginalProfileImageURL"));
    assertEquals("ProfileImageURLHttps does not match.", user.getProfileImageURLHttps(), struct.get("ProfileImageURLHttps"));
    assertEquals("BiggerProfileImageURLHttps does not match.", user.getBiggerProfileImageURLHttps(), struct.get("BiggerProfileImageURLHttps"));
    assertEquals("MiniProfileImageURLHttps does not match.", user.getMiniProfileImageURLHttps(), struct.get("MiniProfileImageURLHttps"));
    assertEquals("OriginalProfileImageURLHttps does not match.", user.getOriginalProfileImageURLHttps(), struct.get("OriginalProfileImageURLHttps"));
    assertEquals("DefaultProfileImage does not match.", user.isDefaultProfileImage(), struct.get("DefaultProfileImage"));
    assertEquals("URL does not match.", user.getURL(), struct.get("URL"));
    assertEquals("Protected does not match.", user.isProtected(), struct.get("Protected"));
    assertEquals("FollowersCount does not match.", user.getFollowersCount(), struct.get("FollowersCount"));
    assertEquals("ProfileBackgroundColor does not match.", user.getProfileBackgroundColor(), struct.get("ProfileBackgroundColor"));
    assertEquals("ProfileTextColor does not match.", user.getProfileTextColor(), struct.get("ProfileTextColor"));
    assertEquals("ProfileLinkColor does not match.", user.getProfileLinkColor(), struct.get("ProfileLinkColor"));
    assertEquals("ProfileSidebarFillColor does not match.", user.getProfileSidebarFillColor(), struct.get("ProfileSidebarFillColor"));
    assertEquals("ProfileSidebarBorderColor does not match.", user.getProfileSidebarBorderColor(), struct.get("ProfileSidebarBorderColor"));
    assertEquals("ProfileUseBackgroundImage does not match.", user.isProfileUseBackgroundImage(), struct.get("ProfileUseBackgroundImage"));
    assertEquals("DefaultProfile does not match.", user.isDefaultProfile(), struct.get("DefaultProfile"));
    assertEquals("ShowAllInlineMedia does not match.", user.isShowAllInlineMedia(), struct.get("ShowAllInlineMedia"));
    assertEquals("FriendsCount does not match.", user.getFriendsCount(), struct.get("FriendsCount"));
    assertEquals("CreatedAt does not match.", user.getCreatedAt(), struct.get("CreatedAt"));
    assertEquals("FavouritesCount does not match.", user.getFavouritesCount(), struct.get("FavouritesCount"));
    assertEquals("UtcOffset does not match.", user.getUtcOffset(), struct.get("UtcOffset"));
    assertEquals("TimeZone does not match.", user.getTimeZone(), struct.get("TimeZone"));
    assertEquals("ProfileBackgroundImageURL does not match.", user.getProfileBackgroundImageURL(), struct.get("ProfileBackgroundImageURL"));
    assertEquals("ProfileBackgroundImageUrlHttps does not match.", user.getProfileBackgroundImageUrlHttps(), struct.get("ProfileBackgroundImageUrlHttps"));
    assertEquals("ProfileBannerURL does not match.", user.getProfileBannerURL(), struct.get("ProfileBannerURL"));
    assertEquals("ProfileBannerRetinaURL does not match.", user.getProfileBannerRetinaURL(), struct.get("ProfileBannerRetinaURL"));
    assertEquals("ProfileBannerIPadURL does not match.", user.getProfileBannerIPadURL(), struct.get("ProfileBannerIPadURL"));
    assertEquals("ProfileBannerIPadRetinaURL does not match.", user.getProfileBannerIPadRetinaURL(), struct.get("ProfileBannerIPadRetinaURL"));
    assertEquals("ProfileBannerMobileURL does not match.", user.getProfileBannerMobileURL(), struct.get("ProfileBannerMobileURL"));
    assertEquals("ProfileBannerMobileRetinaURL does not match.", user.getProfileBannerMobileRetinaURL(), struct.get("ProfileBannerMobileRetinaURL"));
    assertEquals("ProfileBackgroundTiled does not match.", user.isProfileBackgroundTiled(), struct.get("ProfileBackgroundTiled"));
    assertEquals("Lang does not match.", user.getLang(), struct.get("Lang"));
    assertEquals("StatusesCount does not match.", user.getStatusesCount(), struct.get("StatusesCount"));
    assertEquals("GeoEnabled does not match.", user.isGeoEnabled(), struct.get("GeoEnabled"));
    assertEquals("Verified does not match.", user.isVerified(), struct.get("Verified"));
    assertEquals("Translator does not match.", user.isTranslator(), struct.get("Translator"));
    assertEquals("ListedCount does not match.", user.getListedCount(), struct.get("ListedCount"));
    assertEquals("FollowRequestSent does not match.", user.isFollowRequestSent(), struct.get("FollowRequestSent"));
  }

  void assertKey(Status status, Struct struct) {
    assertEquals("Id does not match.", status.getId(), struct.get("Id"));
  }

  @Test
  public void convertStatus() {
    Status status = mockStatus();
    Struct struct = new Struct(StatusConverter.statusSchema);
    StatusConverter.convert(status, struct);
    assertStatus(status, struct);
  }

  @Test
  public void convertUser() {
    User user = mockUser();
    Struct struct = new Struct(StatusConverter.userSchema);
    StatusConverter.convert(user, struct);
    assertUser(user, struct);
  }

  @Test
  public void convertPlace() {
    Place place = mockPlace();
    Struct struct = new Struct(StatusConverter.placeSchema);
    StatusConverter.convert(place, struct);
    assertPlace(place, struct);
  }

  @Test
  public void convertGeoLocation() {
    GeoLocation geoLocation = mockGeoLocation();
    Struct struct = new Struct(StatusConverter.geoLocationSchema);
    StatusConverter.convert(geoLocation, struct);
    assertGeoLocation(geoLocation, struct);
  }

  @Test
  public void convertStatusKey() {
    Status status = mockStatus();
    Struct struct = new Struct(StatusConverter.statusSchemaKey);
    StatusConverter.convertKey(status, struct);
    assertKey(status, struct);
  }

  public static StatusDeletionNotice mockStatusDeletionNotice() {
    StatusDeletionNotice statusDeletionNotice = mock(StatusDeletionNotice.class);
    when(statusDeletionNotice.getStatusId()).thenReturn(1234565345L);
    when(statusDeletionNotice.getUserId()).thenReturn(6543456354L);
    return statusDeletionNotice;
  }

  void assertStatusDeletionNotice(StatusDeletionNotice statusDeletionNotice, Struct struct) {
    assertEquals("StatusId does not match.", statusDeletionNotice.getStatusId(), struct.get("StatusId"));
    assertEquals("UserId does not match.", statusDeletionNotice.getUserId(), struct.get("UserId"));
  }

  void assertStatusDeletionNoticeKey(StatusDeletionNotice statusDeletionNotice, Struct struct) {
    assertEquals("StatusId does not match.", statusDeletionNotice.getStatusId(), struct.get("StatusId"));
  }

  @Test
  public void convertStatusDeletionNotice() {
    StatusDeletionNotice statusDeletionNotice = mockStatusDeletionNotice();
    Struct struct = new Struct(StatusConverter.schemaStatusDeletionNotice);
    StatusConverter.convert(statusDeletionNotice, struct);
    assertStatusDeletionNotice(statusDeletionNotice, struct);
  }

  @Test
  public void convertKeyStatusDeletionNotice() {
    StatusDeletionNotice statusDeletionNotice = mockStatusDeletionNotice();
    Struct struct = new Struct(StatusConverter.schemaStatusDeletionNoticeKey);
    StatusConverter.convertKey(statusDeletionNotice, struct);
    assertStatusDeletionNoticeKey(statusDeletionNotice, struct);
  }
}
