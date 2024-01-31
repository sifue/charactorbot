/**
 * 
 */
package org.soichiro.charactorbot.server;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;

/**
 * Cache of time of last posted.
 * implemented by Memcache.
 * Key : String keyPost
 * Value : Date last
 * 
 * @author soichiro
 *
 */
public class LastPostedTimeCache {
	
	/**
	 * put last exectuion time.
	 * @param keyTwitterAccount
	 * @param last
	 */
	@SuppressWarnings("unchecked")
	public static void put(String keyPost, Date last){
		CacheKey key = new CacheKey(keyPost);
		Cache cache = null;
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
        } catch (CacheException e) {
        	throw new RuntimeException(e);
        }
        cache.put(key, last);
	}
	
	/**
	 * get last exectuion time.
	 * @param keyPost
	 * @return
	 */
	public static Date get(String keyPost){
		CacheKey key = new CacheKey(keyPost);
        Cache cache = null;
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
        } catch (CacheException e) {
        	throw new RuntimeException(e);
        }
        Date last = (Date)cache.get(key);
        return last;
	}
	
	/**
	 * Key of memcache
	 * @author soichiro
	 *
	 */
	private static class CacheKey implements Serializable{
		private static final long serialVersionUID = -1573607637166995844L;
		final String uniqueKeyString = "org.soichiro.charactorbot.server.LastPostedTimeCache.CacheKey";
		final String keyPost;
		
		/**
		 * @param keyPost
		 */
		public CacheKey(String keyPost) {
			super();
			if(keyPost == null) throw new IllegalArgumentException();
			this.keyPost = keyPost;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((keyPost == null) ? 0 : keyPost.hashCode());
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
			if (keyPost == null) {
				if (other.keyPost != null)
					return false;
			} else if (!keyPost.equals(other.keyPost))
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
