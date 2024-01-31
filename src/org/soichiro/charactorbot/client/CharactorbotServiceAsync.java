/**
 * 
 */
package org.soichiro.charactorbot.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author soichiro
 *
 */
public interface CharactorbotServiceAsync {
	
	void getNumberRemainingTwitterAccount(AsyncCallback<Integer> callback);

	void getMessageOnTopPage(AsyncCallback<String> callback);

	void getTwitterAccountList(CUser user,
			AsyncCallback<List<CTwitterAccount>> callback);

	void addTwitterAccount(CTwitterAccount account,
			AsyncCallback<CUser> callback);

	void deleteTwitterAccount(String twitterAccountKey,
			AsyncCallback<CUser> callback);

	void editTwitterAccount(CTwitterAccount account,
			AsyncCallback<CUser> callback);

	void getPostTypeWithDetail(String twitterAccountKey, PostTypeEnum type,
			AsyncCallback<CPostType> callback);

	void updatePostTypeWithDetail(CPostType postType,
			AsyncCallback<CUser> callback);

	void getAuthorizationURL(String consumerKey, String consumerSecret,
			AsyncCallback<String> callback);

	void getAccessToken(String consumerKey, String consumerSecret, String pin,
			AsyncCallback<CAccessToken> callback);

	void updatePostTypeWithKeyword(CPostType postType,
			AsyncCallback<CUser> callback);

	void updatePost(CKeyword cKeyword, AsyncCallback<CUser> callback);

	void deleteAllKeyword(String twitterAccountKey, PostTypeEnum type,
			AsyncCallback<CUser> callback);

	void getLogEntryList(String twitterAccountKey,
			AsyncCallback<List<CLogEntry>> callback);
}
