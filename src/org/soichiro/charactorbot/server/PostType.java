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
import org.soichiro.charactorbot.client.CPostType;
import org.soichiro.charactorbot.client.CUser;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;

/**
 * Twitter Post Type Data Class.
 * @author soichiro
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PostType implements IUpdatable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	/** Parent TwitterAccount */
	@Persistent
	private TwitterAccount twitterAccount;
	
	@Persistent(mappedBy = "postType")
	@Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="sequence asc"))
	private List<Keyword> listKeyword;
	
	@Persistent
	private String postTypeName;
	
    @Persistent
    private Integer interval;
    
    @Persistent
    private Boolean isUseSleep;
	
    @Persistent
    private Integer sequence;
	
	@Persistent
	private String ignoredIDs;
    
	@Persistent
	private Date createdAt;
	   
	@Persistent
	private User createdBy;
	    
	@Persistent
	private Date updatedAt;
	    
	@Persistent
	private User updatedBy;

	/**
	 * @return the twitterAccount
	 */
	public TwitterAccount getTwitterAccount() {
		return twitterAccount;
	}

	/**
	 * @param twitterAccount the twitterAccount to set
	 */
	public void setTwitterAccount(TwitterAccount twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	/**
	 * @return the listKeyword
	 */
	public List<Keyword> getListKeyword() {
		return listKeyword;
	}

	/**
	 * @param listKeyword the listKeyword to set
	 */
	public void setListKeyword(List<Keyword> listKeyword) {
		this.listKeyword = listKeyword;
	}

	/**
	 * @return the postTypeName
	 */
	public String getPostTypeName() {
		return postTypeName;
	}

	/**
	 * @param postTypeName the postTypeName to set
	 */
	public void setPostTypeName(String postTypeName) {
		this.postTypeName = postTypeName;
	}

	/**
	 * @return the interval
	 */
	public Integer getInterval() {
		return interval;
	}

	/**
	 * @param interval the interval to set
	 */
	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	/**
	 * @return the isUseSleep
	 */
	public Boolean getIsUseSleep() {
		return isUseSleep;
	}

	/**
	 * @param isUseSleep the isUseSleep to set
	 */
	public void setIsUseSleep(Boolean isUseSleep) {
		this.isUseSleep = isUseSleep;
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
	 * @return the ignoredIDs
	 */
	public String getIgnoredIDs() {
		return ignoredIDs;
	}

	/**
	 * @param ignoredIDs the ignoredIDs to set
	 */
	public void setIgnoredIDs(String ignoredIDs) {
		this.ignoredIDs = ignoredIDs;
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
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}
	
	/**
	 * Create client side data.
	 * @param ori
	 * @return client side data.
	 */
	public static CPostType createClientSideDate(PostType ori)
	{
		CPostType clientData = new CPostType();
		clientData.setKey(KeyFactory.keyToString(ori.key));
		clientData.setTwitterAccount(KeyFactory.keyToString(ori.getTwitterAccount().getKey()));
		clientData.setPostTypeName(ori.getPostTypeName());
		clientData.setInterval(ori.getInterval());
		clientData.setIsUseSleep(ori.getIsUseSleep());
		clientData.setSequence(ori.getSequence());
		clientData.setIgnoredIDs(ori.getIgnoredIDs());
		clientData.setCreatedAt((Date)ori.createdAt.clone());
		clientData.setCreatedBy(ori.createdBy != null ?
				new CUser(ori.createdBy.getEmail(), ori.createdBy.getAuthDomain()) : null);
		clientData.setUpdatedAt((Date)ori.updatedAt.clone());
		clientData.setUpdatedBy(ori.updatedBy != null ? 
				new CUser(ori.updatedBy.getEmail(), ori.updatedBy.getAuthDomain()) : null);
		return clientData;
	}
	
	/**
	 * Create client side data with detail(CKeyword and CPost).
	 * @param postType
	 * @return
	 */
	public static CPostType createClientSideDataWithDetail(PostType postType) {
		CPostType cPostType = PostType.createClientSideDate(postType);
		List<Keyword> listKeyword = postType.getListKeyword();
		
		for (Keyword keyword : listKeyword) {
			CKeyword cKeyword = Keyword.createClientSideDate(keyword);
			cPostType.getListKeyword().add(cKeyword);
			
			// Create post list too.
			List<Post> listPost = keyword.getListPost();
			for (Post post : listPost) {
				cKeyword.getListPost().add(Post.createClientSideDate(post));
			}
		}
		return cPostType;
	}
}
