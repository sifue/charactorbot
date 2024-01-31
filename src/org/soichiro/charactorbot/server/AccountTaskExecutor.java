/**
 * 
 */
package org.soichiro.charactorbot.server;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

/**
 * Executor of account task.
 * @author soichiro
 *
 */
public class AccountTaskExecutor implements IAccountTaskExecutor {

	/** Task Queue */
	private Queue queue = QueueFactory.getQueue("background-processing");
    
	/* (non-Javadoc)
	 * @see org.soichiro.charactorbot.server.IAccountTaskExecutor#addTaskQueue(java.util.Date, java.util.List)
	 */
	@Override
	public void addTaskQueue(Date now, List<String> listExecuteKey) {
		
		if(listExecuteKey == null || listExecuteKey.size() == 0) return;
		
		TaskOptions ops = withUrl("/tasks/accounttask");
		
		for (String executeKey : listExecuteKey) {
			ops = ops.param("key", executeKey);
		}
		ops = ops.param("now", Long.valueOf(now.getTime()).toString());
		ops = ops.method(Method.GET);
		
		queue.add(ops);
	}
	
}
