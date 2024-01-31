package org.soichiro.charactorbot.server;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.memcache.stdimpl.GCacheException;
import com.google.apphosting.api.ApiProxy;

/**
 * Account task servlet for cron.
 * @author soichiro
 *
 */
public class AccountTaskServlet extends HttpServlet {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -4709070816754025240L;
	
	/** Logger */
	private static final Logger log = Logger.getLogger(AccountTaskServlet.class.getName());

	/** ITwitterBot */
	/* package private for test */ ITwitterBot bot;
	
	static {
	   	log.setLevel(Level.ALL);
	}
	
    /*
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
		Date start = new Date();
    	
		resp.setContentType("text/plain");
		resp.getWriter().println(String.format("AccountTaskServlet called at %s.", new Date().toString()));
		
		String[] keyTwitterAccounts = req.getParameterValues("key");
		String strNow = req.getParameter("now");
		
		// If it's request to spin up, terminate. 
		if(keyTwitterAccounts == null){
			log.info("AccountTask service terminated. keyTwitterAccounts is null.");
			return;
		}
		if(strNow == null ){
			log.info("AccountTask service terminated. strNow is null.");
			return;
		}
		
		Date now = new Date(Long.parseLong(strNow));
		
		try {
			if(bot == null){
				bot = TwitterBot.getInstance();
			}
			for (String keyTwitterAccount : keyTwitterAccounts) {
				bot.run(keyTwitterAccount, now);
			}
			
		} catch (ApiProxy.CapabilityDisabledException ignore) { 
			ignore.printStackTrace(); // ignore exception of maintenance
		} catch (GCacheException ignore) { 
			ignore.printStackTrace(); // ignore exception of maintenance
		} finally {
			log.info(String.format("AccountTask service finished. [total msec:%1$d] [delay msec:%2$d] [batch size:%3$d]",
					Long.valueOf(new Date().getTime() - start.getTime()),
					Long.valueOf(start.getTime() - now.getTime()),
					keyTwitterAccounts.length
					));
		}
    }
}
