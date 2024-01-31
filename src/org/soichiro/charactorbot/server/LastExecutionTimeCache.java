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

import org.soichiro.charactorbot.client.PostTypeEnum;

/**
 * Cache of time of last execution.
 * implemented by Memcache.
 * Key : String keyTwitterAccount, PostTypeEnsum postTypeEnum
 * Value : Date last
 * 
 * @author soichiro
 *
 */
public class LastExecutionTimeCache {
	
	/**
	 * put last exectuion time.
	 * @param keyTwitterAccount
	 * @param postTypeEnum
	 * @param last
	 */
	@SuppressWarnings("unchecked")
	public static void put(String keyTwitterAccount, PostTypeEnum postTypeEnum, Date last){
		CacheKey key = new CacheKey(keyTwitterAccount, postTypeEnum);
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
	 * @param keyTwitterAccount
	 * @param postTypeEnum
	 * @return
	 */
	public static Date get(String keyTwitterAccount, PostTypeEnum postTypeEnum){
		CacheKey key = new CacheKey(keyTwitterAccount, postTypeEnum);
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
		private static final long serialVersionUID = -8584843216513052647L;
		final String uniqueKeyString = "org.soichiro.charactorbot.server.LastExecutionTimeCache.CacheKey";
		final String keyTwitterAccount;
		final PostTypeEnum postTypeEnum;
		/**
		 * @param keyTwitterAccount
		 * @param postTypeEnum
		 */
		public CacheKey(String keyTwitterAccount, PostTypeEnum postTypeEnum) {
			super();
			if(keyTwitterAccount == null) throw new IllegalArgumentException();
			if(postTypeEnum == null) throw new IllegalArgumentException();
			this.keyTwitterAccount = keyTwitterAccount;
			this.postTypeEnum = postTypeEnum;
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
			result = prime * result
					+ ((postTypeEnum == null) ? 0 : postTypeEnum.hashCode());
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
			if (postTypeEnum != other.postTypeEnum)
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
