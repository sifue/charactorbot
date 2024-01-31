/**
 * 
 */
package org.soichiro.charactorbot.server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

public class AccountTaskServletTest {

    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalUserServiceTestConfig())
            .setEnvIsAdmin(true)
            .setEnvIsLoggedIn(true)
            .setEnvEmail("charactorbot")
            .setEnvAuthDomain("gmail.com");

    
    private final AccountTaskServlet servlet = new AccountTaskServlet();
    
    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testDoGetSingleAccountTask() throws IOException {
    	
    	servlet.bot = new ITwitterBot() {
			@Override
			public void run(String keyTwitterAccount, Date now) {
				assertEquals("test_key1", keyTwitterAccount);
				assertEquals(new Date(99999999999001L), now);
			}
		};
		
		servlet.doGet(new HttpServletRequestMock(){
			@Override
			public String[] getParameterValues(String arg0) {
				if("key".equals(arg0)) return new String[]{"test_key1"};
				return null;
			}
			@Override
			public String getParameter(String arg0) {
				if("now".equals(arg0)) return Long.valueOf(99999999999001L).toString();
				return null;
			}
			
		}, new HttpServletResponseMock());
    }
    
    @Test
    public void testDoGetMultiAccountTask() throws IOException {
    	servlet.bot = new ITwitterBot() {
    		int count = 0;
			@Override
			public void run(String keyTwitterAccount, Date now) {
				switch (count) {
				case 0:
					assertEquals("test_key1", keyTwitterAccount);
					assertEquals(new Date(99999999999002L), now);
					break;
				case 1:
					assertEquals("test_key2", keyTwitterAccount);
					assertEquals(new Date(99999999999002L), now);
					break;
				case 2:
					assertEquals("test_key3", keyTwitterAccount);
					assertEquals(new Date(99999999999002L), now);
					break;
				default:
					fail();
					break;
				}
				count++;
			}
		};
		
		servlet.doGet(new HttpServletRequestMock(){
			@Override
			public String[] getParameterValues(String arg0) {
				if("key".equals(arg0)) return new String[]{
						"test_key1",
						"test_key2",
						"test_key3"};
				return null;
			}
			@Override
			public String getParameter(String arg0) {
				if("now".equals(arg0)) return Long.valueOf(99999999999002L).toString();
				return null;
			}
			
		}, new HttpServletResponseMock());
    }
    
    @Test
    public void testDoGetWithoutAccountKey() throws IOException {
    	servlet.bot = new ITwitterBot() {
			@Override
			public void run(String keyTwitterAccount, Date now) {
				fail();
			}
		};
		
		servlet.doGet(new HttpServletRequestMock(){
			@Override
			public String[] getParameterValues(String arg0) {
				return null;
			}
			@Override
			public String getParameter(String arg0) {
				if("now".equals(arg0)) return Long.valueOf(99999999999002L).toString();
				return null;
			}
			
		}, new HttpServletResponseMock());
    }
    
    @Test
    public void testDoGetWithoutNow() throws IOException {
    	servlet.bot = new ITwitterBot() {
			@Override
			public void run(String keyTwitterAccount, Date now) {
				fail();
			}
		};
		
		servlet.doGet(new HttpServletRequestMock(){
			@Override
			public String[] getParameterValues(String arg0) {
				if("key".equals(arg0)) return new String[]{
						"test_key1",
						"test_key2",
						"test_key3"};
				return null;
			}
			@Override
			public String getParameter(String arg0) {
				return null;
			}
			
		}, new HttpServletResponseMock());
    }
    
    @Test
    public void testDoGetWithoutParameters() throws IOException {
    	servlet.bot = new ITwitterBot() {
			@Override
			public void run(String keyTwitterAccount, Date now) {
				fail();
			}
		};
		
		servlet.doGet(new HttpServletRequestMock(),
				new HttpServletResponseMock());
    }
}