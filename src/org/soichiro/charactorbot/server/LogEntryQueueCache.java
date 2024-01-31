/**
 * 
 */
package org.soichiro.charactorbot.server;

import java.io.Serializable;
import java.util.Collections;
import java.util.Queue;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;

import org.soichiro.charactorbot.client.CLogEntry;

/**
 * Cache of Queue of LogEntry.
 * implemented by Memcache.
 * Key : String keyTwitterAccount
 * Value : Queue<LogEntry>
 * 
 * @author soichiro
 *
 */
public class LogEntryQueueCache {
	
	/**
	 * put log entry queue.
	 * @param keyTwitterAccount
	 * @param queue
	 */
	@SuppressWarnings("unchecked")
	public static void put(String keyTwitterAccount, Queue<CLogEntry> queue){
		CacheKey key = new CacheKey(keyTwitterAccount);
		Cache cache = null;
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
        } catch (CacheException e) {
        	throw new RuntimeException(e);
        }
        cache.put(key, queue);
	}
	
	/**
	 * get log entry queue.
	 * @param keyTwitterAccount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Queue<CLogEntry> get(String keyTwitterAccount){
		CacheKey key = new CacheKey(keyTwitterAccount);
        Cache cache = null;
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
        } catch (CacheException e) {
        	throw new RuntimeException(e);
        }
        Queue<CLogEntry> queue = (Queue<CLogEntry>)cache.get(key);
        return queue;
	}
	
	/**
	 * Key of memcache
	 * @author soichiro
	 *
	 */
	private static class CacheKey implements Serializable{
		/** serialVersionUID */
		private static final long serialVersionUID = 8361766554215704175L;
		
		final String uniqueKeyString = "org.soichiro.charactorbot.server.LogEntryQueueCache.CacheKey";
		final String keyTwitterAccount;
		
		/**
		 * @param keyTwitterAccount
		 */
		public CacheKey(String keyTwitterAccount) {
			super();
			this.keyTwitterAccount = keyTwitterAccount;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((keyTwitterAccount == null) ? 0 : keyTwitterAccount
							.hashCode());
			result = prime
					* result
					+ ((uniqueKeyString == null) ? 0 : uniqueKeyString
							.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CacheKey other = (CacheKey) obj;
			if (keyTwitterAccount == null) {
				if (other.keyTwitterAccount != null)
					return false;
			} else if (!keyTwitterAccount.equals(other.keyTwitterAccount))
				return false;
			if (uniqueKeyString == null) {
				if (other.uniqueKeyString != null)
					return false;
			} else if (!uniqueKeyString.equals(other.uniqueKeyString))
				return false;
			return true;
		}

	}
}
