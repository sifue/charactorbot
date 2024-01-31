/**
 * 
 */
package org.soichiro.charactorbot.server;

import java.util.Date;

/**
 * Interface of TwitterBot
 * @author soichiro
 *
 */
public interface ITwitterBot {

	/**
	 * Post to Twitter
	 * @param keyTwitterAccount
	 * @param now
	 */
	public void run(String keyTwitterAccount, Date now);

}