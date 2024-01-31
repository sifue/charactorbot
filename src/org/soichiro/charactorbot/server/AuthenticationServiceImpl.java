/**
 * 
 */
package org.soichiro.charactorbot.server;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.soichiro.charactorbot.client.AuthenticationService;
import org.soichiro.charactorbot.client.CUser;
import org.soichiro.charactorbot.client.CharactorbotRPCException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Google account authentication servlet.
 * @author soichiro
 */
public class AuthenticationServiceImpl extends RemoteServiceServlet implements
		AuthenticationService {
	
	private static final long serialVersionUID = 5821931531681861748L;
	
	@Override
	public CUser getAuthenticatedUserEmail() throws CharactorbotRPCException {
			try{
				UserService userService = UserServiceFactory.getUserService();
				User user = userService.getCurrentUser();
				return user != null ? new CUser(user.getEmail(), user.getAuthDomain()) : null;
			}catch (Exception e) {
				throw new CharactorbotRPCException(e);
			}
		}

	@Override
	public String getLoginURL() throws CharactorbotRPCException {
		try{
				UserService userService = UserServiceFactory.getUserService();
				
				String param = null;
				
				// Japanese is special. sorry.
				HttpServletRequest threadLocalRequest = getThreadLocalRequest();
				if(threadLocalRequest != null 
						&& (Locale.JAPANESE.equals(threadLocalRequest.getLocale())
								|| Locale.JAPAN.equals(threadLocalRequest.getLocale())))
				{
					param = "/?locale=ja";
				}else{
					param = "/";
				}
				
				return userService.createLoginURL(param);
		}catch (Exception e) {
			throw new CharactorbotRPCException(e);
		}
	}

	@Override
	public String getLogoutURL() throws CharactorbotRPCException {
		try{
				UserService userService = UserServiceFactory.getUserService();
				
				String param = null;
				// Japanese is special. sorry.
				HttpServletRequest threadLocalRequest = getThreadLocalRequest();
				if(threadLocalRequest != null 
						&& (Locale.JAPANESE.equals(threadLocalRequest.getLocale())
								|| Locale.JAPAN.equals(threadLocalRequest.getLocale())))
				{
					param = "/?locale=ja";
				}else{
					param = "/";
				}
				
				return userService.createLogoutURL(param);
		}catch (Exception e) {
			throw new CharactorbotRPCException(e);
		}
	}
	
}
