package clients.cache;

import redis.clients.jedis.JedisPool;

public class FileLineReaderCacheFactory {
	private static FileLineReaderCache cache;
	
	public static FileLineReaderCache getFileLineReaderCache(String cacheStrategy, String host, int port) {
		if(cache != null) {
			return cache;
		}
		if(cacheStrategy == null || "local".equals(cacheStrategy)) {
			cache =  new LocalFileLineReaderCache();
		} else {
			cache = new RedisFileLineReaderCache(host, port);
		}
		return cache;
	}
}
