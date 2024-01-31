/**
 * 
 */
package org.soichiro.charactorbot.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Client side Twitter Bot Keyword Data Class.
 * @author soichiro
 *
 */
public class CKeyword implements Serializable {

	private static final long serialVersionUID = -7155612240517956590L;

	private String key;
	
	/** Parent PostType key string */
	private String postType;
	
	private List<CPost> listPost;
	
	private String keyword;
	
    private Integer sequence;
    
    private Boolean isRegex;
    
	private Boolean isActivated;
    
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
	 * @return the postType
	 */
	public String getPostType() {
		return postType;
	}

	/**
	 * @param postType the postType to set
	 */
	public void setPostType(String postType) {
		this.postType = postType;
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
	 * @return the listPost
	 */
	public List<CPost> getListPost() {
		
		if(this.listPost == null)
		{
			this.listPost = new ArrayList<CPost>();
		}
		
		return listPost;
	}

	/**
	 * @param listPost the listPost to set
	 */
	public void setListPost(List<CPost> listPost) {
		this.listPost = listPost;
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
		CKeyword other = (CKeyword) obj;
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
		return "CKeyword [key=" + key + ", postType=" + postType
				+ ", listPost=" + listPost + ", keyword=" + keyword
				+ ", sequence=" + sequence + ", isRegex=" + isRegex
				+ ", isActivated=" + isActivated + ", createdAt=" + createdAt
				+ ", createdBy=" + createdBy + ", updatedAt=" + updatedAt
				+ ", updatedBy=" + updatedBy + "]";
	}
	
}
