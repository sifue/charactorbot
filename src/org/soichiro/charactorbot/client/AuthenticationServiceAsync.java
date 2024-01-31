/**
 * 
 */
package org.soichiro.charactorbot.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author soichiro
 *
 */
public interface AuthenticationServiceAsync {

	void getAuthenticatedUserEmail(AsyncCallback<CUser> callback);

	void getLoginURL(AsyncCallback<String> callback);

	void getLogoutURL(AsyncCallback<String> callback);

}
