/**
 * 
 */
package org.soichiro.charactorbot.server;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class LastPostedTimeCacheTest {

    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testGet() {
    	Date date = LastPostedTimeCache.get("testKey1");
    	assertNull(date);
    }
    
    @Test
    public void testPutAndGet() {
    	LastPostedTimeCache.put("testKey1", new Date(99999990001L));
    	Date date = LastPostedTimeCache.get("testKey1");
    	assertEquals(new Date(99999990001L), date);
    }

    @Test
    public void testUpdate() {
    	LastPostedTimeCache.put("testKey1", new Date(99999990002L));
    	Date date = LastPostedTimeCache.get("testKey1");
    	assertEquals(new Date(99999990002L), date);
    }
}
