/**
 * 
 */
package org.soichiro.charactorbot.client;

import java.io.Serializable;
import java.util.Date;

/**
 * Client side Twitter Account Data Class.
 * @author soichiro
 *
 */
public class CTwitterAccount implements Serializable {

	private static final long serialVersionUID = 3712185751044647708L;

	private String key;
	
	private CUser owner; 
	
	private String botName;
	
	private String screenName;
	    
	private String consumerKey;
	   
	private String consumerSecret;
	   
	private String token;
	   
	private String secret;
	
	private Boolean isActivated;
	
	private String timeZoneId;
	
	private Date createdAt;
	   
	private CUser createdBy;
	    
	private Date updatedAt;
	    
	private CUser updatedBy;

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the owner
	 */
	public CUser getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(CUser owner) {
		this.owner = owner;
	}

	/**
	 * @return the botName
	 */
	public String getBotName() {
		return botName;
	}

	/**
	 * @param botName the botName to set
	 */
	public void setBotName(String botName) {
		this.botName = botName;
	}

	/**
	 * @return the screenName
	 */
	public String getScreenName() {
		return screenName;
	}

	/**
	 * @param screenName the screenName to set
	 */
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	/**
	 * @return the consumerKey
	 */
	public String getConsumerKey() {
		return consumerKey;
	}

	/**
	 * @param consumerKey the consumerKey to set
	 */
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	/**
	 * @return the consumerSecret
	 */
	public String getConsumerSecret() {
		return consumerSecret;
	}

	/**
	 * @param consumerSecret the consumerSecret to set
	 */
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param secret the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * @return the isActivated
	 */
	public Boolean getIsActivated() {
		return isActivated;
	}

	/**
	 * @param isActivated the isActivated to set
	 */
	public void setIsActivated(Boolean isActivated) {
		this.isActivated = isActivated;
	}

	/**
	 * @return the timeZoneId
	 */
	public String getTimeZoneId() {
		return timeZoneId;
	}

	/**
	 * @param timeZoneId the timeZoneId to set
	 */
	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the createdBy
	 */
	public CUser getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(CUser createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the updatedAt
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the updatedBy
	 */
	public CUser getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(CUser updatedBy) {
		this.updatedBy = updatedBy;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CTwitterAccount other = (CTwitterAccount) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CTwitterAccount [key=" + key + ", owner=" + owner
				+ ", botName=" + botName + ", screenName=" + screenName
				+ ", consumerKey=" + consumerKey + ", consumerSecret="
				+ consumerSecret + ", token=" + token + ", secret=" + secret
				+ ", isActivated=" + isActivated + ", timeZoneId=" + timeZoneId
				+ ", createdAt=" + createdAt + ", createdBy=" + createdBy
				+ ", updatedAt=" + updatedAt + ", updatedBy=" + updatedBy + "]";
	}
	
}
