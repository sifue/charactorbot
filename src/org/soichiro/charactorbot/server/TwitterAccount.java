/**
 * 
 */
package org.soichiro.charactorbot.server;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.soichiro.charactorbot.client.CTwitterAccount;
import org.soichiro.charactorbot.client.CUser;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;

/**
 * Twitter Account Data Class.
 * @author soichiro
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class TwitterAccount implements IUpdatable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent(mappedBy = "twitterAccount")
	@Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="sequence asc"))
	private List<PostType> listPostType;
	  
	@Persistent
	private User owner; 
	
	@Persistent
	private String botName;
	
	@Persistent
	private String screenName;
	    
	@Persistent
	private String consumerKey;
	   
	@Persistent
	private String consumerSecret;
	   
	@Persistent
	private String token;
	   
	@Persistent
	private String secret;
	
	@Persistent
	private Boolean isActivated;
	
	@Persistent
	private String timeZoneId;
	
	@Persistent
	private Date createdAt;
	   
	@Persistent
	private User createdBy;
	    
	@Persistent
	private Date updatedAt;
	    
	@Persistent
	private User updatedBy;
	
	/**
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * @return the listPostType
	 */
	public List<PostType> getListPostType() {
		return listPostType;
	}

	/**
	 * @param listPostType the listPostType to set
	 */
	public void setListPostType(List<PostType> listPostType) {
		this.listPostType = listPostType;
	}

	/**
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(User owner) {
		this.owner = owner;
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
	public User getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(User createdBy) {
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
	public User getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * Create client side data.
	 * @param ori original JDO data.
	 * @return client side data.
	 */
	public static CTwitterAccount createClientSideData(TwitterAccount ori){
		CTwitterAccount clientData = new CTwitterAccount();
		clientData.setKey(KeyFactory.keyToString(ori.key));
		clientData.setOwner(new CUser(ori.owner.getEmail(), ori.owner.getAuthDomain()));
		clientData.setBotName(ori.botName);
		clientData.setScreenName(ori.screenName);
		clientData.setConsumerKey(ori.consumerKey);
		clientData.setConsumerSecret(ori.consumerSecret);
		clientData.setToken(ori.token);
		clientData.setSecret(ori.secret);
		clientData.setIsActivated(ori.isActivated);
		clientData.setTimeZoneId(ori.timeZoneId);
		clientData.setCreatedAt((Date)ori.createdAt.clone());
		clientData.setCreatedBy(ori.createdBy != null ? 
				new CUser(ori.createdBy.getEmail(), ori.createdBy.getAuthDomain()) : null);
		clientData.setUpdatedAt((Date)ori.updatedAt.clone());
		clientData.setUpdatedBy(ori.updatedBy != null ?
				new CUser(ori.updatedBy.getEmail(), ori.updatedBy.getAuthDomain()) : null);
		return clientData;
	}
}
