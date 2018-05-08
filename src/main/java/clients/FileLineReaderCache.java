package clients;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

public class FileLineReaderCache {
	
	// Local cache only for now
	// Can also add a remote cache like redis easily.
	
	private final int port;
	private final String host;
	private HashMap<String, HashMap<Integer, String>> cache;
	private Integer currentMax;

	public FileLineReaderCache(String cacheStrategy, String host, int port) {
		if(cacheStrategy == null || "local".equals(cacheStrategy)) {
			cache = new HashMap<String, HashMap<Integer, String>>();
		}
		this.port = port;
		this.host = host;
	}

	public void setLine(String fileUrl, int index, String line) {
		cache.get(fileUrl).put(index, line);
		currentMax = null;
	}

	public Optional<String> getLine(String fileUrl, int index) {
		return Optional.of(cache.get(fileUrl).get(Integer.valueOf(index)));
	}

	public boolean isFileCached(String fileUrl) {
		return cache.containsKey(fileUrl);
	}

	public void initializeFile(String fileUrl) {
		cache.put(fileUrl, new HashMap<Integer, String>());
	}

	public void releaseFile(String fileUrl) {
		cache.remove(fileUrl);
	}

	public Integer getMaxIndex(String fileUrl) {
		if(currentMax != null) {
			return currentMax;
		}
		currentMax = Collections.max(cache.get(fileUrl).keySet());
		return currentMax;
	}
}
