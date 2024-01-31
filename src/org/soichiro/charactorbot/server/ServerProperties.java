package org.soichiro.charactorbot.server;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Properties of Charactorbot Server.
 * @author soichiro
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ServerProperties {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
    /**
     *  Stop creating bot function. you can change this property by datastore viewer.
     */
    @Persistent
    private Boolean isStopCreateBot;
    
    /**
     *  Limit of number of TwitterAccount. you can change this property by datastore viewer.
     */
    @Persistent
    private Integer limitTwitterAccount;
    
    /**
     *  Number of remaining TwitterAccount. Don't change this property by datastore viewer.
     */
    @Persistent
    private Integer numberRemainingTwitterAccount;
    
    /**
     *  Last execute epoch msec.
     */
    @Persistent
    private Long lastExecuteTime;
    
    /**
     *  Administrator's message of top page header.
     */
    @Persistent
    private String messageOnTopPage;
    
	/** Default of limit of TwitterAccount */
	public static final Integer DEFAULT_LIMIT_OF_TWITTER_ACCOUNT = Integer.valueOf(23);
	
	/** Default of message of TopPage */
	public static final String DEFAULT_MESSAGE_OF_TOP_PAGE = "<Set a message of top page>";

	/**
     * Private constructor
     */
    private ServerProperties() {
	}
    
    /**
	 * @return the key
	 */
	public Key getKey() {
		return key;
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
	
	
	/**
	 * @return the limitTwitterAccount
	 */
	public Integer getLimitTwitterAccount() {
		return limitTwitterAccount;
	}

	/**
	 * @param limitTwitterAccount the limitTwitterAccount to set
	 */
	public void setLimitTwitterAccount(Integer limitTwitterAccount) {
		this.limitTwitterAccount = limitTwitterAccount;
	}

	
	/**
	 * @return the numberRemainingTwitterAccount
	 */
	public Integer getNumberRemainingTwitterAccount() {
		return numberRemainingTwitterAccount;
	}

	/**
	 * @param numberRemainingTwitterAccount the numberRemainingTwitterAccount to set
	 */
	public void setNumberRemainingTwitterAccount(
			Integer numberRemainingTwitterAccount) {
		this.numberRemainingTwitterAccount = numberRemainingTwitterAccount;
	}

	/**
	 * @return the lastExecuteTime
	 */
	public Long getLastExecuteTime() {
		return lastExecuteTime;
	}

	/**
	 * @param lastExecuteTime the lastExecuteTime to set
	 */
	public void setLastExecuteTime(Long lastExecuteTime) {
		this.lastExecuteTime = lastExecuteTime;
	}

	/**
	 * @return the messageOnTopPage
	 */
	public String getMessageOnTopPage() {
		return messageOnTopPage;
	}

	/**
	 * @param messageOnTopPage the messageOnTopPage to set
	 */
	public void setMessageOnTopPage(String messageOnTopPage) {
		this.messageOnTopPage = messageOnTopPage;
	}


	/**
	 * get instance of CharactorbotProperties
	 * @param pm
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized static ServerProperties getInstance(PersistenceManager pm){
		Query query = pm.newQuery(ServerProperties.class);
		List<ServerProperties> listProperties = (List<ServerProperties>)query.execute();
		
		ServerProperties properties = null;
		if(listProperties.size() == 0){
			properties = new ServerProperties();
			
			// set default property
			properties.setIsStopCreateBot(Boolean.FALSE);
			properties.setLimitTwitterAccount(DEFAULT_LIMIT_OF_TWITTER_ACCOUNT);
			properties.setNumberRemainingTwitterAccount(DEFAULT_LIMIT_OF_TWITTER_ACCOUNT);
			
			pm.makePersistent(properties);
			return properties;
		} else {
			return listProperties.get(0);
		}
	}
}
