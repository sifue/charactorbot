/**
 * 
 */
package org.soichiro.charactorbot.server;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.soichiro.charactorbot.client.PostTypeEnum;

import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class LastExecutionTimeCacheTest {

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
    	Date date = LastExecutionTimeCache.get("testKey1", PostTypeEnum.NOMAL_POST);
    	assertNull(date);
    }
    
    @Test
    public void testPutAndGet() {
    	LastExecutionTimeCache.put("testKey1", PostTypeEnum.NOMAL_POST, new Date(99999990001L));
    	Date date = LastExecutionTimeCache.get("testKey1", PostTypeEnum.NOMAL_POST);
    	assertEquals(new Date(99999990001L), date);
    }

    @Test
    public void testUpdate() {
    	LastExecutionTimeCache.put("testKey1", PostTypeEnum.NOMAL_POST, new Date(99999990002L));
    	Date date = LastExecutionTimeCache.get("testKey1", PostTypeEnum.NOMAL_POST);
    	assertEquals(new Date(99999990002L), date);
    }
}
