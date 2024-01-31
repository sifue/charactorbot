/**
 * 
 */
package org.soichiro.charactorbot.server;

import java.net.URL;
import java.util.Date;

import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.User;

/**
 * @author soichiro
 *
 */
public class UserMock implements User {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -867989726787243001L;

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(User o) {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see twitter4j.TwitterResponse#getRateLimitStatus()
	 */
	@Override
	public RateLimitStatus getRateLimitStatus() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.TwitterResponse#getAccessLevel()
	 */
	@Override
	public int getAccessLevel() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getId()
	 */
	@Override
	public long getId() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getName()
	 */
	@Override
	public String getName() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getScreenName()
	 */
	@Override
	public String getScreenName() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getLocation()
	 */
	@Override
	public String getLocation() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getDescription()
	 */
	@Override
	public String getDescription() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#isContributorsEnabled()
	 */
	@Override
	public boolean isContributorsEnabled() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileImageUrlHttps()
	 */
	@Override
	public URL getProfileImageUrlHttps() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#isProtected()
	 */
	@Override
	public boolean isProtected() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getFollowersCount()
	 */
	@Override
	public int getFollowersCount() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getStatus()
	 */
	@Override
	public Status getStatus() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileBackgroundColor()
	 */
	@Override
	public String getProfileBackgroundColor() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileTextColor()
	 */
	@Override
	public String getProfileTextColor() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileLinkColor()
	 */
	@Override
	public String getProfileLinkColor() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileSidebarFillColor()
	 */
	@Override
	public String getProfileSidebarFillColor() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileSidebarBorderColor()
	 */
	@Override
	public String getProfileSidebarBorderColor() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#isProfileUseBackgroundImage()
	 */
	@Override
	public boolean isProfileUseBackgroundImage() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#isShowAllInlineMedia()
	 */
	@Override
	public boolean isShowAllInlineMedia() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getFriendsCount()
	 */
	@Override
	public int getFriendsCount() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getCreatedAt()
	 */
	@Override
	public Date getCreatedAt() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getFavouritesCount()
	 */
	@Override
	public int getFavouritesCount() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getUtcOffset()
	 */
	@Override
	public int getUtcOffset() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getTimeZone()
	 */
	@Override
	public String getTimeZone() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileBackgroundImageUrl()
	 */
	@Override
	public String getProfileBackgroundImageUrl() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileBackgroundImageUrlHttps()
	 */
	@Override
	public String getProfileBackgroundImageUrlHttps() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#isProfileBackgroundTiled()
	 */
	@Override
	public boolean isProfileBackgroundTiled() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getLang()
	 */
	@Override
	public String getLang() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getStatusesCount()
	 */
	@Override
	public int getStatusesCount() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#isGeoEnabled()
	 */
	@Override
	public boolean isGeoEnabled() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#isVerified()
	 */
	@Override
	public boolean isVerified() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#isTranslator()
	 */
	@Override
	public boolean isTranslator() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getListedCount()
	 */
	@Override
	public int getListedCount() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#isFollowRequestSent()
	 */
	@Override
	public boolean isFollowRequestSent() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileImageURL()
	 */
	@Override
	public String getProfileImageURL() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getBiggerProfileImageURL()
	 */
	@Override
	public String getBiggerProfileImageURL() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getMiniProfileImageURL()
	 */
	@Override
	public String getMiniProfileImageURL() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getOriginalProfileImageURL()
	 */
	@Override
	public String getOriginalProfileImageURL() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileImageURLHttps()
	 */
	@Override
	public String getProfileImageURLHttps() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getBiggerProfileImageURLHttps()
	 */
	@Override
	public String getBiggerProfileImageURLHttps() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getMiniProfileImageURLHttps()
	 */
	@Override
	public String getMiniProfileImageURLHttps() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getOriginalProfileImageURLHttps()
	 */
	@Override
	public String getOriginalProfileImageURLHttps() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getURL()
	 */
	@Override
	public String getURL() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileBackgroundImageURL()
	 */
	@Override
	public String getProfileBackgroundImageURL() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileBannerURL()
	 */
	@Override
	public String getProfileBannerURL() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileBannerRetinaURL()
	 */
	@Override
	public String getProfileBannerRetinaURL() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileBannerIPadURL()
	 */
	@Override
	public String getProfileBannerIPadURL() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileBannerIPadRetinaURL()
	 */
	@Override
	public String getProfileBannerIPadRetinaURL() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileBannerMobileURL()
	 */
	@Override
	public String getProfileBannerMobileURL() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.User#getProfileBannerMobileRetinaURL()
	 */
	@Override
	public String getProfileBannerMobileRetinaURL() {
		
		return null;
	}

	@Override
	public URLEntity[] getDescriptionURLEntities() {
		return null;
	}

	@Override
	public URLEntity getURLEntity() {
		return null;
	}

}
