package com.xxl.job.admin.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * local cache tool
 *
 * @author xuxueli 2018-01-22 21:37:34
 */
public class LocalCacheUtil {

	private static final ConcurrentMap<String, LocalCacheData> CACHE_REPOSITORY = new ConcurrentHashMap<>(); // 类型建议用抽象父类，兼容性更好；

	private static class LocalCacheData {

		private String key;

		private Object val;

		private long timeoutTime;

		public LocalCacheData() {
		}

		public LocalCacheData(String key, Object val, long timeoutTime) {
			this.key = key;
			this.val = val;
			this.timeoutTime = timeoutTime;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Object getVal() {
			return val;
		}

		public void setVal(Object val) {
			this.val = val;
		}

		public long getTimeoutTime() {
			return timeoutTime;
		}

		public void setTimeoutTime(long timeoutTime) {
			this.timeoutTime = timeoutTime;
		}

	}

	/**
	 * set cache
	 * @param key
	 * @param val
	 * @param cacheTime
	 * @return
	 */
	public static boolean set(String key, Object val, long cacheTime) {

		// clean timeout cache, before set new cache (avoid cache too much)
		cleanTimeoutCache();

		// set new cache
		if (key == null || key.trim().isEmpty()) {
			return false;
		}
		if (val == null) {
			remove(key);
		}
		if (cacheTime <= 0) {
			remove(key);
		}
		long timeoutTime = System.currentTimeMillis() + cacheTime;
		LocalCacheData localCacheData = new LocalCacheData(key, val, timeoutTime);
		CACHE_REPOSITORY.put(localCacheData.getKey(), localCacheData);
		return true;
	}

	/**
	 * remove cache
	 * @param key
	 * @return
	 */
	public static boolean remove(String key) {
		if (key == null || key.trim().isEmpty()) {
			return false;
		}
		CACHE_REPOSITORY.remove(key);
		return true;
	}

	/**
	 * get cache
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		if (key == null || key.trim().isEmpty()) {
			return null;
		}
		LocalCacheData localCacheData = CACHE_REPOSITORY.get(key);
		if (localCacheData != null && System.currentTimeMillis() < localCacheData.getTimeoutTime()) {
			return localCacheData.getVal();
		}
		else {
			remove(key);
			return null;
		}
	}

	/**
	 * clean timeout cache
	 */
	public static void cleanTimeoutCache() {
		if (!CACHE_REPOSITORY.keySet().isEmpty()) {
			for (Map.Entry<String, LocalCacheData> entry : CACHE_REPOSITORY.entrySet()) {
				LocalCacheData localCacheData = entry.getValue();
				if (localCacheData != null && System.currentTimeMillis() >= localCacheData.getTimeoutTime()) {
					CACHE_REPOSITORY.remove(entry.getKey());
				}
			}
		}
	}

}
