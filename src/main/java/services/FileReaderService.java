package services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

import clients.FileLineReaderCache;

public class FileReaderService {

	private FileLineReaderCache cache;
	private String defaultFileName;

	// This is currently configured to work only for one file.
	// This can easily be changed to work for many files  by passing a list of fileUrls
	// And changing the config to be able to handle a list of file urls
	public FileReaderService(String fileUrl, String cacheStrategy, String host, int port) {
		defaultFileName = fileUrl;
		populateCache(fileUrl, cacheStrategy, host, port);
	}

	private void populateCache(String fileUrl, String cacheStrategy, String host, int port) {
		if (cache == null) {
			cache = new FileLineReaderCache(cacheStrategy, host, port);
		}
		File file = new File("file.txt");
		try {
			FileUtils.copyURLToFile(new URL(fileUrl), file);
			if (!cache.isFileCached(fileUrl)) {
				byte[] fileBytes = Files.readAllBytes(file.toPath());
				char singleChar;
				int index = 1;
				StringBuffer currentLine = new StringBuffer();
				cache.initializeFile(fileUrl);
				for (byte b : fileBytes) {
					singleChar = (char) b;
					if (singleChar == '\n') {
						cache.setLine(fileUrl, index, currentLine.toString());
						currentLine = new StringBuffer();
						index++;
					} else {
						currentLine.append(singleChar);
					}
				}
			}
		} catch (IOException e) {
			cache.releaseFile(fileUrl);
		}
	}

	public Optional<String> getLine(String fileUrl, int index) {
		return cache.getLine(fileUrl, index);
	}

	public Optional<String> performHealthCheck(String fileUrl) {
		if(cache == null || !cache.isFileCached(fileUrl)) {
			return Optional.of("Cache is not populated correctly for file: " + fileUrl);
		}
		return Optional.empty();
	}
	
	public String getDefaultFileName() {
		return defaultFileName;
	}
	
	public boolean isIndexBeyondMax(String fileUrl, int index) {
		if(cache == null || !cache.isFileCached(fileUrl) || index > cache.getMaxIndex(fileUrl)) {
			return true;
		}
		return false;
	}
}
