package clients.cache;

import java.time.Duration;
import java.util.Optional;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisFileLineReaderCache extends FileLineReaderCache implements FileLineReaderCacheInterface {
	private JedisPool redisCachePool;

	public RedisFileLineReaderCache(String host, int port) {
		this.redisCachePool =  new JedisPool(buildPoolConfig(), host, port);
	}
	
	public void setLine(String fileUrl, int index, String line) {
		redisCachePool.getResource().hset(fileUrl, String.valueOf(index), line);
		if(index > currentMax.get(fileUrl)) {
			currentMax.put(fileUrl, Integer.valueOf(index));
		}
	}

	public Optional<String> getLine(String fileUrl, int index) {
		return Optional.ofNullable(redisCachePool.getResource().hget(fileUrl, String.valueOf(index)));
	}

	public boolean isFileCached(String fileUrl) {
		return redisCachePool.getResource().exists(fileUrl);
	}

	public void initializeFile(String fileUrl) {
		redisCachePool.getResource().hset(fileUrl, String.valueOf(-1), "");
		currentMax.put(fileUrl, Integer.valueOf(0));
	}

	public void releaseFile(String fileUrl) {
		redisCachePool.getResource().del(fileUrl);
	}

	public Integer getMaxIndex(String fileUrl) {
		Integer max = currentMax.get(fileUrl);
		if(max != null) {
			return max;
		} else {
			return Integer.valueOf(0);
		}
	}
	
	private JedisPoolConfig buildPoolConfig() {
	    final JedisPoolConfig poolConfig = new JedisPoolConfig();
	    poolConfig.setMaxTotal(128);
	    poolConfig.setMaxIdle(128);
	    poolConfig.setMinIdle(16);
	    poolConfig.setTestOnBorrow(true);
	    poolConfig.setTestOnReturn(true);
	    poolConfig.setTestWhileIdle(true);
	    poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
	    poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
	    poolConfig.setNumTestsPerEvictionRun(3);
	    poolConfig.setBlockWhenExhausted(true);
	    return poolConfig;
	}
}
