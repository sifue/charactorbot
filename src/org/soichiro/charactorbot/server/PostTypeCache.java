/**
 * 
 */
package org.soichiro.charactorbot.server;

import java.io.Serializable;
import java.util.Collections;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;

import org.soichiro.charactorbot.client.CPostType;

/**
 * Cache of PostType.
 * implemented by Memcache.
 * Key : String keyPostType
 * Value : CPostType cPostType
 * 
 * @author soichiro
 *
 */
public class PostTypeCache {
	
	/**
	 * put cPostType.
	 * @param keyPostType
	 * @param cPostType
	 */
	@SuppressWarnings("unchecked")
	public static void put(String keyPostType, CPostType cPostType){
		CacheKey key = new CacheKey(keyPostType);
		Cache cache = null;
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
        } catch (CacheException e) {
        	throw new RuntimeException(e);
        }
        cache.put(key, cPostType);
	}
	
	/**
	 * get cPostType.
	 * @param keyPostType
	 * @return
	 */
	public static CPostType get(String keyPostType){
		CacheKey key = new CacheKey(keyPostType);
        Cache cache = null;
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
        } catch (CacheException e) {
        	throw new RuntimeException(e);
        }
        CPostType cPostType = (CPostType)cache.get(key);
        return cPostType;
	}
	
	/**
	 * remove cPostType.
	 * @param keyPostType
	 * @return
	 */
	public static CPostType remove(String keyPostType){
		CacheKey key = new CacheKey(keyPostType);
        Cache cache = null;
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
        } catch (CacheException e) {
        	throw new RuntimeException(e);
        }
        CPostType cPostType = (CPostType)cache.remove(key);
        return cPostType;
	}
	
	/**
	 * Key of memcache
	 * @author soichiro
	 *
	 */
	private static class CacheKey implements Serializable{

		/** serialVersionUID */
		private static final long serialVersionUID = -2765797737236644970L;
		
		final String uniqueKeyString = "org.soichiro.charactorbot.server.PostTypeCache.CacheKey";
		final String keyPostType;
		
		/**
		 * @param keyPostType
		 */
		public CacheKey(String keyTwitterAccount) {
			super();
			this.keyPostType = keyTwitterAccount;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((keyPostType == null) ? 0 : keyPostType.hashCode());
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
			if (keyPostType == null) {
				if (other.keyPostType != null)
					return false;
			} else if (!keyPostType.equals(other.keyPostType))
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
