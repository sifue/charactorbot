/**
 * 
 */
package org.soichiro.charactorbot.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author soichiro
 *
 */
@RemoteServiceRelativePath("mainService")
public interface CharactorbotService extends RemoteService {
	
	Integer getNumberRemainingTwitterAccount() throws CharactorbotRPCException;
	
	String getMessageOnTopPage() throws CharactorbotRPCException;
	
	List<CTwitterAccount> getTwitterAccountList(CUser user) throws CharactorbotRPCException;
	
	CUser addTwitterAccount(CTwitterAccount account) throws CharactorbotRPCException;
	
	CUser editTwitterAccount(CTwitterAccount account) throws CharactorbotRPCException;
	
	CUser deleteTwitterAccount(String twitterAccountKey) throws CharactorbotRPCException;
	
	CPostType getPostTypeWithDetail(String twitterAccountKey, PostTypeEnum type) throws CharactorbotRPCException;
	
	CUser updatePostTypeWithDetail(CPostType postType) throws CharactorbotRPCException;
	
	CUser updatePostTypeWithKeyword(CPostType postType) throws CharactorbotRPCException;
	
	CUser updatePost(CKeyword cKeyword) throws CharactorbotRPCException;
	
	CUser deleteAllKeyword(String twitterAccountKey, PostTypeEnum type) throws CharactorbotRPCException;
	
	String getAuthorizationURL(String consumerKey, String consumerSecret) throws CharactorbotRPCException;
	
	CAccessToken getAccessToken(String consumerKey, String consumerSecret, String pin) throws CharactorbotRPCException;
	
	List<CLogEntry> getLogEntryList(String twitterAccountKey) throws CharactorbotRPCException;
}
