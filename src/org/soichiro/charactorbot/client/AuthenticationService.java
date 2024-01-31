/**
 * 
 */
package org.soichiro.charactorbot.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *Google account authentication service.
 * @author soichiro
 * 
 */
@RemoteServiceRelativePath("authentication")
public interface AuthenticationService extends RemoteService {
	
	/**
	 * @return If you are not authenticated. return null.
	 */
	CUser getAuthenticatedUserEmail() throws CharactorbotRPCException;
	
	/**
	 * @return LoginURL
	 */
	String getLoginURL() throws CharactorbotRPCException;
	
	/**
	 * @return LogoutURL
	 */
	String getLogoutURL() throws CharactorbotRPCException;
	
}
