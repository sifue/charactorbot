/**
 * 
 */
package org.soichiro.charactorbot.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.soichiro.charactorbot.client.CPost;
import org.soichiro.charactorbot.client.CUser;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;

/**
 * Twitter Post Contents Data Class.
 * @author soichiro
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Post implements IUpdatable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
    
	/** Parent Keyword */
	@Persistent
	private Keyword keyword;
	
    @Persistent
    private Integer sequence;
    
    @Persistent
    private String message;
    
    @Persistent
    private Integer count;

    @Persistent
    private Date createdAt;
    
    @Persistent
    private User createdBy;
    
    @Persistent
    private Date updatedAt;
    
    @Persistent
    private User updatedBy;
    
    /**
     * Constructor
     */
    public Post() {
	}

	/**
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * @return the keyword
	 */
	public Keyword getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(Keyword keyword) {
		this.keyword = keyword;
	}

	/**
	 * @return the sequence
	 */
	public Integer getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
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
	 * @param ori
	 * @return client side data.
	 */
	public static CPost createClientSideDate(Post ori)
	{
		CPost clientData = new CPost();
		clientData.setKey(KeyFactory.keyToString(ori.key));
		clientData.setKeyword(KeyFactory.keyToString(ori.getKeyword().getKey()));
		clientData.setMessage(ori.getMessage());
		clientData.setSequence(ori.getSequence());
		clientData.setCount(ori.getCount());
		clientData.setCreatedAt((Date)ori.createdAt.clone());
		clientData.setCreatedBy(ori.createdBy != null ? 
				new CUser(ori.createdBy.getEmail(), ori.createdBy.getAuthDomain()) : null);
		clientData.setUpdatedAt((Date)ori.updatedAt.clone());
		clientData.setUpdatedBy(ori.updatedBy != null ?
				new CUser(ori.updatedBy.getEmail(), ori.updatedBy.getAuthDomain()) : null);
		return clientData;
	}
}
