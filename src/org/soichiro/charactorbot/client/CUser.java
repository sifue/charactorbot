/**
 * 
 */
package org.soichiro.charactorbot.client;

import java.io.Serializable;

/**
 * Client side User data class.
 * 
 * @author soichiro
 *
 */
public class CUser  implements Serializable{
	
	private static final long serialVersionUID = -6736849535152014655L;

	private String email;
	
	private String authDomain;
	
	/**
	 * Constructor
	 */
	public CUser() {
		super();
	}
	
	/**
	 * Constructor
	 * @param email
	 * @param authDomain
	 */
	public CUser(String email, String authDomain)
	{
		super();
		this.email = email;
		this.authDomain = authDomain;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the authDomain
	 */
	public String getAuthDomain() {
		return authDomain;
	}

	/**
	 * @param authDomain the authDomain to set
	 */
	public void setAuthDomain(String authDomain) {
		this.authDomain = authDomain;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authDomain == null) ? 0 : authDomain.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		CUser other = (CUser) obj;
		if (authDomain == null) {
			if (other.authDomain != null)
				return false;
		} else if (!authDomain.equals(other.authDomain))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CUser [email=" + email + ", authDomain=" + authDomain + "]";
	}
}
