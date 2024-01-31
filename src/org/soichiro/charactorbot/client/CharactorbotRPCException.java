/**
 * 
 */
package org.soichiro.charactorbot.client;

import java.io.Serializable;

/**
 * Exception for GWT-RPC of charactorbot
 * @author soichiro
 *
 */
public class CharactorbotRPCException extends Exception implements Serializable {

	private static final long serialVersionUID = 4960612653132577680L;
	private String keyTwitterAccount;
	
	/**
	 * Constructor
	 */
	public CharactorbotRPCException() {
		super();
	}
	
	/**
	 * Constructor
	 * @param message
	 */
	public CharactorbotRPCException(String message) {
		super(message);
	}
	
	/**
	 * Constructor
	 * @param t Throwable
	 */
	public CharactorbotRPCException(Throwable t) {
		super(t);
	}
	
	/**
	 * Constructor
	 * @param t Throwable
	 * @param keyTwitterAccount
	 */
	public CharactorbotRPCException(Throwable t, String keyTwitterAccount) {
		super(t);
		this.keyTwitterAccount = keyTwitterAccount;
	}

	/**
	 * @return the keyTwitterAccount
	 */
	public String getKeyTwitterAccount() {
		return keyTwitterAccount;
	}
}
