package services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

import clients.cache.FileLineReaderCache;
import clients.cache.FileLineReaderCacheFactory;

public class FileReaderService {

	private FileLineReaderCache cache;
	private String defaultFileName;
	
	public String getDefaultFileName() {
		return defaultFileName;
	}
	
	public FileLineReaderCache getFileLineReaderCache() {
		return cache;
	}

	// This is currently configured to work only for one file.
	// This can easily be changed to work for many files  by passing a list of fileUrls
	// And changing the config to be able to handle a list of file urls
	public FileReaderService(String fileUrl, String cacheStrategy, String host, int port) {
		defaultFileName = fileUrl;
		populateCache(fileUrl, cacheStrategy, host, port);
	}

	private void populateCache(String fileUrl, String cacheStrategy, String host, int port) {
		if (getFileLineReaderCache() == null) {
			cache = FileLineReaderCacheFactory.getFileLineReaderCache(cacheStrategy, host, port);
		}
		if(cache.isFileCached(fileUrl)) {
			return;
		}
		File file = new File("file.txt");
		try {
			FileUtils.copyURLToFile(new URL(fileUrl), file);
			if (!getFileLineReaderCache().isFileCached(fileUrl)) {
				getFileLineReaderCache().writeFileToCache(fileUrl, file);
			}
		} catch (IOException e) {
			getFileLineReaderCache().releaseFile(fileUrl);
		}
	}

	public Optional<String> getLine(String fileUrl, int index) {
		return getFileLineReaderCache().getLine(fileUrl, index);
	}

	public Optional<String> performHealthCheck(String fileUrl) {
		if(getFileLineReaderCache() == null || !getFileLineReaderCache().isFileCached(fileUrl)) {
			return Optional.of("Cache is not populated correctly for file: " + fileUrl);
		}
		return Optional.empty();
	}
	
	public boolean isIndexBeyondMax(String fileUrl, int index) {
		if(getFileLineReaderCache() == null || !getFileLineReaderCache().isFileCached(fileUrl) || index > getFileLineReaderCache().getMaxIndex(fileUrl)) {
			return true;
		}
		return false;
	}
}
