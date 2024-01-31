/**
 * 
 */
package org.soichiro.charactorbot.server;

import java.util.Date;
import java.util.Random;

import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Place;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.SymbolEntity;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;

/**
 * @author soichiro
 *
 */
public class StatusMock implements Status {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4258851902721378978L;

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Status o) {
		
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
	 * @see twitter4j.Status#getCreatedAt()
	 */
	@Override
	public Date getCreatedAt() {
		
		return null;
	}
	
	private long id = new Random().nextLong();
	
	/* (non-Javadoc)
	 * @see twitter4j.Status#getId()
	 */
	@Override
	public long getId() {
		
		return id;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#getText()
	 */
	@Override
	public String getText() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#getSource()
	 */
	@Override
	public String getSource() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#isTruncated()
	 */
	@Override
	public boolean isTruncated() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#getInReplyToStatusId()
	 */
	@Override
	public long getInReplyToStatusId() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#getInReplyToUserId()
	 */
	@Override
	public long getInReplyToUserId() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#getInReplyToScreenName()
	 */
	@Override
	public String getInReplyToScreenName() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#getGeoLocation()
	 */
	@Override
	public GeoLocation getGeoLocation() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#getPlace()
	 */
	@Override
	public Place getPlace() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#isFavorited()
	 */
	@Override
	public boolean isFavorited() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#getUser()
	 */
	@Override
	public User getUser() {
		return new UserMock(){
			private static final long serialVersionUID = 2156306332461134360L;
			@Override
			public String getScreenName() {
				return "sifue_4466";
			};
		};
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#isRetweet()
	 */
	@Override
	public boolean isRetweet() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#getRetweetedStatus()
	 */
	@Override
	public Status getRetweetedStatus() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#getContributors()
	 */
	@Override
	public long[] getContributors() {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.Status#isRetweetedByMe()
	 */
	@Override
	public boolean isRetweetedByMe() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#getUserMentionEntities()
	 */
	@Override
	public UserMentionEntity[] getUserMentionEntities() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#getURLEntities()
	 */
	@Override
	public URLEntity[] getURLEntities() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#getHashtagEntities()
	 */
	@Override
	public HashtagEntity[] getHashtagEntities() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#getMediaEntities()
	 */
	@Override
	public MediaEntity[] getMediaEntities() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#isPossiblySensitive()
	 */
	@Override
	public boolean isPossiblySensitive() {
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.Status#getCurrentUserRetweetId()
	 */
	@Override
	public long getCurrentUserRetweetId() {
		return 0;
	}

	@Override
	public SymbolEntity[] getSymbolEntities() {
		
		return null;
	}

	@Override
	public int getFavoriteCount() {
		
		return 0;
	}

	@Override
	public String getIsoLanguageCode() {
		
		return null;
	}

	@Override
	public boolean isRetweeted() {
		
		return false;
	}

	@Override
	public int getRetweetCount() {
		
		return 0;
	}
}
