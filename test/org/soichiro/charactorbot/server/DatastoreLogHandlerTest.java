/**
 * 
 */
package org.soichiro.charactorbot.server;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.soichiro.charactorbot.client.CLogEntry;
import org.soichiro.charactorbot.client.CTwitterAccount;
import org.soichiro.charactorbot.client.CUser;
import org.soichiro.charactorbot.client.CharactorbotRPCException;
import org.soichiro.charactorbot.client.CharactorbotService;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author soichiro
 *
 */
public class DatastoreLogHandlerTest {

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
    
    /**
     * store logentry
     * @throws CharactorbotRPCException
     */
    @Test
    public void testPublishWithOneException() throws CharactorbotRPCException {
    	
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
    	
        // getLogEntry
        List<CLogEntry> logEntryList = servlet.getLogEntryList(remoteAccount.getKey());
		assertNotNull(logEntryList);
		assertEquals(0, logEntryList.size());
		
		// throw exception
		DatastoreLogHandler handler = new DatastoreLogHandler();
		LogRecord record = new LogRecord(Level.WARNING, "test");
		TwitterBotException twitterBotException =
				new TwitterBotException(new RuntimeException("test"), remoteAccount.getKey());
		record.setThrown(twitterBotException);
		handler.publish(record);
		
		// getLogEntry
        List<CLogEntry> logEntryList2 = servlet.getLogEntryList(remoteAccount.getKey());
		assertNotNull(logEntryList2);
		assertEquals(1, logEntryList2.size());
        
        // delete
        servlet.deleteTwitterAccount(remoteAccount.getKey());
        
    }
    
    /**
     * store five logEntry  (one is removed)
     * @throws CharactorbotRPCException
     */
    @Test
    public void testPublishWithSixExceptions() throws CharactorbotRPCException {
    	
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
    	
        // getLogEntry
        List<CLogEntry> logEntryList = servlet.getLogEntryList(remoteAccount.getKey());
		assertNotNull(logEntryList);
		assertEquals(0, logEntryList.size());
		
		// throw exception
		DatastoreLogHandler handler = new DatastoreLogHandler();
		
		for (int i = 0; i < 6; i++) {
			LogRecord record = new LogRecord(Level.WARNING, "test" + i);
			TwitterBotException twitterBotException =
					new TwitterBotException(new RuntimeException("test" + i), remoteAccount.getKey());
			record.setThrown(twitterBotException);
			handler.publish(record);
		}
		
		// getLogEntry size is five.
        List<CLogEntry> logEntryList2 = servlet.getLogEntryList(remoteAccount.getKey());
		assertNotNull(logEntryList2);
		assertEquals(5, logEntryList2.size());
        
        // delete
        servlet.deleteTwitterAccount(remoteAccount.getKey());
        
    }
    
    /**
     * not store info log record
     * @throws CharactorbotRPCException
     */
    @Test
    public void testPublishWithInfoLogRecord() throws CharactorbotRPCException {
    	
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
    	
        // getLogEntry
        List<CLogEntry> logEntryList = servlet.getLogEntryList(remoteAccount.getKey());
		assertNotNull(logEntryList);
		assertEquals(0, logEntryList.size());
		
		// throw exception
		DatastoreLogHandler handler = new DatastoreLogHandler();
		LogRecord record = new LogRecord(Level.INFO, "test");
		TwitterBotException twitterBotException =
				new TwitterBotException(new RuntimeException("test"), remoteAccount.getKey());
		record.setThrown(twitterBotException);
		handler.publish(record);
		
		// getLogEntry
        List<CLogEntry> logEntryList2 = servlet.getLogEntryList(remoteAccount.getKey());
		assertNotNull(logEntryList2);
		assertEquals(0, logEntryList2.size());
        
        // delete
        servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
    
    /**
     * not store fine log record
     * @throws CharactorbotRPCException
     */
    @Test
    public void testPublishWithFineLogRecord() throws CharactorbotRPCException {
    	
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
    	
        // getLogEntry
        List<CLogEntry> logEntryList = servlet.getLogEntryList(remoteAccount.getKey());
		assertNotNull(logEntryList);
		assertEquals(0, logEntryList.size());
		
		// throw exception
		DatastoreLogHandler handler = new DatastoreLogHandler();
		LogRecord record = new LogRecord(Level.FINE, "test");
		TwitterBotException twitterBotException =
				new TwitterBotException(new RuntimeException("test"), remoteAccount.getKey());
		record.setThrown(twitterBotException);
		handler.publish(record);
		
		// getLogEntry
        List<CLogEntry> logEntryList2 = servlet.getLogEntryList(remoteAccount.getKey());
		assertNotNull(logEntryList2);
		assertEquals(0, logEntryList2.size());
        
        // delete
        servlet.deleteTwitterAccount(remoteAccount.getKey());
    }
}
