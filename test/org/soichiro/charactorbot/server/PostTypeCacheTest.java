/**
 * 
 */
package org.soichiro.charactorbot.server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.soichiro.charactorbot.client.CPostType;

import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class PostTypeCacheTest {

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
    	CPostType cPostType = PostTypeCache.get("testKey1");
    	assertNull(cPostType);
    }
    
    @Test
    public void testPutAndGet() {
    	CPostType cPostTypeInput = new CPostType();
    	cPostTypeInput.setKey("postTypeKey1");
    	PostTypeCache.put("testKey1", cPostTypeInput);
    	
    	CPostType cPostType = PostTypeCache.get("testKey1");
    	assertEquals(cPostTypeInput, cPostType);
    }

    @Test
    public void testUpdate() {
    	CPostType cPostTypeInput = new CPostType();
    	cPostTypeInput.setKey("postTypeKey2");
    	PostTypeCache.put("testKey1", cPostTypeInput);
    	
    	CPostType cPostType = PostTypeCache.get("testKey1");
    	assertEquals(cPostTypeInput, cPostType);
    }
    
    @Test
    public void testRemove() {
    	CPostType cPostTypeInput = new CPostType();
    	cPostTypeInput.setKey("postTypeKey3");
    	PostTypeCache.put("testKey2", cPostTypeInput);
    	
    	CPostType cPostType = PostTypeCache.remove("testKey2");
    	assertEquals(cPostTypeInput, cPostType);
    	
    	CPostType cPostTypeRemoved = PostTypeCache.get("testKey2");
    	assertNull(cPostTypeRemoved);
    }
}
