/**
 * 
 */
package org.soichiro.charactorbot.server;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.Queue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.soichiro.charactorbot.client.CLogEntry;

import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class LogEntryQueueCacheTest {

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
    	Queue<CLogEntry> queue = LogEntryQueueCache.get("testKey1");
    	assertNull(queue);
    }
    
    @Test
    public void testPutAndGet() {
    	Queue<CLogEntry> inputQueue = new ArrayDeque<CLogEntry>();
    	CLogEntry logEntry1 = new CLogEntry();
    	logEntry1.setLogText("testLogText1");
    	inputQueue.add(logEntry1);
    	CLogEntry logEntry2 = new CLogEntry();
    	logEntry2.setLogText("testLogText2");
    	inputQueue.add(logEntry2);
    	LogEntryQueueCache.put("testKey1", inputQueue);
    	
    	Queue<CLogEntry> queue = LogEntryQueueCache.get("testKey1");
    	assertEquals(inputQueue.poll().getLogText(), queue.poll().getLogText());
    	assertEquals(inputQueue.poll().getLogText(), queue.poll().getLogText());
    	assertEquals(inputQueue.poll(), queue.poll());
    }

    @Test
    public void testUpdate() {
    	Queue<CLogEntry> inputQueue = new ArrayDeque<CLogEntry>();
    	CLogEntry logEntry1 = new CLogEntry();
    	logEntry1.setLogText("testLogText1");
    	inputQueue.add(logEntry1);
    	CLogEntry logEntry2 = new CLogEntry();
    	logEntry2.setLogText("testLogText2");
    	inputQueue.add(logEntry2);
    	CLogEntry logEntry3 = new CLogEntry();
    	logEntry2.setLogText("testLogText3");
    	inputQueue.add(logEntry3);
    	LogEntryQueueCache.put("testKey1", inputQueue);
    	
    	Queue<CLogEntry> queue = LogEntryQueueCache.get("testKey1");
    	assertEquals(inputQueue.poll().getLogText(), queue.poll().getLogText());
    	assertEquals(inputQueue.poll().getLogText(), queue.poll().getLogText());
    	assertEquals(inputQueue.poll().getLogText(), queue.poll().getLogText());
    	assertEquals(inputQueue.poll(), queue.poll());
    }
}
