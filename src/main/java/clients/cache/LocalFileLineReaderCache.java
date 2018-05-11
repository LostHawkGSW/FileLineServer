package clients.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

public class LocalFileLineReaderCache extends FileLineReaderCache implements FileLineReaderCacheInterface {
	private HashMap<String, HashMap<Integer, String>> cache;
	
	public LocalFileLineReaderCache() {
		super();
		cache = new HashMap<String, HashMap<Integer, String>>();
	}
	
	public void setLine(String fileUrl, int index, String line) {
		cache.get(fileUrl).put(index, line);
		if(index > currentMax.get(fileUrl)) {
			currentMax.put(fileUrl, Integer.valueOf(index));
		}
	}

	public Optional<String> getLine(String fileUrl, int index) {
		String value = null;
		try {
			value = cache.get(fileUrl).get(Integer.valueOf(index));
		} catch(NullPointerException e) {
			
		}
		return Optional.ofNullable(value);
	}

	public boolean isFileCached(String fileUrl) {
		return cache.containsKey(fileUrl);
	}

	public void initializeFile(String fileUrl) {
		cache.put(fileUrl, new HashMap<Integer, String>());
		currentMax.put(fileUrl, Integer.valueOf(0));
	}

	public void releaseFile(String fileUrl) {
		cache.remove(fileUrl);
	}

	public Integer getMaxIndex(String fileUrl) {
		Integer max = currentMax.get(fileUrl);
		if(max != null) {
			return max;
		}
		try {
			max = Collections.max(cache.get(fileUrl).keySet());
		} catch(NoSuchElementException | NullPointerException  e) {
			max = 0;
		}
		return max;
	}
}
