/**
 * 
 */
package org.soichiro.charactorbot.server;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.soichiro.charactorbot.client.PostTypeEnum;

import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class PostTypeKeyMapCacheTest {

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
    	Map<PostTypeEnum, String> map = PostTypeKeyMapCache.get("testKey1");
    	assertNull(map);
    }
    
    @Test
    public void testPutAndGet() {
    	
    	Map<PostTypeEnum, String> mapInput = new HashMap<PostTypeEnum, String>();
    	mapInput.put(PostTypeEnum.NOMAL_POST, "value1");
    	mapInput.put(PostTypeEnum.REPLY, "value2");
    	PostTypeKeyMapCache.put("testKey1", mapInput);
    	
    	Map<PostTypeEnum, String> map = PostTypeKeyMapCache.get("testKey1");
    	assertEquals(mapInput, map);
    }

    @Test
    public void testUpdate() {
     	Map<PostTypeEnum, String> mapInput = new HashMap<PostTypeEnum, String>();
    	mapInput.put(PostTypeEnum.NOMAL_POST, "value1-1");
    	mapInput.put(PostTypeEnum.REPLY, "value2-1");
    	mapInput.put(PostTypeEnum.REPLY_FOR_ME, "value3");
    	mapInput.put(PostTypeEnum.WELCOME_POST, "value4");
    	PostTypeKeyMapCache.put("testKey1", mapInput);
    	
    	Map<PostTypeEnum, String> map = PostTypeKeyMapCache.get("testKey1");
    	assertEquals(mapInput, map);
    }
}
