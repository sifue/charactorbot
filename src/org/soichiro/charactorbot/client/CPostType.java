/**
 * 
 */
package org.soichiro.charactorbot.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Client side Twitter Post Type Data Class.
 * @author soichiro
 *
 */
public class CPostType implements Serializable {

	private static final long serialVersionUID = -5973262952651797021L;

	private String key;
	
	/** Parent TwitterAccount key string */
	private String twitterAccount;
	
	private List<CKeyword> listKeyword;
	
	private String postTypeName;
	
    private Integer interval;
    
    private Boolean isUseSleep;
	
    private Integer sequence;
    
	private String ignoredIDs;
	
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
	 * @return the twitterAccount
	 */
	public String getTwitterAccount() {
		return twitterAccount;
	}

	/**
	 * @param twitterAccount the twitterAccount to set
	 */
	public void setTwitterAccount(String twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	
	/**
	 * @return the listKeyword
	 */
	public List<CKeyword> getListKeyword() {
		
		if(this.listKeyword == null)
		{
			this.listKeyword = new ArrayList<CKeyword>();
		}
		
		return listKeyword;
	}

	/**
	 * @param listKeyword the listKeyword to set
	 */
	public void setListKeyword(List<CKeyword> listKeyword) {
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
		CPostType other = (CPostType) obj;
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
		return "CPostType [key=" + key + ", twitterAccount=" + twitterAccount
				+ ", listKeyword=" + listKeyword + ", postTypeName="
				+ postTypeName + ", interval=" + interval + ", isUseSleep="
				+ isUseSleep + ", sequence=" + sequence + ", ignoredIDs="
				+ ignoredIDs + ", createdAt=" + createdAt + ", createdBy="
				+ createdBy + ", updatedAt=" + updatedAt + ", updatedBy="
				+ updatedBy + "]";
	}
}
