/**
 * 
 */
package org.soichiro.charactorbot.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.soichiro.charactorbot.client.CKeyword;
import org.soichiro.charactorbot.client.CPost;
import org.soichiro.charactorbot.client.CPostType;
import org.soichiro.charactorbot.client.CTwitterAccount;
import org.soichiro.charactorbot.client.CUser;
import org.soichiro.charactorbot.client.CharactorbotRPCException;
import org.soichiro.charactorbot.client.CharactorbotService;
import org.soichiro.charactorbot.client.PostTypeEnum;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.User;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author soichiro
 *
 */
public class TwitterBotTest {
	
    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig())
		    .setEnvIsAdmin(true)
		    .setEnvIsLoggedIn(true)
		    .setEnvEmail("charactorbot")
		    .setEnvAuthDomain("gmail.com");
    
    private final CharactorbotService servlet = new CharactorbotServiceImpl();
    
    @Before
    public void setUp() throws CharactorbotRPCException {
        helper.setUp();
        System.clearProperty(TwitterBot.LOCAL_HOUR_OF_DAY_KEY);
        System.clearProperty(TwitterBot.LOCAL_CALENDER_KEY);
    }

	@After
    public void tearDown() {
        helper.tearDown();
    }

    /**
     * normal post
     */
    @Test
    public void testNormalPost() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	         	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertEquals(testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    /**
     * normal post twice in interval 3min with edit.
     */
    @Test
    public void testNormalPostTwiceWithEdit() throws CharactorbotRPCException {
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        

    	final String testMessage = "test_post " + new Date().getTime();
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	         	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
       
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
        // test
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertEquals(testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);

    	final String testMessage2 = "test_post2 " + new Date().getTime();
    	
    	// post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
				break;
			default:
				break;
			}
        	
        	for (CKeyword cKeyword : listKeyword) {
        		List<CPost> listPost = cKeyword.getListPost();
        		listPost.clear(); // clear
				CPost post = new CPost();
        		post.setMessage(testMessage2);
        		listPost.add(post);
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
        
        // test
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertEquals(testMessage2, status);
				return new StatusMock();
    		}
    	};
    	
    	Date after1min = new Date(now.getTime() + 60000L);
    	bot.run(remoteAccount.getKey(), after1min);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * normal post twice with account activation.
     */
    @Test
    public void testNormalPostTwiceWithActivationChange() throws CharactorbotRPCException {
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        CUser user = new CUser("charactorbot","gmail.com");
		List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(user);
        
        CTwitterAccount remoteAccount = listAccount.get(0);
    	final String testMessage = "test_post " + new Date().getTime();
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3)); // 3min.
	        	
	         	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
       
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
        // test
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<String> listStatus = new ArrayList<String>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertEquals(testMessage, status);
				listStatus.add(status);
				return new StatusMock();
    		}
    	};
    	bot.run(remoteAccount.getKey(), now);
    	assertEquals(1, listStatus.size());
    	
    	// Deactivation
    	remoteAccount.setIsActivated(false);
    	servlet.editTwitterAccount(remoteAccount);
    	
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				fail();
				return new StatusMock();
    		}
    	};
    	Date after1min = new Date(now.getTime() + 60000L);
    	bot.run(remoteAccount.getKey(), after1min);
    	
    	// Activation
    	remoteAccount.setIsActivated(true);
    	servlet.editTwitterAccount(remoteAccount);

    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				fail();
				return new StatusMock();
    		}
    	};
    	Date after2min = new Date(after1min.getTime() + 60000L);
    	bot.run(remoteAccount.getKey(), after2min);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    
    /**
     * normal post twice in interval 3min.
     */
    @Test
    public void testNormalPostTwiceInInterval() throws CharactorbotRPCException {
    	
    	// Adds
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        

    	final String testMessage = "test_post " + new Date().getTime();
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
        // test
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertEquals(testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);

        // test
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				fail();
				return new StatusMock();
    		}
    	};
    	
    	Date after1min = new Date(now.getTime() + 60000L);
    	bot.run(remoteAccount.getKey(), after1min);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * normal post twice out of interval 3min.
     */
    @Test
    public void testNormalPostTwiceOutOfInterval() throws CharactorbotRPCException {
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        

    	final String testMessage = "test_post " + new Date().getTime();
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
        // test
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertEquals(testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);

        // test
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertEquals(testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	Date after4min = new Date(now.getTime() + 240000L);
    	bot.run(remoteAccount.getKey(), after4min);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * normal post with sleep 3:00
     */
    @Test
    public void testNormalPostWithSleepAt3() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	postType.setIsUseSleep(Boolean.TRUE); // sleep
        	
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				fail();
				return new StatusMock();
    		}
    	};
    	
    	// set local hour of day
    	System.setProperty(TwitterBot.LOCAL_HOUR_OF_DAY_KEY, "3");
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    /**
     * normal post with sleep 17:00
     */
    @Test
    public void testNormalPostWithSleepAt17() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	postType.setIsUseSleep(Boolean.TRUE); // sleep
        	
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
 
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertEquals(testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	// set local hour of day
    	System.setProperty(TwitterBot.LOCAL_HOUR_OF_DAY_KEY, "17");
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    
    /**
     * normal post
     */
    @Test
    public void testNormalPostDateTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #date# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertNotNull(status);
				assertTrue(status.contains("ŒŽ"));
				assertTrue(status.contains("“ú"));
				assertFalse(status.contains("#date#"));
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * normal post time tag
     */
    @Test
    public void testNormalPostTimeTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #time# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertNotNull(status);
				assertTrue(status.contains("Žž"));
				assertTrue(status.contains("•ª"));
				assertFalse(status.contains("#time#"));
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * normal post time tag
     */
    @Test
    public void testNormalPostBrTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #br# new line" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertNotNull(status);
				assertTrue(status.contains("\n"));
				assertFalse(status.contains("#br#"));
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * normal post
     */
    @Test
    public void testNormalPostStopTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #stop# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}

				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				fail();
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * normal post
     */
    @Test
    public void testNormalPostWeekTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				String testMessage = testMessage1.replaceAll("#week_1#", "");
				assertEquals(testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	// Sunday 10:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108071010"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * normal post
     */
    @Test
    public void testNormalPostHourTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				String testMessage = testMessage3.replaceAll("#hour_5#", "");
				assertEquals(testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	// Wednesday 05:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108100510"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * normal post
     */
    @Test
    public void testNormalPostHourTagAndWeekTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				String testMessage_week_1 = testMessage1.replaceAll("#week_1#", "");
				String testMessage_hour_5 = testMessage3.replaceAll("#hour_5#", "");
				
				assertTrue(status.equals(testMessage_week_1)
						|| status.equals(testMessage_hour_5));
				return new StatusMock();
    		}
    	};
    	
    	// Sunday 05:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108070510"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * normal post
     */
    @Test
    public void testNormalPostWeekHourTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	final String testMessage5 = "test_post5 #week_4_hour_11#" + new Date().getTime();
    	final String testMessage6 = "test_post5 #week_5_hour_13#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case NOMAL_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	         	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage5);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage6);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				String testMessage = testMessage5.replaceAll("#week_4_hour_11#", "");
				assertEquals(testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	// Wednesday 11:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108101110"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    /**
     * reply for me post
     */
    @Test
    public void testReplyForMePost() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				for (int i = 0; i < 10; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						// before 1 min. 
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword" + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user" + index;
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				assertEquals("@user1 " + testMessage, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for me post with Regex
     */
    @Test
    public void testReplyForMePostRegex() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword(".*key.*");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.TRUE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				assertEquals("@user0 " + testMessage, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for me post with keyword activation
     */
    @Test
    public void testReplyForMePostKeywordNotActivated() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.FALSE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				fail();
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for me post. not match
     */
    @Test
    public void testReplyForMePostNotMatch() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1-not match");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 10; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						// before 1 min. 
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword" + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user" + index;
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				fail();
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for me post by order
     */
    @Test
    public void testReplyForMePostByOrder() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
				postType.setInterval(Integer.valueOf(3));
				
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	
	        	keyword = new CKeyword();
	    		keyword.setKeyword("keyword2");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage + " " + cKeyword.getKeyword());
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						// before 1 min. 
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 keyword2 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user" + index;
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				assertEquals("@user0 " + testMessage + " keyword1", latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for me post in interval 1 min.
     */
    @Test
    public void testReplyForMePostInInterval1min() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(1)); // interval 1 min.
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<StatusUpdate> listStatusUpdate = new ArrayList<StatusUpdate>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 10; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						// 0 - 9 min.
						Date createdAt = new Date(now.getTime() - (60000L * index)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + createdAt.toString();
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user" + index;
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				listStatusUpdate.add(latestStatus);
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(1, listStatusUpdate.size());
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for me post in interval 3 min.
     */
    @Test
    public void testReplyForMePostInInterval3min() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3)); // interval 3 min.
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<StatusUpdate> listStatusUpdate = new ArrayList<StatusUpdate>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 10; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						// 0 - 9 min.
						Date createdAt = new Date(now.getTime() - (60000L * index)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + createdAt.toString();
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user" + index;
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				listStatusUpdate.add(latestStatus);
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(3, listStatusUpdate.size());
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for me post with ignore id
     */
    @Test
    public void testReplyForMePostWithIgnoreId() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	postType.setIgnoredIDs("user3, user4");
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<StatusUpdate> listStatusUpdate = new ArrayList<StatusUpdate>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 10; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						// before 1 or 9 min. ( last index is 9 min. because to stop paging)
						Date createdAt = new Date(now.getTime() - (60000L * (index== 9 ? 9 :1 ))); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user" + index;
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				listStatusUpdate.add(latestStatus);
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(7, listStatusUpdate.size());
    	assertEquals("@user0 " + testMessage, listStatusUpdate.get(0).getStatus());
    	assertEquals("@user1 " + testMessage, listStatusUpdate.get(1).getStatus());
    	assertEquals("@user2 " + testMessage, listStatusUpdate.get(2).getStatus());
    	assertEquals("@user5 " + testMessage, listStatusUpdate.get(3).getStatus());
    	assertEquals("@user6 " + testMessage, listStatusUpdate.get(4).getStatus());
    	assertEquals("@user7 " + testMessage, listStatusUpdate.get(5).getStatus());
    	assertEquals("@user8 " + testMessage, listStatusUpdate.get(6).getStatus());
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for me post with date tag
     */
    @Test
    public void testReplyForMePostDateTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #date# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				assertTrue(latestStatus.getStatus().contains("ŒŽ"));
				assertTrue(latestStatus.getStatus().contains("“ú"));
				assertFalse(latestStatus.getStatus().contains("#date#"));
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    /**
     * reply for me post with time tag
     */
    @Test
    public void testReplyForMePostTimeTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #time# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				assertTrue(latestStatus.getStatus().contains("Žž"));
				assertTrue(latestStatus.getStatus().contains("•ª"));
				assertFalse(latestStatus.getStatus().contains("#time#"));
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    /**
     * reply for me post with br tag
     */
    @Test
    public void testReplyForMePostBrTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #br# new line" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				assertTrue(latestStatus.getStatus().contains("\n"));
				assertFalse(latestStatus.getStatus().contains("#br#"));
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for me post with user_name tag
     */
    @Test
    public void testReplyForMePostUserNameTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #user_name# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_1111");
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getName() {
									return "TestBot_1111";
								};
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String message = testMessage.replaceAll("#user_name#", "TestBot_1111");
				assertEquals("@user0 " + message, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    /**
     * reply for me post with activation
     */
    @Test
    public void testReplyForMePostWithActivationChange() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_1111");
    	account.setScreenName("sifue_4466");
    	account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
    	account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
    	account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
    	account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
    	account.setTimeZoneId("Asia/Tokyo");
    	account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
    	List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
    	
    	CTwitterAccount remoteAccount = listAccount.get(0);
    	final Date now = new Date();
    	
    	// Deactivation
    	remoteAccount.setIsActivated(false);
    	servlet.editTwitterAccount(remoteAccount);
    	
    	// Activation
    	remoteAccount.setIsActivated(true);
    	servlet.editTwitterAccount(remoteAccount);
    	
    	// post type check and create
    	for (PostTypeEnum type : PostTypeEnum.values()) {
    		
    		CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
    		List<CKeyword> listKeyword = postType.getListKeyword();
    		switch (type) {
    		case REPLY_FOR_ME:
    			CKeyword keyword = new CKeyword();
    			keyword.setKeyword("keyword1");
    			keyword.setIsActivated(Boolean.TRUE);
    			keyword.setIsRegex(Boolean.FALSE);
    			listKeyword.add(keyword);
    			postType.setInterval(Integer.valueOf(3));
    			
    			for (CKeyword cKeyword : listKeyword) {
    				List<CPost> listPost = cKeyword.getListPost();
    				CPost post = new CPost();
    				post.setMessage(testMessage);
    				listPost.add(post);
    			}
    			break;
    		default:
    			break;
    		}
    		
    		// update
    		servlet.updatePostTypeWithDetail(postType);
    	}
    	
    	
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
    		private static final long serialVersionUID = 7907704279643794875L;
    		int pageCount = 0;
    		@Override
    		public ResponseList<Status> getHomeTimeline(Paging paging)
    				throws TwitterException {
    			ResponseList<Status> list = new ResponseListMock();
    			pageCount++;
    			if(pageCount > 1) return list;
    			for (int i = 0; i < 1; i++) {
    				final int index = i;
    				list.add(new StatusMock(){
    					Date createdAt = new Date(now.getTime() - (60000L * 1)); // 1min. ago
    					private static final long serialVersionUID = 1740213806003030940L;
    					
    					@Override
    					public String getText() {
    						return "@sifue_4466 keyword1 " + index;
    					};
    					
    					@Override
    					public Date getCreatedAt() {
    						return createdAt;
    					};
    					
    					@Override
    					public twitter4j.User getUser() {
    						return new UserMock(){
    							private static final long serialVersionUID = 6090402254015396494L;
    							@Override
    							public String getName() {
    								return "TestBot_1111";
    							};
    							@Override
    							public String getScreenName() {
    								return "user0";
    							};
    						};
    					};
    				});
    			}
    			return list;
    		}
    		
    		@Override
    		public Status updateStatus(StatusUpdate latestStatus)
    				throws TwitterException {
    			fail();
    			return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    /**
     * reply for me post with stop tag
     */
    @Test
    public void testReplyForMePostStopTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #stop# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				fail();
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    /**
     * reply for me post with week tag
     */
    @Test
    public void testReplyForMePostWeekTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String testMessage = testMessage1.replaceAll("#week_1#", "");
				assertEquals("@user0 " + testMessage, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	// Sunday 10:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108071010"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    

    /**
     * reply for me post with hour tag
     */
    @Test
    public void testReplyForMePostHourTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String testMessage = testMessage3.replaceAll("#hour_5#", "");
				assertEquals("@user0 " + testMessage, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	// Wednesday 05:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108100510"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    

    /**
     * reply for me post with hour tag and week tag
     */
    @Test
    public void testReplyForMePostHourTagAndWeekTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String testMessage_week_1 = testMessage1.replaceAll("#week_1#", "");
				String testMessage_hour_5 = testMessage3.replaceAll("#hour_5#", "");
				
				assertTrue(latestStatus.getStatus().equals("@user0 " + testMessage_week_1)
						|| latestStatus.getStatus().equals("@user0 " + testMessage_hour_5));
				
				return new StatusMock();
			}
    	};
    	
    	// Sunday 05:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108070510"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for me post with week_hour tag
     */
    @Test
    public void testReplyForMePostWeekHourTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	final String testMessage5 = "test_post5 #week_4_hour_11#" + new Date().getTime();
    	final String testMessage6 = "test_post5 #week_5_hour_13#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage5);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage6);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String testMessage = testMessage5.replaceAll("#week_4_hour_11#", "");
				assertEquals("@user0 " + testMessage, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	// Wednesday 11:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108101110"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for me post with group tag 0
     */
    @Test
    public void testReplyForMePostGroupTag0() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #group_0# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword(".*(key)(wo)(rd1).*");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.TRUE); // TRUE
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String message = testMessage.replaceAll("#group_0#", "@sifue_4466 keyword1 0");
				assertEquals("@user0 " + message, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for me post with group tag 1
     */
    @Test
    public void testReplyForMePostGroupTag1() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #group_1# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword(".*(key)(wo)(rd1).*");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.TRUE); // TRUE
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String message = testMessage.replaceAll("#group_1#", "key");
				assertEquals("@user0 " + message, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for me post with group tag 2
     */
    @Test
    public void testReplyForMePostGroupTag2() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #group_2# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword(".*(key)(wo)(rd1).*");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.TRUE); // TRUE
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String message = testMessage.replaceAll("#group_2#", "wo");
				assertEquals("@user0 " + message, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for me post with favorite tag
     */
    @Test
    public void testReplyForMeFavoriteTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #favorite# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final boolean[] isFavoriteArray = new boolean[1];
    	isFavoriteArray[0] = false;
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status createFavorite(long id) throws TwitterException {
				isFavoriteArray[0] = true;
				return super.createFavorite(id);
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String message = testMessage.replaceAll("#favorite#", "");
				assertEquals("@user0 " + message, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertTrue(isFavoriteArray[0]);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post
     */
    @Test
    public void testReplyPost() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				for (int i = 0; i < 10; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						// before 1 min. 
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword" + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user" + index;
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				assertEquals("@user1 " + testMessage, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    /**
     * reply for timeline post with Regex
     */
    @Test
    public void testReplyPostRegex() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword(".*key.*");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.TRUE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				assertEquals("@user0 " + testMessage, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post with keyword activation
     */
    @Test
    public void testReplyPostKeywordNotActivated() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.FALSE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				fail();
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post. not match
     */
    @Test
    public void testReplyPostNotMatch() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1-not match");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 10; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						// before 1 min. 
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword" + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user" + index;
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				fail();
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post by order
     */
    @Test
    public void testReplyPostByOrder() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
				postType.setInterval(Integer.valueOf(3));
				
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	
	        	keyword = new CKeyword();
	    		keyword.setKeyword("keyword2");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage + " " + cKeyword.getKeyword());
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						// before 1 min. 
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 keyword2 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user" + index;
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				assertEquals("@user0 " + testMessage + " keyword1", latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post in interval 1 min.
     */
    @Test
    public void testReplyPostInInterval1min() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(1)); // interval 1 min.
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<StatusUpdate> listStatusUpdate = new ArrayList<StatusUpdate>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 10; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						// 0 - 9 min.
						Date createdAt = new Date(now.getTime() - (60000L * index)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + createdAt.toString();
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user" + index;
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				listStatusUpdate.add(latestStatus);
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(1, listStatusUpdate.size());
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post in interval 3 min.
     */
    @Test
    public void testReplyPostInInterval3min() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3)); // interval 3 min.
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<StatusUpdate> listStatusUpdate = new ArrayList<StatusUpdate>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 10; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						// 0 - 9 min.
						Date createdAt = new Date(now.getTime() - (60000L * index)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + createdAt.toString();
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user" + index;
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				listStatusUpdate.add(latestStatus);
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(3, listStatusUpdate.size());
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post with ignore id
     */
    @Test
    public void testReplyPostWithIgnoreId() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	postType.setIgnoredIDs("user3, user4");
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<StatusUpdate> listStatusUpdate = new ArrayList<StatusUpdate>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 10; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						// before 1 or 9 min. ( last index is 9 min. because to stop paging)
						Date createdAt = new Date(now.getTime() - (60000L * (index== 9 ? 9 :1 ))); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user" + index;
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				listStatusUpdate.add(latestStatus);
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(7, listStatusUpdate.size());
    	assertEquals("@user0 " + testMessage, listStatusUpdate.get(0).getStatus());
    	assertEquals("@user1 " + testMessage, listStatusUpdate.get(1).getStatus());
    	assertEquals("@user2 " + testMessage, listStatusUpdate.get(2).getStatus());
    	assertEquals("@user5 " + testMessage, listStatusUpdate.get(3).getStatus());
    	assertEquals("@user6 " + testMessage, listStatusUpdate.get(4).getStatus());
    	assertEquals("@user7 " + testMessage, listStatusUpdate.get(5).getStatus());
    	assertEquals("@user8 " + testMessage, listStatusUpdate.get(6).getStatus());
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post with date tag
     */
    @Test
    public void testReplyPostDateTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #date# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				assertTrue(latestStatus.getStatus().contains("ŒŽ"));
				assertTrue(latestStatus.getStatus().contains("“ú"));
				assertFalse(latestStatus.getStatus().contains("#date#"));
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    /**
     * reply for timeline post with time tag
     */
    @Test
    public void testReplyPostTimeTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #time# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				assertTrue(latestStatus.getStatus().contains("Žž"));
				assertTrue(latestStatus.getStatus().contains("•ª"));
				assertFalse(latestStatus.getStatus().contains("#time#"));
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post with br tag
     */
    @Test
    public void testReplyPostBrTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #br# new line" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				assertTrue(latestStatus.getStatus().contains("\n"));
				assertFalse(latestStatus.getStatus().contains("#br#"));
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post with user_name tag
     */
    @Test
    public void testReplyPostUserNameTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #user_name# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_1111");
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getName() {
									return "TestBot_1111";
								};
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String message = testMessage.replaceAll("#user_name#", "TestBot_1111");
				assertEquals("@user0 " + message, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    

    /**
     * reply for timeline post with activation
     */
    @Test
    public void testReplyPostWithActivationChange() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_1111");
    	account.setScreenName("sifue_4466");
    	account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
    	account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
    	account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
    	account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
    	account.setTimeZoneId("Asia/Tokyo");
    	account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
    	List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
    	
    	CTwitterAccount remoteAccount = listAccount.get(0);
    	final Date now = new Date();
    	
    	// Deactivation
    	remoteAccount.setIsActivated(false);
    	servlet.editTwitterAccount(remoteAccount);
    	
    	// Activation
    	remoteAccount.setIsActivated(true);
    	servlet.editTwitterAccount(remoteAccount);
    	
    	// post type check and create
    	for (PostTypeEnum type : PostTypeEnum.values()) {
    		
    		CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
    		List<CKeyword> listKeyword = postType.getListKeyword();
    		switch (type) {
    		case REPLY:
    			CKeyword keyword = new CKeyword();
    			keyword.setKeyword("keyword1");
    			keyword.setIsActivated(Boolean.TRUE);
    			keyword.setIsRegex(Boolean.FALSE);
    			listKeyword.add(keyword);
    			postType.setInterval(Integer.valueOf(3));
    			
    			for (CKeyword cKeyword : listKeyword) {
    				List<CPost> listPost = cKeyword.getListPost();
    				CPost post = new CPost();
    				post.setMessage(testMessage);
    				listPost.add(post);
    			}
    			break;
    		default:
    			break;
    		}
    		
    		// update
    		servlet.updatePostTypeWithDetail(postType);
    	}
    	
    	
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
    		private static final long serialVersionUID = 7907704279643794875L;
    		int pageCount = 0;
    		@Override
    		public ResponseList<Status> getHomeTimeline(Paging paging)
    				throws TwitterException {
    			ResponseList<Status> list = new ResponseListMock();
    			pageCount++;
    			if(pageCount > 1) return list;
    			for (int i = 0; i < 1; i++) {
    				final int index = i;
    				list.add(new StatusMock(){
    					Date createdAt = new Date(now.getTime() - (60000L * 1)); // 1min. ago
    					private static final long serialVersionUID = 1740213806003030940L;
    					
    					@Override
    					public String getText() {
    						return "@sifue_4466 keyword1 " + index;
    					};
    					
    					@Override
    					public Date getCreatedAt() {
    						return createdAt;
    					};
    					
    					@Override
    					public twitter4j.User getUser() {
    						return new UserMock(){
    							private static final long serialVersionUID = 6090402254015396494L;
    							@Override
    							public String getName() {
    								return "TestBot_1111";
    							};
    							@Override
    							public String getScreenName() {
    								return "user0";
    							};
    						};
    					};
    				});
    			}
    			return list;
    		}
    		
    		@Override
    		public Status updateStatus(StatusUpdate latestStatus)
    				throws TwitterException {
    			fail();
    			return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post with stop tag
     */
    @Test
    public void testReplyPostStopTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #stop# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				fail();
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    /**
     * reply for timeline post with week tag
     */
    @Test
    public void testReplyPostWeekTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String testMessage = testMessage1.replaceAll("#week_1#", "");
				assertEquals("@user0 " + testMessage, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	// Sunday 10:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108071010"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    

    /**
     * reply for timeline post with hour tag
     */
    @Test
    public void testReplyPostHourTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String testMessage = testMessage3.replaceAll("#hour_5#", "");
				assertEquals("@user0 " + testMessage, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	// Wednesday 05:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108100510"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    

    /**
     * reply for timeline post with hour tag and week tag
     */
    @Test
    public void testReplyPostHourTagAndWeekTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String testMessage_week_1 = testMessage1.replaceAll("#week_1#", "");
				String testMessage_hour_5 = testMessage3.replaceAll("#hour_5#", "");
				
				assertTrue(latestStatus.getStatus().equals("@user0 " + testMessage_week_1)
						|| latestStatus.getStatus().equals("@user0 " + testMessage_hour_5));
				
				return new StatusMock();
			}
    	};
    	
    	// Sunday 05:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108070510"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post with week_hour tag
     */
    @Test
    public void testReplyPostWeekHourTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	final String testMessage5 = "test_post5 #week_4_hour_11#" + new Date().getTime();
    	final String testMessage6 = "test_post5 #week_5_hour_13#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage5);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage6);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String testMessage = testMessage5.replaceAll("#week_4_hour_11#", "");
				assertEquals("@user0 " + testMessage, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	// Wednesday 11:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108101110"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post with group tag 0
     */
    @Test
    public void testReplyPostGroupTag0() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #group_0# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword(".*(key)(wo)(rd1).*");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.TRUE); // TRUE
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String message = testMessage.replaceAll("#group_0#", "@sifue_4466 keyword1 0");
				assertEquals("@user0 " + message, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post with group tag 1
     */
    @Test
    public void testReplyPostGroupTag1() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #group_1# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword(".*(key)(wo)(rd1).*");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.TRUE); // TRUE
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String message = testMessage.replaceAll("#group_1#", "key");
				assertEquals("@user0 " + message, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post with group tag 2
     */
    @Test
    public void testReplyPostGroupTag2() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #group_2# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword(".*(key)(wo)(rd1).*");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.TRUE); // TRUE
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String message = testMessage.replaceAll("#group_2#", "wo");
				assertEquals("@user0 " + message, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * reply for timeline post with favorite tag
     */
    @Test
    public void testReplyFavoriteTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #favorite# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final boolean[] isFavoriteArray = new boolean[1];
    	isFavoriteArray[0] = false;
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user0";
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status createFavorite(long id) throws TwitterException {
				isFavoriteArray[0] = true;
				return super.createFavorite(id);
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				String message = testMessage.replaceAll("#favorite#", "");
				assertEquals("@user0 " + message, latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertTrue(isFavoriteArray[0]);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    

    /**
     * reply for me and reply for timeline. prior to reply for me.
     */
    @Test
    public void testReplyForMePostAndReplyPost() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post reply_for_me " + new Date().getTime();
    	final String testMessage2 = "test_post reply_for_timeline " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY_FOR_ME:
			case REPLY:
				postType.setInterval(Integer.valueOf(3));
				
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("keyword1");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
					if(PostTypeEnum.REPLY_FOR_ME.equals(type)){
						post.setMessage(testMessage1 + " " + cKeyword.getKeyword());
					} else if (PostTypeEnum.REPLY.equals(type)) {
						post.setMessage(testMessage2 + " " + cKeyword.getKeyword());
					}
	        		listPost.add(post);
				}
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			int pageCount = 0;
			@Override
			public ResponseList<Status> getHomeTimeline(Paging paging)
					throws TwitterException {
				ResponseList<Status> list = new ResponseListMock();
				pageCount++;
				if(pageCount > 1) return list;
				for (int i = 0; i < 1; i++) {
					final int index = i;
					final Date now = new Date();
					list.add(new StatusMock(){
						// before 1 min. 
						Date createdAt = new Date(now.getTime() - (60000L * 1)); 
						private static final long serialVersionUID = 1740213806003030940L;
						
						@Override
						public String getText() {
							return "@sifue_4466 keyword1 keyword2 " + index;
						};
						
						@Override
						public Date getCreatedAt() {
							return createdAt;
						};
						
						@Override
						public twitter4j.User getUser() {
							return new UserMock(){
								private static final long serialVersionUID = 6090402254015396494L;
								@Override
								public String getScreenName() {
									return "user" + index;
								};
							};
						};
					});
				}
				return list;
			}
			
			@Override
			public Status updateStatus(StatusUpdate latestStatus)
					throws TwitterException {
				assertEquals("@user0 " + testMessage1 + " keyword1", latestStatus.getStatus());
				return new StatusMock();
			}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    

    /**
     * welcome post
     */
    @Test
    public void testWelcomePost() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	         	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<Long> listFriendship = new ArrayList<Long>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				listFriendship.add(userId);
				return new UserMock(){
					private static final long serialVersionUID = 7907704279643794875L;
					@Override
					public String getScreenName() {
						return "user1";
					}
				};
			}
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertEquals("@user1 " + testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(1, listFriendship.size());
    	assertEquals(Long.valueOf(2L), listFriendship.get(0));
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * welcome post If post is nothing. not create friendship.
     */
    @Test
    public void testWelcomePostWithoutPost() throws CharactorbotRPCException {
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				fail();
				return new UserMock();
			}
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				fail();
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    /**
     * welcome post twice in interval 3min with edit.
     */
    @Test
    public void testWelcomePostTwiceWithEdit() throws CharactorbotRPCException {
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        

    	final String testMessage = "test_post " + new Date().getTime();
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	         	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
       
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
        // test
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<Long> listFriendship = new ArrayList<Long>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				listFriendship.add(userId);
				return new UserMock(){
					private static final long serialVersionUID = 7907704279643794875L;
					@Override
					public String getScreenName() {
						return "user1";
					}
				};
			}
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertEquals("@user1 " + testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(1, listFriendship.size());
    	assertEquals(Long.valueOf(2L), listFriendship.get(0));

    	final String testMessage2 = "test_post2 " + new Date().getTime();
    	
    	// post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
				break;
			default:
				break;
			}
        	
        	for (CKeyword cKeyword : listKeyword) {
        		List<CPost> listPost = cKeyword.getListPost();
        		listPost.clear(); // clear
				CPost post = new CPost();
        		post.setMessage(testMessage2);
        		listPost.add(post);
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
        
        // test
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertEquals(testMessage2, status);
				return new StatusMock();
    		}
    	};
    	
    	Date after1min = new Date(now.getTime() + 60000L);
    	bot.run(remoteAccount.getKey(), after1min);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * welcome post twice with account activation.
     */
    @Test
    public void testWelcomePostTwiceWithActivationChange() throws CharactorbotRPCException {
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        CUser user = new CUser("charactorbot","gmail.com");
		List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(user);
        
        CTwitterAccount remoteAccount = listAccount.get(0);
    	final String testMessage = "test_post " + new Date().getTime();
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3)); // 3min.
	        	
	         	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
       
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
        // test
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<Long> listFriendship = new ArrayList<Long>();
    	final List<String> listStatus = new ArrayList<String>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				listFriendship.add(userId);
				return new UserMock(){
					private static final long serialVersionUID = 7907704279643794875L;
					@Override
					public String getScreenName() {
						return "user1";
					}
				};
			}
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				listStatus.add(status);
				assertEquals("@user1 " + testMessage, status);
				return new StatusMock();
    		}
    	};
    	bot.run(remoteAccount.getKey(), now);
    	assertEquals(1, listStatus.size());
    	
    	assertEquals(1, listFriendship.size());
    	assertEquals(Long.valueOf(2L), listFriendship.get(0));
    	
    	// Deactivation
    	remoteAccount.setIsActivated(false);
    	servlet.editTwitterAccount(remoteAccount);
    	
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				fail();
				return new StatusMock();
    		}
    	};
    	Date after1min = new Date(now.getTime() + 60000L);
    	bot.run(remoteAccount.getKey(), after1min);
    	
    	// Activation
    	remoteAccount.setIsActivated(true);
    	servlet.editTwitterAccount(remoteAccount);

    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				fail();
				return new StatusMock();
    		}
    	};
    	Date after2min = new Date(after1min.getTime() + 60000L);
    	bot.run(remoteAccount.getKey(), after2min);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * welcome post twice in interval 3min.
     */
    @Test
    public void testWelcomePostTwiceInInterval() throws CharactorbotRPCException {
    	
    	// Adds
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        

    	final String testMessage = "test_post " + new Date().getTime();
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
        // test
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<Long> listFriendship = new ArrayList<Long>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				listFriendship.add(userId);
				return new UserMock(){
					private static final long serialVersionUID = 7907704279643794875L;
					@Override
					public String getScreenName() {
						return "user1";
					}
				};
			}
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertEquals("@user1 " + testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
   
    	assertEquals(1, listFriendship.size());
    	assertEquals(Long.valueOf(2L), listFriendship.get(0));

        // test
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				fail();
				return new StatusMock();
    		}
    	};
    	
    	Date after1min = new Date(now.getTime() + 60000L);
    	bot.run(remoteAccount.getKey(), after1min);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * welcome post twice out of interval 3min.
     */
    @Test
    public void testWelcomePostTwiceOutOfInterval() throws CharactorbotRPCException {
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        

    	final String testMessage = "test_post " + new Date().getTime();
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
        // test
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<Long> listFriendship = new ArrayList<Long>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				listFriendship.add(userId);
				return new UserMock(){
					private static final long serialVersionUID = 7907704279643794875L;
					@Override
					public String getScreenName() {
						return "user1";
					}
				};
			}
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertEquals("@user1 " + testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(1, listFriendship.size());
    	assertEquals(Long.valueOf(2L), listFriendship.get(0));
        // test
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertEquals(testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	Date after4min = new Date(now.getTime() + 240000L);
    	bot.run(remoteAccount.getKey(), after4min);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * welcome post with sleep 3:00
     */
    @Test
    public void testWelcomePostWithSleepAt3() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	postType.setIsUseSleep(Boolean.TRUE); // sleep
        	
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				fail();
				return new UserMock();
			}
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				fail();
				return new StatusMock();
    		}
    	};
    	
    	// set local hour of day
    	System.setProperty(TwitterBot.LOCAL_HOUR_OF_DAY_KEY, "3");
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    /**
     * welcome post with sleep 17:00
     */
    @Test
    public void testWelcomePostWithSleepAt17() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	postType.setIsUseSleep(Boolean.TRUE); // sleep
        	
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
 
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<Long> listFriendship = new ArrayList<Long>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				listFriendship.add(userId);
				return new UserMock(){
					private static final long serialVersionUID = 7907704279643794875L;
					@Override
					public String getScreenName() {
						return "user1";
					}
				};
			}
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertEquals("@user1 " + testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	// set local hour of day
    	System.setProperty(TwitterBot.LOCAL_HOUR_OF_DAY_KEY, "17");
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(1, listFriendship.size());
    	assertEquals(Long.valueOf(2L), listFriendship.get(0));
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    
    
    /**
     * welcome post
     */
    @Test
    public void testWelcomePostDateTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #date# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	           	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<Long> listFriendship = new ArrayList<Long>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				listFriendship.add(userId);
				return new UserMock(){
					private static final long serialVersionUID = 7907704279643794875L;
					@Override
					public String getScreenName() {
						return "user1";
					}
				};
			}
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertNotNull(status);
				assertTrue(status.contains("ŒŽ"));
				assertTrue(status.contains("“ú"));
				assertFalse(status.contains("#date#"));
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(1, listFriendship.size());
    	assertEquals(Long.valueOf(2L), listFriendship.get(0));
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * welcome post
     */
    @Test
    public void testWelcomePostTimeTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #time# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<Long> listFriendship = new ArrayList<Long>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				listFriendship.add(userId);
				return new UserMock(){
					private static final long serialVersionUID = 7907704279643794875L;
					@Override
					public String getScreenName() {
						return "user1";
					}
				};
			}
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				assertNotNull(status);
				assertTrue(status.contains("Žž"));
				assertTrue(status.contains("•ª"));
				assertFalse(status.contains("#time#"));
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(1, listFriendship.size());
    	assertEquals(Long.valueOf(2L), listFriendship.get(0));
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * welcome post
     */
    @Test
    public void testWelcomeUserNameTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #user_name# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<Long> listFriendship = new ArrayList<Long>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				listFriendship.add(userId);
				return new UserMock(){
					private static final long serialVersionUID = 7907704279643794875L;
					@Override
					public String getScreenName() {
						return "user1";
					}
					@Override
					public String getName() {
						return "user1";
					}
				};
			}
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				String message = testMessage.replaceAll("#user_name#", "user1");
				assertEquals("@user1 " + message, status);
				assertFalse(status.contains("#time#"));
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(1, listFriendship.size());
    	assertEquals(Long.valueOf(2L), listFriendship.get(0));
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * welcome post If use stop tag. create friendship.
     */
    @Test
    public void testWelcomePostStopTag() throws CharactorbotRPCException {
    	
    	final String testMessage = "test_post #stop# " + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage);
	        		listPost.add(post);
				}

				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<Long> listFriendship = new ArrayList<Long>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				listFriendship.add(userId);
				return new UserMock(){
					private static final long serialVersionUID = 7907704279643794875L;
					@Override
					public String getScreenName() {
						return "user1";
					}
				};
			}

			@Override
    		public Status updateStatus(String status) throws TwitterException {
				fail();
				return new StatusMock();
    		}
    	};
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(1, listFriendship.size());
    	assertEquals(Long.valueOf(2L), listFriendship.get(0));
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * welcome post
     */
    @Test
    public void testWelcomePostWeekTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<Long> listFriendship = new ArrayList<Long>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				listFriendship.add(userId);
				return new UserMock(){
					private static final long serialVersionUID = 7907704279643794875L;
					@Override
					public String getScreenName() {
						return "user1";
					}
				};
			}
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				String testMessage = testMessage1.replaceAll("#week_1#", "");
				assertEquals("@user1 " + testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	// Sunday 10:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108071010"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(1, listFriendship.size());
    	assertEquals(Long.valueOf(2L), listFriendship.get(0));
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * welcome post
     */
    @Test
    public void testWelcomePostHourTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<Long> listFriendship = new ArrayList<Long>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				listFriendship.add(userId);
				return new UserMock(){
					private static final long serialVersionUID = 7907704279643794875L;
					@Override
					public String getScreenName() {
						return "user1";
					}
				};
			}
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				String testMessage = testMessage3.replaceAll("#hour_5#", "");
				assertEquals("@user1 " + testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	// Wednesday 05:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108100510"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(1, listFriendship.size());
    	assertEquals(Long.valueOf(2L), listFriendship.get(0));
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * welcome post
     */
    @Test
    public void testWelcomePostHourTagAndWeekTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	        	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<Long> listFriendship = new ArrayList<Long>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				listFriendship.add(userId);
				return new UserMock(){
					private static final long serialVersionUID = 7907704279643794875L;
					@Override
					public String getScreenName() {
						return "user1";
					}
				};
			}
			@Override
    		public Status updateStatus(String status) throws TwitterException {
				String testMessage_week_1 = testMessage1.replaceAll("#week_1#", "");
				String testMessage_hour_5 = testMessage3.replaceAll("#hour_5#", "");
				
				assertTrue(status.equals("@user1 " + testMessage_week_1)
						|| status.equals("@user1 " + testMessage_hour_5));
				return new StatusMock();
    		}
    	};
    	
    	// Sunday 05:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108070510"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(1, listFriendship.size());
    	assertEquals(Long.valueOf(2L), listFriendship.get(0));
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * welcome post
     */
    @Test
    public void testWelcomePostWeekHourTag() throws CharactorbotRPCException {
    	
    	final String testMessage1 = "test_post1 #week_1#" + new Date().getTime();
    	final String testMessage2 = "test_post2 #week_2#" + new Date().getTime();
    	final String testMessage3 = "test_post3 #hour_5#" + new Date().getTime();
    	final String testMessage4 = "test_post4 #hour_17#" + new Date().getTime();
    	final String testMessage5 = "test_post5 #week_4_hour_11#" + new Date().getTime();
    	final String testMessage6 = "test_post5 #week_5_hour_13#" + new Date().getTime();
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        // post type check and create
        for (PostTypeEnum type : PostTypeEnum.values()) {
			
        	CPostType postType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case WELCOME_POST:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("null");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	postType.setInterval(Integer.valueOf(3));
	        	
	         	for (CKeyword cKeyword : listKeyword) {
	        		List<CPost> listPost = cKeyword.getListPost();
					CPost post = new CPost();
	        		post.setMessage(testMessage1);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage2);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage3);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage4);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage5);
	        		listPost.add(post);
	        		post = new CPost();
	        		post.setMessage(testMessage6);
	        		listPost.add(post);
				}
	        	
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        }
    	
    	Date now = new Date();
    	
    	TwitterBot bot = (TwitterBot)TwitterBot.getInstance();
    	final List<Long> listFriendship = new ArrayList<Long>();
    	bot.twitterMock = new TwitterMock(){
			private static final long serialVersionUID = 7907704279643794875L;
			@Override
			public User createFriendship(long userId) throws TwitterException {
				listFriendship.add(userId);
				return new UserMock(){
					private static final long serialVersionUID = 7907704279643794875L;
					@Override
					public String getScreenName() {
						return "user1";
					}
				};
			}

			@Override
    		public Status updateStatus(String status) throws TwitterException {
				String testMessage = testMessage5.replaceAll("#week_4_hour_11#", "");
				assertEquals("@user1 " + testMessage, status);
				return new StatusMock();
    		}
    	};
    	
    	// Wednesday 11:10
    	System.setProperty(TwitterBot.LOCAL_CALENDER_KEY, "201108101110"); 
    	
    	bot.run(remoteAccount.getKey(), now);
    	
    	assertEquals(1, listFriendship.size());
    	assertEquals(Long.valueOf(2L), listFriendship.get(0));
    	
    	// delete account
    	servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
}
