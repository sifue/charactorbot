package org.soichiro.charactorbot.server;

/**
 * Exception for DatastoreLogHandler
 * 
 * @author soichiro
 *
 */
public class TwitterBotException extends Exception {

	private static final long serialVersionUID = 4960612653132577680L;
	private final String keyTwitterAccount;
	
	/**
	 * Constructor
	 * @param t
	 */
	public TwitterBotException(Throwable t, String keyTwitterAccount) {
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
