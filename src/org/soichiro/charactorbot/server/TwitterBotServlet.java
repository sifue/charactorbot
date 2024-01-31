package org.soichiro.charactorbot.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.KeyFactory;

/**
 * Twitter bot servlet for cron.
 * @author soichiro
 *
 */
public class TwitterBotServlet extends HttpServlet {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -4709070816754025240L;
	
	/** Use flag of batch of Account Task Servlet*/
	/* package */ static final String IS_USE_BATCH_KEY = "org.soichiro.charactorbot.server.TwitterBotServlet.isUseBatch";
	/** Batch size of Account Task Servlet*/
	/* package */ static final String BATCH_SIZE_KEY = "org.soichiro.charactorbot.server.TwitterBotServlet.batchSize";
	
	/** Logger */
	private static final Logger log = Logger.getLogger(TwitterBotServlet.class.getName());

	/** ITwitterBot */
	/* package */ ITwitterBot bot;
	/** AccountTaskExecutor */
	/* package */ IAccountTaskExecutor executer;
	
	static {
	   	log.setLevel(Level.ALL);
	}
	
    /*
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
    	Date now = new Date();
    	
		resp.setContentType("text/plain");
		resp.getWriter().println(String.format("TwitterBotServlet called at %s.", now.toString()));
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		boolean isUseBatch = true;
		int batchSize = -1;
		int batchCount = -1;
		try
		{
			List<String> listKey = new ArrayList<String>();
			Query query = pm.newQuery(TwitterAccount.class);
			List<TwitterAccount> listTwitterAccount = (List<TwitterAccount>)query.execute();
			
			// filter by isActivated property.
			for (TwitterAccount twitterAccount : listTwitterAccount) {
				if(!twitterAccount.getIsActivated().booleanValue()) continue;
				listKey.add(KeyFactory.keyToString(twitterAccount.getKey()));
			}
			
			
			String strIsUseBatch = System.getProperty(IS_USE_BATCH_KEY, "true");
			isUseBatch = Boolean.parseBoolean(strIsUseBatch);
			if(isUseBatch){ // divide tasks by batch
				
				String strBatchSize = System.getProperty(BATCH_SIZE_KEY, "1");
				batchSize = Integer.parseInt(strBatchSize);
				
				int keyCount = listKey.size();
				batchCount = (keyCount / batchSize) + (keyCount % batchSize == 0 ? 0 : 1);
				
				// Add Task Queue with batch of accountKey
				for (int i = 0; i < batchCount; i++) {
					int fromIndex = i * batchSize;
					int toIndex;
					if(i == (batchCount - 1)){ // if final batch
						toIndex = keyCount;
					} else {
						toIndex = ((i + 1) * batchSize);
					}
					
					List<String> listExecuteKey =listKey.subList(fromIndex, toIndex);
					
					if(executer == null){
						executer = new AccountTaskExecutor();
					}
					executer.addTaskQueue(now, listExecuteKey);
					
					log.info(String.format("Add Queue. [now:%1$s] [batch size:%2$d]",
							now.toString(),
							listExecuteKey.size()));
				}
				
			} else { // not divide tasks
				if(bot == null) {
					bot = TwitterBot.getInstance();
				}
				for (String keyTwitterAccount : listKey) {
					bot.run(keyTwitterAccount, now);
				}
				
			}
			
		} finally {
	        pm.close();
	    }
		log.info(String.format("TwitterBot service successfully finished. [conf. batch size:%1$d] [batch count:%2$d]",
				batchSize,
				batchCount));
    }

}
