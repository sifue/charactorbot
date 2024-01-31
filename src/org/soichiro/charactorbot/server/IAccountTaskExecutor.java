/**
 * 
 */
package org.soichiro.charactorbot.server;

import java.util.Date;
import java.util.List;

/**
 * Interface of AccountTaskExecutor
 * @author soichiro
 *
 */
public interface IAccountTaskExecutor {

	/**
	 * add task queue.
	 * @param now
	 * @param queue
	 * @param listExecuteKey
	 */
	public void addTaskQueue(Date now, List<String> listExecuteKey);

}