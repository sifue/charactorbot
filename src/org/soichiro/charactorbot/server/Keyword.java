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

import org.soichiro.charactorbot.client.CKeyword;
import org.soichiro.charactorbot.client.CUser;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;

/**
 * Twitter Bot Keyword Data Class.
 * @author soichiro
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Keyword implements IUpdatable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	/** Parent PostType */
	@Persistent
	private PostType postType;
	
	@Persistent(mappedBy = "keyword")
	@Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="sequence asc"))
	private List<Post> listPost;
	
	@Persistent
	private String keyword;
	
    @Persistent
    private Integer sequence;
    
    @Persistent
    private Boolean isRegex;
    
	@Persistent
	private Boolean isActivated;
    
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
	 * @return the postType
	 */
	public PostType getPostType() {
		return postType;
	}

	/**
	 * @param postType the postType to set
	 */
	public void setPostType(PostType postType) {
		this.postType = postType;
	}

	/**
	 * @return the listPost
	 */
	public List<Post> getListPost() {
		return listPost;
	}

	/**
	 * @param listPost the listPost to set
	 */
	public void setListPost(List<Post> listPost) {
		this.listPost = listPost;
	}

	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(String keyword) {
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
	 * @return the isRegex
	 */
	public Boolean getIsRegex() {
		return isRegex;
	}

	/**
	 * @param isRegex the isRegex to set
	 */
	public void setIsRegex(Boolean isRegex) {
		this.isRegex = isRegex;
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
	public static CKeyword createClientSideDate(Keyword ori)
	{
		CKeyword clientData = new CKeyword();
		clientData.setKey(KeyFactory.keyToString(ori.key));
		clientData.setPostType(KeyFactory.keyToString(ori.getPostType().getKey()));
		clientData.setKeyword(ori.getKeyword());
		clientData.setSequence(ori.getSequence());
		clientData.setIsRegex(ori.getIsRegex());
		clientData.setIsActivated(ori.getIsActivated());
		clientData.setCreatedAt((Date)ori.createdAt.clone());
		clientData.setCreatedBy(ori.createdBy != null ?
				new CUser(ori.createdBy.getEmail(), ori.createdBy.getAuthDomain()) : null);
		clientData.setUpdatedAt((Date)ori.updatedAt.clone());
		clientData.setUpdatedBy(ori.updatedBy != null ?
				new CUser(ori.updatedBy.getEmail(), ori.updatedBy.getAuthDomain()) : null);
		return clientData;
	}
}
