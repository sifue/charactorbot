/**
 * 
 */
package org.soichiro.charactorbot.server;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.soichiro.charactorbot.client.CKeyword;
import org.soichiro.charactorbot.client.CLogEntry;
import org.soichiro.charactorbot.client.CPost;
import org.soichiro.charactorbot.client.CPostType;
import org.soichiro.charactorbot.client.CTwitterAccount;
import org.soichiro.charactorbot.client.CUser;
import org.soichiro.charactorbot.client.CharactorbotRPCException;
import org.soichiro.charactorbot.client.CharactorbotService;
import org.soichiro.charactorbot.client.PostTypeEnum;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author soichiro
 *
 */
public class CharactorbotServiceImplTest {

    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig())
		    .setEnvIsAdmin(true)
		    .setEnvIsLoggedIn(true)
		    .setEnvEmail("charactorbot")
		    .setEnvAuthDomain("gmail.com");

    private final CharactorbotService servlet = new CharactorbotServiceImpl();
    
    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }
    
    @Test
    public void testGetNumberRemainingTwitterAccount() throws CharactorbotRPCException {
        assertEquals(Integer.valueOf(23), servlet.getNumberRemainingTwitterAccount());
    }
    
    @Test
    public void testGetMessageOnTopPage() throws CharactorbotRPCException {
    	assertNull(servlet.getMessageOnTopPage());
    }

    /**
     * Test for
     * addTwitterAccount
     * getTwitterAccountList
     * editTwitterAccount
     * getLogEntryList (DatastoreLogHandler)
     * deleteTwitterAccount
     * 
     * @throws CharactorbotRPCException
     */
    @Test
    public void testProcessTwitterAccount() throws CharactorbotRPCException {
    	
    	// Add
    	CTwitterAccount account = new CTwitterAccount();
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
		account.setTimeZoneId("UTC");
		account.setIsActivated(Boolean.TRUE);
    	
    	servlet.addTwitterAccount(account);
    	
    	// get
        List<CTwitterAccount> listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        CTwitterAccount remoteAccount = listAccount.get(0);
        
        assertEquals(account.getBotName(), remoteAccount.getBotName());
        assertEquals(account.getScreenName(), remoteAccount.getScreenName());
        assertEquals(account.getConsumerKey(), remoteAccount.getConsumerKey());
        assertEquals(account.getConsumerSecret(), remoteAccount.getConsumerSecret());
        assertEquals(account.getToken(), remoteAccount.getToken());
        assertEquals(account.getSecret(), remoteAccount.getSecret());
        assertEquals(account.getTimeZoneId(), remoteAccount.getTimeZoneId());
        assertEquals(account.getIsActivated(), remoteAccount.getIsActivated());
        
        // edit
        account.setKey(remoteAccount.getKey());
    	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE));
		account.setScreenName("sifue_4466_edit");
		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ_edit");
		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY_edit");
		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A_edit");
		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI_edit");
		account.setTimeZoneId("Asia/Tokyo");
		account.setIsActivated(Boolean.FALSE);
		account.setCreatedAt(remoteAccount.getCreatedAt());
		account.setCreatedBy(remoteAccount.getCreatedBy());
		account.setUpdatedAt(remoteAccount.getUpdatedAt());
		account.setUpdatedBy(remoteAccount.getUpdatedBy());
        
        servlet.editTwitterAccount(account);
     
        // get
        listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        remoteAccount = listAccount.get(0);
        
        assertEquals(account.getKey(), remoteAccount.getKey());
        assertEquals(account.getBotName(), remoteAccount.getBotName());
        assertEquals(account.getScreenName(), remoteAccount.getScreenName());
        assertEquals(account.getConsumerKey(), remoteAccount.getConsumerKey());
        assertEquals(account.getConsumerSecret(), remoteAccount.getConsumerSecret());
        assertEquals(account.getToken(), remoteAccount.getToken());
        assertEquals(account.getSecret(), remoteAccount.getSecret());
        assertEquals(account.getIsActivated(), remoteAccount.getIsActivated());
        assertEquals(account.getCreatedAt(), remoteAccount.getCreatedAt());
        assertEquals(account.getCreatedBy(), remoteAccount.getCreatedBy());
        assertNotSame(account.getUpdatedAt(), remoteAccount.getUpdatedAt());
        assertEquals(account.getUpdatedBy(), remoteAccount.getUpdatedBy());
        
        // getLogEntry
        List<CLogEntry> logEntryList = servlet.getLogEntryList(account.getKey());
		assertNotNull(logEntryList);
		assertEquals(0, logEntryList.size());
		
        // delete
        servlet.deleteTwitterAccount(account.getKey());
        
        // get
        listAccount = servlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
        
        assertEquals(0, listAccount.size());
    }
    
    /**
     * only update.
     * test for
     * getPostTypeWithDetail
     * updatePostTypeWithDetail
     * 
     * @throws CharactorbotRPCException 
     */
    @Test
    public void testUpdatePostTypeWithDetail1() throws CharactorbotRPCException {
    	
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
        	
        	assertEquals(type.toString(), postType.getPostTypeName());
        	
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("Keyword_Active");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	
	    		keyword = new CKeyword();
	    		keyword.setKeyword("Keyword_Inactive");
	    		keyword.setIsActivated(Boolean.FALSE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	
	    		keyword = new CKeyword();
	    		keyword.setKeyword(".*Keyword_Active_Regex.*");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.TRUE);
	        	listKeyword.add(keyword);
	        	
	    		keyword = new CKeyword();
	    		keyword.setKeyword(".*Keyword_Inactive_Regex.*");
	    		keyword.setIsActivated(Boolean.FALSE);
	    		keyword.setIsRegex(Boolean.TRUE);
	        	listKeyword.add(keyword);
				break;
			default:
				break;
			}
        	
        	for (CKeyword cKeyword : listKeyword) {
        		List<CPost> listPost = cKeyword.getListPost();
				CPost post = new CPost();
        		post.setMessage("#user_name# " + type.toString() + " - user_name");
        		listPost.add(post);
        		post = new CPost();
    			post.setMessage("#date# " + type.toString() + " - date");
        		listPost.add(post);
        		post = new CPost();
    			post.setMessage("#time# " + type.toString() + " - time");
        		listPost.add(post);
        		post = new CPost();
        		post.setMessage("#stop# " + type.toString() + " - stop");
        		listPost.add(post);
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        	
        	// get
        	CPostType remotePostType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	
        	// test and part update
        	List<CKeyword> remoteListKeyword = remotePostType.getListKeyword();
        	for (int i = 0; i < remoteListKeyword.size(); i++) {
        		
        		CKeyword cKeyword = null;
        		List<CPost> listPost = null;
            	switch (type) {
    			case REPLY:
    			case REPLY_FOR_ME:
					switch (i) {
					case 0:
						cKeyword = remoteListKeyword.get(i);
						assertEquals("Keyword_Active", cKeyword.getKeyword());
						assertEquals(Boolean.TRUE, cKeyword.getIsActivated());
						assertEquals(Boolean.FALSE, cKeyword.getIsRegex());
						assertEquals(Integer.valueOf(i), cKeyword.getSequence());
						assertNotNull(cKeyword.getCreatedAt());
						assertNotNull(cKeyword.getCreatedBy());
						assertNotNull(cKeyword.getUpdatedAt());
						assertNotNull(cKeyword.getUpdatedBy());
						listPost = cKeyword.getListPost();
						break;
					case 1:
						cKeyword = remoteListKeyword.get(i);
						assertEquals("Keyword_Inactive", cKeyword.getKeyword());
						assertEquals(Boolean.FALSE, cKeyword.getIsActivated());
						assertEquals(Boolean.FALSE, cKeyword.getIsRegex());
						assertEquals(Integer.valueOf(i), cKeyword.getSequence());
						assertNotNull(cKeyword.getCreatedAt());
						assertNotNull(cKeyword.getCreatedBy());
						assertNotNull(cKeyword.getUpdatedAt());
						assertNotNull(cKeyword.getUpdatedBy());
						listPost = cKeyword.getListPost();
						
						// part updated
						cKeyword.setKeyword("Keyword_Active_Regex_updated");
						cKeyword.setIsActivated(Boolean.TRUE);
						cKeyword.setIsRegex(Boolean.TRUE);
						
						break;
					case 2:
						cKeyword = remoteListKeyword.get(i);
						assertEquals(".*Keyword_Active_Regex.*", cKeyword.getKeyword());
						assertEquals(Boolean.TRUE, cKeyword.getIsActivated());
						assertEquals(Boolean.TRUE, cKeyword.getIsRegex());
						assertEquals(Integer.valueOf(i), cKeyword.getSequence());
						assertNotNull(cKeyword.getCreatedAt());
						assertNotNull(cKeyword.getCreatedBy());
						assertNotNull(cKeyword.getUpdatedAt());
						assertNotNull(cKeyword.getUpdatedBy());
						listPost = cKeyword.getListPost();
						break;
					case 3:
						cKeyword = remoteListKeyword.get(i);
						assertEquals(".*Keyword_Inactive_Regex.*", cKeyword.getKeyword());
						assertEquals(Boolean.FALSE, cKeyword.getIsActivated());
						assertEquals(Boolean.TRUE, cKeyword.getIsRegex());
						assertEquals(Integer.valueOf(i), cKeyword.getSequence());
						assertNotNull(cKeyword.getCreatedAt());
						assertNotNull(cKeyword.getCreatedBy());
						assertNotNull(cKeyword.getUpdatedAt());
						assertNotNull(cKeyword.getUpdatedBy());
						listPost = cKeyword.getListPost();
						break;
					}
					break;
				default:
					cKeyword = remoteListKeyword.get(i);
					listPost = cKeyword.getListPost();
					break;
            	}
				
            	for (int j = 0; j < listPost.size(); j++) {
						
	        		CPost cPost = null;
    				switch (j) {
            		case 0:
            			cPost = listPost.get(j);
            			assertEquals("#user_name# " + type.toString() + " - user_name", cPost.getMessage());
            			assertNotNull(cPost.getCreatedAt());
            			assertNotNull(cPost.getCreatedBy());
            			assertNotNull(cPost.getUpdatedAt());
            			assertNotNull(cPost.getUpdatedBy());
            			break;
    				case 1:
    					cPost = listPost.get(j);
    					assertEquals("#date# " + type.toString() + " - date", cPost.getMessage());
    					assertNotNull(cPost.getCreatedAt());
						assertNotNull(cPost.getCreatedBy());
						assertNotNull(cPost.getUpdatedAt());
						assertNotNull(cPost.getUpdatedBy());
    					break;
    				case 2:
    					cPost = listPost.get(j);
    					assertEquals("#time# " + type.toString() + " - time", cPost.getMessage());
						assertNotNull(cPost.getCreatedAt());
						assertNotNull(cPost.getCreatedBy());
						assertNotNull(cPost.getUpdatedAt());
						assertNotNull(cPost.getUpdatedBy());
						
						// part update
						cPost.setMessage("#time# " + type.toString() + " - time_update");
    					break;
    				case 3:
    					cPost = listPost.get(j);
    					assertEquals("#stop# " + type.toString() + " - stop", cPost.getMessage());
						assertNotNull(cPost.getCreatedAt());
						assertNotNull(cPost.getCreatedBy());
						assertNotNull(cPost.getUpdatedAt());
						assertNotNull(cPost.getUpdatedBy());
    					break;
    				}
				}
			}
        	
        	
        	// update
        	servlet.updatePostTypeWithDetail(remotePostType);
        	
        	// get
        	CPostType remotePostType2 = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	
        	// test and part update
        	List<CKeyword> remoteListKeyword2 = remotePostType2.getListKeyword();
        	for (int i = 0; i < remoteListKeyword.size(); i++) {
        		
        		CKeyword cKeyword = null;
        		CKeyword cKeyword2 = null;
        		List<CPost> listPost = null;
        		List<CPost> listPost2 = null;
            	switch (type) {
    			case REPLY:
    			case REPLY_FOR_ME:
					switch (i) {
					case 0:
						cKeyword = remoteListKeyword.get(i);
						cKeyword2 = remoteListKeyword2.get(i);
						
						assertEquals(cKeyword.getKeyword(), cKeyword2.getKeyword());
						assertEquals(cKeyword.getIsActivated(), cKeyword2.getIsActivated());
						assertEquals(cKeyword.getIsRegex(), cKeyword2.getIsRegex());
						assertEquals(cKeyword.getSequence(), cKeyword2.getSequence());
						assertEquals(cKeyword.getCreatedAt(), cKeyword2.getCreatedAt());
						assertEquals(cKeyword.getCreatedBy(), cKeyword2.getCreatedBy());
						assertEquals(cKeyword.getUpdatedAt(), cKeyword2.getUpdatedAt());
						assertEquals(cKeyword.getUpdatedBy(), cKeyword2.getUpdatedBy());
						
						listPost = cKeyword.getListPost();
						listPost2 = cKeyword2.getListPost();
						break;
					case 1:
						cKeyword = remoteListKeyword.get(i);
						cKeyword2 = remoteListKeyword2.get(i);
						
						assertEquals("Keyword_Active_Regex_updated", cKeyword2.getKeyword());
						assertEquals(Boolean.TRUE, cKeyword2.getIsActivated());
						assertEquals(Boolean.TRUE, cKeyword2.getIsRegex());
						assertEquals(cKeyword.getSequence(), cKeyword2.getSequence());
						assertEquals(cKeyword.getCreatedAt(), cKeyword2.getCreatedAt());
						assertEquals(cKeyword.getCreatedBy(), cKeyword2.getCreatedBy());
						assertNotSame(cKeyword.getUpdatedAt(), cKeyword2.getUpdatedAt());
						assertEquals(cKeyword.getUpdatedBy(), cKeyword2.getUpdatedBy());
						
						listPost = cKeyword.getListPost();
						listPost2 = cKeyword2.getListPost();
						
						break;
					case 2:
						cKeyword = remoteListKeyword.get(i);
						cKeyword2 = remoteListKeyword2.get(i);
						
						assertEquals(cKeyword.getKeyword(), cKeyword2.getKeyword());
						assertEquals(cKeyword.getIsActivated(), cKeyword2.getIsActivated());
						assertEquals(cKeyword.getIsRegex(), cKeyword2.getIsRegex());
						assertEquals(cKeyword.getSequence(), cKeyword2.getSequence());
						assertEquals(cKeyword.getCreatedAt(), cKeyword2.getCreatedAt());
						assertEquals(cKeyword.getCreatedBy(), cKeyword2.getCreatedBy());
						assertEquals(cKeyword.getUpdatedAt(), cKeyword2.getUpdatedAt());
						assertEquals(cKeyword.getUpdatedBy(), cKeyword2.getUpdatedBy());
						
						listPost = cKeyword.getListPost();
						listPost2 = cKeyword2.getListPost();
						break;
					case 3:
						cKeyword = remoteListKeyword.get(i);
						cKeyword2 = remoteListKeyword2.get(i);
						
						assertEquals(cKeyword.getKeyword(), cKeyword2.getKeyword());
						assertEquals(cKeyword.getIsActivated(), cKeyword2.getIsActivated());
						assertEquals(cKeyword.getIsRegex(), cKeyword2.getIsRegex());
						assertEquals(cKeyword.getSequence(), cKeyword2.getSequence());
						assertEquals(cKeyword.getCreatedAt(), cKeyword2.getCreatedAt());
						assertEquals(cKeyword.getCreatedBy(), cKeyword2.getCreatedBy());
						assertEquals(cKeyword.getUpdatedAt(), cKeyword2.getUpdatedAt());
						assertEquals(cKeyword.getUpdatedBy(), cKeyword2.getUpdatedBy());
						
						listPost = cKeyword.getListPost();
						listPost2 = cKeyword2.getListPost();
						break;
					}
					break;
				default:
					cKeyword = remoteListKeyword.get(i);
					cKeyword2 = remoteListKeyword2.get(i);
					listPost = cKeyword.getListPost();
					listPost2 = cKeyword2.getListPost();
					break;
            	}
				
            	for (int j = 0; j < listPost2.size(); j++) {
						
	        		CPost cPost = null;
	        		CPost cPost2 = null;
    				switch (j) {
             		case 0:
            			cPost = listPost.get(j);
            			cPost2 = listPost2.get(j);
            			assertEquals(cPost.getMessage(), cPost2.getMessage());
            			assertEquals(cPost.getCreatedAt(), cPost2.getCreatedAt());
            			assertEquals(cPost.getCreatedBy(), cPost2.getCreatedBy());
            			assertEquals(cPost.getUpdatedAt(), cPost2.getUpdatedAt());
            			assertEquals(cPost.getUpdatedBy(), cPost2.getUpdatedBy());
            			break;
    				case 1:
    					cPost = listPost.get(j);
    					cPost2 = listPost2.get(j);
    					assertEquals(cPost.getMessage(), cPost2.getMessage());
    					assertEquals(cPost.getCreatedAt(), cPost2.getCreatedAt());
    					assertEquals(cPost.getCreatedBy(), cPost2.getCreatedBy());
    					assertEquals(cPost.getUpdatedAt(), cPost2.getUpdatedAt());
    					assertEquals(cPost.getUpdatedBy(), cPost2.getUpdatedBy());
    					break;
    				case 2:
    					cPost = listPost.get(j);
    					cPost2 = listPost2.get(j);
    					assertEquals("#time# " + type.toString() + " - time_update", cPost2.getMessage());
    					assertNotSame(cPost.getCreatedAt(), cPost2.getCreatedAt());
    					assertNotSame(cPost.getCreatedBy(), cPost2.getCreatedBy());
    					assertNotSame(cPost.getUpdatedAt(), cPost2.getUpdatedAt());
    					assertNotSame(cPost.getUpdatedBy(), cPost2.getUpdatedBy());
    					break;
    				case 3:
    					cPost = listPost.get(j);
    					cPost2 = listPost2.get(j);
    					assertEquals(cPost.getMessage(), cPost2.getMessage());
    					assertNotSame(cPost.getCreatedAt(), cPost2.getCreatedAt());
    					assertNotSame(cPost.getCreatedBy(), cPost2.getCreatedBy());
    					assertNotSame(cPost.getUpdatedAt(), cPost2.getUpdatedAt());
    					assertNotSame(cPost.getUpdatedBy(), cPost2.getUpdatedBy());
    					break;
	    			}
				}
			}
		}
    }
    
    
    /**
     * only remove and add.
     * test for 
     * getPostTypeWithDetail
     * updatePostTypeWithDetail
     * deleteAllKeyword
     * 
     * @throws CharactorbotRPCException 
     */
    @SuppressWarnings("unused")
	@Test
    public void testUpdatePostTypeWithDetail2() throws CharactorbotRPCException {
    	
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
        	
        	assertEquals(type.toString(), postType.getPostTypeName());
        	
        	List<CKeyword> listKeyword = postType.getListKeyword();
        	switch (type) {
			case REPLY:
			case REPLY_FOR_ME:
	    		CKeyword keyword = new CKeyword();
	    		keyword.setKeyword("Keyword_Active");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	
	    		keyword = new CKeyword();
	    		keyword.setKeyword("Keyword_Inactive");
	    		keyword.setIsActivated(Boolean.FALSE);
	    		keyword.setIsRegex(Boolean.FALSE);
	        	listKeyword.add(keyword);
	        	
	    		keyword = new CKeyword();
	    		keyword.setKeyword(".*Keyword_Active_Regex.*");
	    		keyword.setIsActivated(Boolean.TRUE);
	    		keyword.setIsRegex(Boolean.TRUE);
	        	listKeyword.add(keyword);
	        	
	    		keyword = new CKeyword();
	    		keyword.setKeyword(".*Keyword_Inactive_Regex.*");
	    		keyword.setIsActivated(Boolean.FALSE);
	    		keyword.setIsRegex(Boolean.TRUE);
	        	listKeyword.add(keyword);
				break;
			default:
				break;
			}
        	
        	for (CKeyword cKeyword : listKeyword) {
        		List<CPost> listPost = cKeyword.getListPost();
				CPost post = new CPost();
        		post.setMessage("#user_name# " + type.toString() + " - user_name");
        		listPost.add(post);
        		
        		post = new CPost();
    			post.setMessage("#date# " + type.toString() + " - date");
        		listPost.add(post);
        		
        		post = new CPost();
    			post.setMessage("#time# " + type.toString() + " - time");
        		listPost.add(post);
        		
        		post = new CPost();
        		post.setMessage("#stop# " + type.toString() + " - stop");
        		listPost.add(post);
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(postType);
        	
        	// get
        	CPostType remotePostType = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	
        	// test and part add and remove
        	List<CKeyword> remoteListKeyword = remotePostType.getListKeyword();
        	for (int i = 0; i < remoteListKeyword.size(); i++) {
        		
        		CKeyword cKeyword = null;
        		List<CPost> listPost = null;
            	switch (type) {
    			case REPLY:
    			case REPLY_FOR_ME:
					switch (i) {
					case 0:
						cKeyword = remoteListKeyword.get(i);
						assertEquals("Keyword_Active", cKeyword.getKeyword());
						assertEquals(Boolean.TRUE, cKeyword.getIsActivated());
						assertEquals(Boolean.FALSE, cKeyword.getIsRegex());
						assertEquals(Integer.valueOf(i), cKeyword.getSequence());
						assertNotNull(cKeyword.getCreatedAt());
						assertNotNull(cKeyword.getCreatedBy());
						assertNotNull(cKeyword.getUpdatedAt());
						assertNotNull(cKeyword.getUpdatedBy());
						listPost = cKeyword.getListPost();
						break;
					case 1:
						cKeyword = remoteListKeyword.get(i);
						assertEquals("Keyword_Inactive", cKeyword.getKeyword());
						assertEquals(Boolean.FALSE, cKeyword.getIsActivated());
						assertEquals(Boolean.FALSE, cKeyword.getIsRegex());
						assertEquals(Integer.valueOf(i), cKeyword.getSequence());
						assertNotNull(cKeyword.getCreatedAt());
						assertNotNull(cKeyword.getCreatedBy());
						assertNotNull(cKeyword.getUpdatedAt());
						assertNotNull(cKeyword.getUpdatedBy());
						listPost = cKeyword.getListPost();
						break;
					case 2:
						cKeyword = remoteListKeyword.get(i);
						assertEquals(".*Keyword_Active_Regex.*", cKeyword.getKeyword());
						assertEquals(Boolean.TRUE, cKeyword.getIsActivated());
						assertEquals(Boolean.TRUE, cKeyword.getIsRegex());
						assertEquals(Integer.valueOf(i), cKeyword.getSequence());
						assertNotNull(cKeyword.getCreatedAt());
						assertNotNull(cKeyword.getCreatedBy());
						assertNotNull(cKeyword.getUpdatedAt());
						assertNotNull(cKeyword.getUpdatedBy());
						listPost = cKeyword.getListPost();
						break;
					case 3:
						cKeyword = remoteListKeyword.get(i);
						assertEquals(".*Keyword_Inactive_Regex.*", cKeyword.getKeyword());
						assertEquals(Boolean.FALSE, cKeyword.getIsActivated());
						assertEquals(Boolean.TRUE, cKeyword.getIsRegex());
						assertEquals(Integer.valueOf(i), cKeyword.getSequence());
						assertNotNull(cKeyword.getCreatedAt());
						assertNotNull(cKeyword.getCreatedBy());
						assertNotNull(cKeyword.getUpdatedAt());
						assertNotNull(cKeyword.getUpdatedBy());
						listPost = cKeyword.getListPost();
						break;
					}
					break;
				default:
					cKeyword = remoteListKeyword.get(i);
					listPost = cKeyword.getListPost();
					break;
            	}
            	
            	for (int j = 0; j < listPost.size(); j++) {
	        		CPost cPost = null;
    				switch (j) {
    				case 0:
    					cPost = listPost.get(j);
    					assertEquals("#user_name# " + type.toString() + " - user_name", cPost.getMessage());
						assertNotNull(cPost.getCreatedAt());
						assertNotNull(cPost.getCreatedBy());
						assertNotNull(cPost.getUpdatedAt());
						assertNotNull(cPost.getUpdatedBy());
    					break;
    				case 1:
    					cPost = listPost.get(j);
    					assertEquals("#date# " + type.toString() + " - date", cPost.getMessage());
    					assertNotNull(cPost.getCreatedAt());
						assertNotNull(cPost.getCreatedBy());
						assertNotNull(cPost.getUpdatedAt());
						assertNotNull(cPost.getUpdatedBy());
    					break;
    				case 2:
    					cPost = listPost.get(j);
    					assertEquals("#time# " + type.toString() + " - time", cPost.getMessage());
						assertNotNull(cPost.getCreatedAt());
						assertNotNull(cPost.getCreatedBy());
						assertNotNull(cPost.getUpdatedAt());
						assertNotNull(cPost.getUpdatedBy());
						
    					break;
    				case 3:
    					cPost = listPost.get(j);
    					assertEquals("#stop# " + type.toString() + " - stop", cPost.getMessage());
						assertNotNull(cPost.getCreatedAt());
						assertNotNull(cPost.getCreatedBy());
						assertNotNull(cPost.getUpdatedAt());
						assertNotNull(cPost.getUpdatedBy());
    					break;
    				}
    				break;
				}
            	
            	// remove post 1
            	listPost.remove(2);
            	
    			// add last
        		CPost newPost = new CPost();
        		newPost.setMessage("#time# " + type.toString() + " - time_new");
        		listPost.add(2, newPost);
			}
        	
        	switch (type) {
			case REPLY:
			case REPLY_FOR_ME:
            	// remove keyword 1
				remoteListKeyword.remove(1);
				// add keyword
	    		CKeyword newKeyword = new CKeyword();
	    		newKeyword.setKeyword("Keyword_Inactive_new");
	    		newKeyword.setIsActivated(Boolean.FALSE);
	    		newKeyword.setIsRegex(Boolean.FALSE);
	    		remoteListKeyword.add(1, newKeyword);
				break;
			default:
				break;
			}
        	
        	// update
        	servlet.updatePostTypeWithDetail(remotePostType);
        	
        	// get
        	CPostType remotePostType2 = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	
        	// test and part update
        	List<CKeyword> remoteListKeyword2 = remotePostType2.getListKeyword();
        	for (int i = 0; i < remoteListKeyword.size(); i++) {
        		
        		CKeyword cKeyword = null;
        		CKeyword cKeyword2 = null;
        		List<CPost> listPost = null;
        		List<CPost> listPost2 = null;
            	switch (type) {
    			case REPLY:
    			case REPLY_FOR_ME:
					switch (i) {
					case 0:
						cKeyword = remoteListKeyword.get(i);
						cKeyword2 = remoteListKeyword2.get(i);
						
						assertEquals(cKeyword.getKeyword(), cKeyword2.getKeyword());
						assertEquals(cKeyword.getIsActivated(), cKeyword2.getIsActivated());
						assertEquals(cKeyword.getIsRegex(), cKeyword2.getIsRegex());
						assertEquals(cKeyword.getSequence(), cKeyword2.getSequence());
						assertEquals(cKeyword.getCreatedAt(), cKeyword2.getCreatedAt());
						assertEquals(cKeyword.getCreatedBy(), cKeyword2.getCreatedBy());
						assertEquals(cKeyword.getUpdatedAt(), cKeyword2.getUpdatedAt());
						assertEquals(cKeyword.getUpdatedBy(), cKeyword2.getUpdatedBy());
						
						listPost = cKeyword.getListPost();
						listPost2 = cKeyword2.getListPost();
						break;
					case 1:
						cKeyword = remoteListKeyword.get(i);
						cKeyword2 = remoteListKeyword2.get(i);
						
						assertEquals("Keyword_Inactive_new", cKeyword2.getKeyword());
						assertEquals(Boolean.FALSE, cKeyword2.getIsActivated());
						assertEquals(Boolean.FALSE, cKeyword2.getIsRegex());
						assertEquals(Integer.valueOf(1), cKeyword2.getSequence());
						assertNotSame(cKeyword.getCreatedAt(), cKeyword2.getCreatedAt());
						assertNotSame(cKeyword.getCreatedBy(), cKeyword2.getCreatedBy());
						assertNotSame(cKeyword.getUpdatedAt(), cKeyword2.getUpdatedAt());
						assertNotSame(cKeyword.getUpdatedBy(), cKeyword2.getUpdatedBy());
						
						listPost = cKeyword.getListPost();
						listPost2 = cKeyword2.getListPost();
						
						break;
					case 2:
						cKeyword = remoteListKeyword.get(i);
						cKeyword2 = remoteListKeyword2.get(i);
						
						assertEquals(cKeyword.getKeyword(), cKeyword2.getKeyword());
						assertEquals(cKeyword.getIsActivated(), cKeyword2.getIsActivated());
						assertEquals(cKeyword.getIsRegex(), cKeyword2.getIsRegex());
						assertEquals(cKeyword.getSequence(), cKeyword2.getSequence());
						assertEquals(cKeyword.getCreatedAt(), cKeyword2.getCreatedAt());
						assertEquals(cKeyword.getCreatedBy(), cKeyword2.getCreatedBy());
						assertEquals(cKeyword.getUpdatedAt(), cKeyword2.getUpdatedAt());
						assertEquals(cKeyword.getUpdatedBy(), cKeyword2.getUpdatedBy());
						
						listPost = cKeyword.getListPost();
						listPost2 = cKeyword2.getListPost();
						break;
					case 3:
						cKeyword = remoteListKeyword.get(i);
						cKeyword2 = remoteListKeyword2.get(i);
						
						assertEquals(cKeyword.getKeyword(), cKeyword2.getKeyword());
						assertEquals(cKeyword.getIsActivated(), cKeyword2.getIsActivated());
						assertEquals(cKeyword.getIsRegex(), cKeyword2.getIsRegex());
						assertEquals(cKeyword.getSequence(), cKeyword2.getSequence());
						assertEquals(cKeyword.getCreatedAt(), cKeyword2.getCreatedAt());
						assertEquals(cKeyword.getCreatedBy(), cKeyword2.getCreatedBy());
						assertEquals(cKeyword.getUpdatedAt(), cKeyword2.getUpdatedAt());
						assertEquals(cKeyword.getUpdatedBy(), cKeyword2.getUpdatedBy());
						
						listPost = cKeyword.getListPost();
						listPost2 = cKeyword2.getListPost();
						break;
					}
					break;
				default:
					cKeyword = remoteListKeyword.get(i);
					cKeyword2 = remoteListKeyword2.get(i);
					listPost = cKeyword.getListPost();
					listPost2 = cKeyword2.getListPost();
					break;
            	}
				
            	for (int j = 0; j < listPost2.size(); j++) {
						
	        		CPost cPost = null;
	        		CPost cPost2 = null;
    				switch (j) {
    				case 0:
    					cPost = listPost.get(j);
    					cPost2 = listPost2.get(j);
    					assertEquals(cPost.getMessage(), cPost2.getMessage());
    					assertEquals(cPost.getCreatedAt(), cPost2.getCreatedAt());
    					assertEquals(cPost.getCreatedBy(), cPost2.getCreatedBy());
    					assertEquals(cPost.getUpdatedAt(), cPost2.getUpdatedAt());
    					assertEquals(cPost.getUpdatedBy(), cPost2.getUpdatedBy());
    					break;
    				case 1:
    					cPost = listPost.get(j);
    					cPost2 = listPost2.get(j);
    					assertEquals(cPost.getMessage(), cPost2.getMessage());
    					assertEquals(cPost.getCreatedAt(), cPost2.getCreatedAt());
    					assertEquals(cPost.getCreatedBy(), cPost2.getCreatedBy());
    					assertEquals(cPost.getUpdatedAt(), cPost2.getUpdatedAt());
    					assertEquals(cPost.getUpdatedBy(), cPost2.getUpdatedBy());
    					break;
    				case 2:
    					cPost = listPost.get(j);
    					cPost2 = listPost2.get(j);
    					assertEquals("#time# " + type.toString() + " - time_new", cPost2.getMessage());
    					assertNotSame(cPost.getCreatedAt(), cPost2.getCreatedAt());
    					assertNotSame(cPost.getCreatedBy(), cPost2.getCreatedBy());
    					assertNotSame(cPost.getUpdatedAt(), cPost2.getUpdatedAt());
    					assertNotSame(cPost.getUpdatedBy(), cPost2.getUpdatedBy());
    					break;
    				case 3:
    					cPost = listPost.get(j);
    					cPost2 = listPost2.get(j);
    					assertEquals(cPost.getMessage(), cPost2.getMessage());
    					assertNotSame(cPost.getCreatedAt(), cPost2.getCreatedAt());
    					assertNotSame(cPost.getCreatedBy(), cPost2.getCreatedBy());
    					assertNotSame(cPost.getUpdatedAt(), cPost2.getUpdatedAt());
    					assertNotSame(cPost.getUpdatedBy(), cPost2.getUpdatedBy());
    					break;
	    			}
				}
			}
        	
        	// delete allKeyword of REPLY_FOR_ME
        	CPostType remotePostType3 = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> remoteListKeyword3 = remotePostType3.getListKeyword();
        	assertTrue(remoteListKeyword3.size() > 0);
        	
        	servlet.deleteAllKeyword(remoteAccount.getKey(), type);
        	
        	CPostType remotePostType4 = servlet.getPostTypeWithDetail(remoteAccount.getKey(), type);
        	List<CKeyword> remoteListKeyword4 = remotePostType4.getListKeyword();
        	assertEquals(0,  remoteListKeyword4.size());
		}
    }
    
    @Test
    public void testGetAuthorizationURL() throws CharactorbotRPCException {
    	assertNotNull(servlet.getAuthorizationURL(
    			"w0K5IffSJaYziqW8jClAkQ",
    			"QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY"));
    }
}
