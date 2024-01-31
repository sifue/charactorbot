package org.soichiro.charactorbot.client;

import java.io.Serializable;
import java.util.Date;

/**
 * Client side LogEntry Data Class. <br>
 * for using datastore low-level API <br>
 * @author soichiro
 *
 */
public class CLogEntry implements Serializable {

	private static final long serialVersionUID = 4529616928949680369L;
	
	/** Parent TwitterAccount key string */
	private String twitterAccount;
	
	/** logType */
	private String logType;
	
	/** logText */
    private String logText;
    
    /** createdAt */
	private Date createdAt;
	
	/**
	 * Constructor
	 */
	public CLogEntry() {
		super();
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
	 * @return the logType
	 */
	public String getLogType() {
		return logType;
	}

	/**
	 * @param logType the logType to set
	 */
	public void setLogType(String logType) {
		this.logType = logType;
	}

	/**
	 * @return the logText
	 */
	public String getLogText() {
		return logText;
	}

	/**
	 * @param logText the logText to set
	 */
	public void setLogText(String logText) {
		this.logText = logText;
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


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CLogEntry [twitterAccount=" + twitterAccount + ", logType="
				+ logType + ", logText=" + logText + ", createdAt=" + createdAt
				+ "]";
	}
	
}
