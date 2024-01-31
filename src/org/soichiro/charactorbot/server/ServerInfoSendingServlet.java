/**
 * 
 */
package org.soichiro.charactorbot.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Send Server information to centralServer
 * @author soichiro
 *
 */
public class ServerInfoSendingServlet extends HttpServlet {

	/** serialVersionUID */
	private static final long serialVersionUID = 7677730759422380293L;
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ServerProperties properties = ServerProperties.getInstance(pm);
		
		StringBuffer strUrl = new StringBuffer();
		
		strUrl.append("http://www.soichiro.org/charactorbot/log.php?");
		strUrl.append("application_id=");
		String id = System.getProperty("com.google.appengine.application.id");
		strUrl.append(id);
		
		strUrl.append("&");
		strUrl.append("limit_twitter_account=");
		Integer limitTwitterAccount = properties.getLimitTwitterAccount();
		strUrl.append(limitTwitterAccount.toString());
		
		strUrl.append("&");
		strUrl.append("number_remaining_twitter_account=");
		Integer numberRemainingTwitterAccount = properties.getNumberRemainingTwitterAccount();
		strUrl.append(numberRemainingTwitterAccount.toString());
		
		strUrl.append("&");
		strUrl.append("is_stop_create_bot=");
		Boolean isStopCreateBot = properties.getIsStopCreateBot();
		strUrl.append(isStopCreateBot.booleanValue() ? 1 : 0);
		
		strUrl.append("&");
		strUrl.append("version=1.7.0");
		
		resp.setContentType("text/html");
		try {
            URL url = new URL(strUrl.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
        		resp.getWriter().println(line);
            }
            reader.close();
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }
		
		resp.getWriter().close();
	}
}
