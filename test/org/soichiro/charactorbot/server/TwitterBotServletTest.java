/**
 * 
 */
package org.soichiro.charactorbot.server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.soichiro.charactorbot.client.CTwitterAccount;
import org.soichiro.charactorbot.client.CUser;
import org.soichiro.charactorbot.client.CharactorbotRPCException;
import org.soichiro.charactorbot.client.CharactorbotService;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

public class TwitterBotServletTest {

    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalUserServiceTestConfig())
            .setEnvIsAdmin(true)
            .setEnvIsLoggedIn(true)
            .setEnvEmail("charactorbot")
            .setEnvAuthDomain("gmail.com");
    
    private final CharactorbotService charactorbotServlet = new CharactorbotServiceImpl();
    private final TwitterBotServlet twitterBotServlet = new TwitterBotServlet();

	private List<CTwitterAccount> listAccount = new ArrayList<CTwitterAccount>();
    
    @Before
    public void setUp() throws CharactorbotRPCException {
        helper.setUp();
        
    	// Add 10 accounts
        for (int i = 0; i < 10; i++) {
        	CTwitterAccount account = new CTwitterAccount();
        	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE) + "_" + i);
    		account.setScreenName("sifue_4466");
    		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
    		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
    		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
    		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
    		account.setTimeZoneId("UTC");
    		account.setIsActivated(Boolean.TRUE);
    		
    		charactorbotServlet.addTwitterAccount(account);
		}
        
        // Add 10 not activate accounts
        for (int j = 0; j < 10; j++) {
        	CTwitterAccount account = new CTwitterAccount();
        	account.setBotName("TestBot_" + new Random().nextInt(Integer.MAX_VALUE) + "_na_" + j);
    		account.setScreenName("sifue_4466");
    		account.setConsumerKey("w0K5IffSJaYziqW8jClAkQ");
    		account.setConsumerSecret("QlVKCAaYmt2VVXS4xzH6Xyd5o1Iosw817qyDFQzTY");
    		account.setToken("5638792-qyMjROPcSUe0x69R9vdByperiFbYNiGln8LyDPtK1A");
    		account.setSecret("oJ08qAGQWOuxbdebDB8zU1ZhrVID9PUVde2vCpBI");
    		account.setTimeZoneId("UTC");
    		account.setIsActivated(Boolean.FALSE);
    		charactorbotServlet.addTwitterAccount(account);
		}
        
		// get
        listAccount = charactorbotServlet.getTwitterAccountList(new CUser("charactorbot","gmail.com"));
    }

    @After
    public void tearDown() throws CharactorbotRPCException {
    	
    	for (CTwitterAccount account : listAccount) {
    		charactorbotServlet.deleteTwitterAccount(account.getKey());
		}
    	
        helper.tearDown();
    }

    /**
     * without batch
     * @throws IOException
     */
    @Test
    public void testDoGetWithoutBatch() throws IOException {
    	System.setProperty(TwitterBotServlet.IS_USE_BATCH_KEY, "false");
    	
    	// test
    	final int[] countRef = new int[1];
    	twitterBotServlet.bot = new ITwitterBot() {
    		int count = 0;
			@Override
			public void run(String keyTwitterAccount, Date now) {
				assertEquals(listAccount.get(count).getKey(), keyTwitterAccount);
				count++;
				countRef[0] = count;
				// use to delete activated accounts
//				try {
//					charactorbotServlet.deleteTwitterAccount(keyTwitterAccount);
//				} catch (CharactorbotRPCException e) {
//					e.printStackTrace();
//				}
			}
		};
		twitterBotServlet.executer = new IAccountTaskExecutor() {
			@Override
			public void addTaskQueue(Date now, List<String> listExecuteKey) {
				fail();
			}
		};
		
		// GET
		twitterBotServlet.doGet(new HttpServletRequestMock(), new HttpServletResponseMock());
		
		// test count
		assertEquals(10, countRef[0]);
    }
    
    
    /**
     * with batch. size is 1.
     * batch size is [1,1,1,1,1,1,1,1,1,1]
     * @throws IOException
     */
    @Test
    public void testDoGetWithBatchSizeOne() throws IOException {
    	System.setProperty(TwitterBotServlet.IS_USE_BATCH_KEY, "true");
    	System.setProperty(TwitterBotServlet.BATCH_SIZE_KEY, "1");
    	
    	// test
    	twitterBotServlet.bot = new ITwitterBot() {
			@Override
			public void run(String keyTwitterAccount, Date now) {
				fail();
			}
		};
		final List<List<String>> listOfListExecuteKey = 
				new ArrayList<List<String>>();
		twitterBotServlet.executer = new IAccountTaskExecutor() {
			@Override
			public void addTaskQueue(Date now, List<String> listExecuteKey) {
				listOfListExecuteKey.add(listExecuteKey);
			}
		};
		
		// GET
		twitterBotServlet.doGet(new HttpServletRequestMock(), new HttpServletResponseMock());
		
		// test count
		assertEquals(10, listOfListExecuteKey.size());
		for (int i = 0; i < 10; i++) {
			assertEquals(1, listOfListExecuteKey.get(i).size());
		}
    }
    

    /**
     * with batch. size is 2.
     * batch size is [2,2,2,2,2]
     * @throws IOException
     */
    @Test
    public void testDoGetWithBatchSizeTwo() throws IOException {
    	System.setProperty(TwitterBotServlet.IS_USE_BATCH_KEY, "true");
    	System.setProperty(TwitterBotServlet.BATCH_SIZE_KEY, "2");
    	
    	// test
    	twitterBotServlet.bot = new ITwitterBot() {
			@Override
			public void run(String keyTwitterAccount, Date now) {
				fail();
			}
		};
		final List<List<String>> listOfListExecuteKey = 
				new ArrayList<List<String>>();
		twitterBotServlet.executer = new IAccountTaskExecutor() {
			@Override
			public void addTaskQueue(Date now, List<String> listExecuteKey) {
				listOfListExecuteKey.add(listExecuteKey);
			}
		};
		
		// GET
		twitterBotServlet.doGet(new HttpServletRequestMock(), new HttpServletResponseMock());
		
		// test count
		assertEquals(5, listOfListExecuteKey.size());
		assertEquals(2, listOfListExecuteKey.get(0).size());
		assertEquals(2, listOfListExecuteKey.get(1).size());
		assertEquals(2, listOfListExecuteKey.get(2).size());
		assertEquals(2, listOfListExecuteKey.get(3).size());
		assertEquals(2, listOfListExecuteKey.get(4).size());
    }
    
    
    /**
     * with batch. size is 3.
     * batch size is [3,3,3,1]
     * @throws IOException
     */
    @Test
    public void testDoGetWithBatchSizeThree() throws IOException {
    	System.setProperty(TwitterBotServlet.IS_USE_BATCH_KEY, "true");
    	System.setProperty(TwitterBotServlet.BATCH_SIZE_KEY, "3");
    	
    	// test
    	twitterBotServlet.bot = new ITwitterBot() {
			@Override
			public void run(String keyTwitterAccount, Date now) {
				fail();
			}
		};
		final List<List<String>> listOfListExecuteKey = 
				new ArrayList<List<String>>();
		twitterBotServlet.executer = new IAccountTaskExecutor() {
			@Override
			public void addTaskQueue(Date now, List<String> listExecuteKey) {
				listOfListExecuteKey.add(listExecuteKey);
			}
		};
		
		// GET
		twitterBotServlet.doGet(new HttpServletRequestMock(), new HttpServletResponseMock());
		
		// test count
		assertEquals(4, listOfListExecuteKey.size());
		assertEquals(3, listOfListExecuteKey.get(0).size());
		assertEquals(3, listOfListExecuteKey.get(1).size());
		assertEquals(3, listOfListExecuteKey.get(2).size());
		assertEquals(1, listOfListExecuteKey.get(3).size());
    }
    
    
    /**
     * with batch. size is 4.
     * batch size is [4,4,2]
     * @throws IOException
     */
    @Test
    public void testDoGetWithBatchSizeFour() throws IOException {
    	System.setProperty(TwitterBotServlet.IS_USE_BATCH_KEY, "true");
    	System.setProperty(TwitterBotServlet.BATCH_SIZE_KEY, "4");
    	
    	// test
    	twitterBotServlet.bot = new ITwitterBot() {
			@Override
			public void run(String keyTwitterAccount, Date now) {
				fail();
			}
		};
		final List<List<String>> listOfListExecuteKey = 
				new ArrayList<List<String>>();
		twitterBotServlet.executer = new IAccountTaskExecutor() {
			@Override
			public void addTaskQueue(Date now, List<String> listExecuteKey) {
				listOfListExecuteKey.add(listExecuteKey);
			}
		};
		
		// GET
		twitterBotServlet.doGet(new HttpServletRequestMock(),new HttpServletResponseMock());
		
		// test count
		assertEquals(3, listOfListExecuteKey.size());
		assertEquals(4, listOfListExecuteKey.get(0).size());
		assertEquals(4, listOfListExecuteKey.get(1).size());
		assertEquals(2, listOfListExecuteKey.get(2).size());
    }
    
    
    /**
     * with batch. size is 5.
     * batch size is [5,5]
     * @throws IOException
     */
    @Test
    public void testDoGetWithBatchSizeFive() throws IOException {
    	System.setProperty(TwitterBotServlet.IS_USE_BATCH_KEY, "true");
    	System.setProperty(TwitterBotServlet.BATCH_SIZE_KEY, "5");
    	
    	// test
    	twitterBotServlet.bot = new ITwitterBot() {
			@Override
			public void run(String keyTwitterAccount, Date now) {
				fail();
			}
		};
		final List<List<String>> listOfListExecuteKey = 
				new ArrayList<List<String>>();
		twitterBotServlet.executer = new IAccountTaskExecutor() {
			@Override
			public void addTaskQueue(Date now, List<String> listExecuteKey) {
				listOfListExecuteKey.add(listExecuteKey);
			}
		};
		
		// GET
		twitterBotServlet.doGet(new HttpServletRequestMock(), new HttpServletResponseMock());
		
		// test count
		assertEquals(2, listOfListExecuteKey.size());
		assertEquals(5, listOfListExecuteKey.get(0).size());
		assertEquals(5, listOfListExecuteKey.get(1).size());
    }
    
    /**
     * with batch. size is 20.
     * batch size is [10]
     * @throws IOException
     */
    @Test
    public void testDoGetWithBatchSizeTwenty() throws IOException {
    	System.setProperty(TwitterBotServlet.IS_USE_BATCH_KEY, "true");
    	System.setProperty(TwitterBotServlet.BATCH_SIZE_KEY, "20");
    	
    	// test
    	twitterBotServlet.bot = new ITwitterBot() {
			@Override
			public void run(String keyTwitterAccount, Date now) {
				fail();
			}
		};
		final List<List<String>> listOfListExecuteKey = 
				new ArrayList<List<String>>();
		twitterBotServlet.executer = new IAccountTaskExecutor() {
			@Override
			public void addTaskQueue(Date now, List<String> listExecuteKey) {
				listOfListExecuteKey.add(listExecuteKey);
			}
		};
		
		// GET
		twitterBotServlet.doGet(new HttpServletRequestMock(), new HttpServletResponseMock());
		
		// test count
		assertEquals(1, listOfListExecuteKey.size());
		assertEquals(10, listOfListExecuteKey.get(0).size());
    }
}