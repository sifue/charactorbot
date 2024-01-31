/**
 * 
 */
package org.soichiro.charactorbot.server;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.soichiro.charactorbot.client.AuthenticationService;
import org.soichiro.charactorbot.client.CharactorbotRPCException;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

public class AuthenticationServiceImplTest {

    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalUserServiceTestConfig())
            .setEnvIsAdmin(true)
            .setEnvIsLoggedIn(true)
            .setEnvEmail("charactorbot")
            .setEnvAuthDomain("gmail.com");

    
    private final AuthenticationService servlet = new AuthenticationServiceImpl();
    
    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testGetAuthenticatedUserEmail() throws CharactorbotRPCException {
       assertEquals("charactorbot", servlet.getAuthenticatedUserEmail().getEmail());
       assertEquals("gmail.com", servlet.getAuthenticatedUserEmail().getAuthDomain());
    }
    
    @Test
    public void testGetLoginURL() throws CharactorbotRPCException {
       assertEquals("/_ah/login?continue=%2F", servlet.getLoginURL());
    }
    
    @Test
    public void testGetLogoutURL() throws CharactorbotRPCException {
       assertEquals("/_ah/logout?continue=%2F", servlet.getLogoutURL());
    }
    
    
}