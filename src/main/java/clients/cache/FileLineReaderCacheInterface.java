package clients.cache;

import java.util.Optional;

public interface FileLineReaderCacheInterface {
	public void setLine(String fileUrl, int index, String line);
	
	public Optional<String> getLine(String fileUrl, int index);
	
	public boolean isFileCached(String fileUrl);
	
	public void initializeFile(String fileUrl);
	
	public void releaseFile(String fileUrl);
	
	public Integer getMaxIndex(String fileUrl);
}
