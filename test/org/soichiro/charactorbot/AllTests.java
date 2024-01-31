/**
 * 
 */
package org.soichiro.charactorbot;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.soichiro.charactorbot.server.AccountTaskServletTest;
import org.soichiro.charactorbot.server.AuthenticationServiceImplTest;
import org.soichiro.charactorbot.server.CharactorbotServiceImplTest;
import org.soichiro.charactorbot.server.DatastoreLogHandlerTest;
import org.soichiro.charactorbot.server.LastExecutionTimeCacheTest;
import org.soichiro.charactorbot.server.LastPostedTimeCacheTest;
import org.soichiro.charactorbot.server.LogEntryQueueCacheTest;
import org.soichiro.charactorbot.server.PostTypeCacheTest;
import org.soichiro.charactorbot.server.PostTypeKeyMapCacheTest;
import org.soichiro.charactorbot.server.TwitterBotServletTest;
import org.soichiro.charactorbot.server.TwitterBotTest;

@RunWith(Suite.class)
@SuiteClasses( { 
		MyFirstTest.class,
		LocalDatastoreTest.class,
		LocalMemcacheTest.class,
		TaskQueueTest.class,
		AccountTaskServletTest.class,
		AuthenticationTest.class,
        AuthenticationServiceImplTest.class,
        CharactorbotServiceImplTest.class,
        DatastoreLogHandlerTest.class,
        LastExecutionTimeCacheTest.class,
        LastPostedTimeCacheTest.class,
        LogEntryQueueCacheTest.class,
        PostTypeCacheTest.class,
        PostTypeKeyMapCacheTest.class,
        TwitterBotServletTest.class,
        TwitterBotTest.class
        })
public class AllTests {
    public static void main(String[] args) {
        JUnitCore.main(AllTests.class.getName());
    }
}