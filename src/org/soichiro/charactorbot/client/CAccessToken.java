/**
 * 
 */
package org.soichiro.charactorbot.client;

import java.io.Serializable;

/**
 * Twitter Access token client side data class.
 * @author soichiro
 *
 */
public class CAccessToken  implements Serializable{
	
	private static final long serialVersionUID = 1206089476741776666L;

	private String screenName;
	
	private int userId;
	
	private String consumerKey;
	
	private String consumerSecret;
	
	private String token;
	
    private String tokenSecret;
    
    private Boolean isStopCreateBot;

	public CAccessToken() {
		super();
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
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
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
	 * @return the tokenSecret
	 */
	public String getTokenSecret() {
		return tokenSecret;
	}

	/**
	 * @param tokenSecret the tokenSecret to set
	 */
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	/**
	 * @return the isStopCreateBot
	 */
	public Boolean getIsStopCreateBot() {
		return isStopCreateBot;
	}

	/**
	 * @param isStopCreateBot the isStopCreateBot to set
	 */
	public void setIsStopCreateBot(Boolean isStopCreateBot) {
		this.isStopCreateBot = isStopCreateBot;
	}
}
