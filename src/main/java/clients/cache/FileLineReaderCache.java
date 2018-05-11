package clients.cache;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public abstract class FileLineReaderCache implements FileLineReaderCacheInterface {
	protected HashMap<String, Integer> currentMax;
	
	public FileLineReaderCache() {
		currentMax = new HashMap<String, Integer>();
	}
	
	public void writeFileToCache(String fileUrl, File file) throws IOException {
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		char singleChar;
		int index = 1;
		StringBuffer currentLine = new StringBuffer();
		initializeFile(fileUrl);
		for (byte b : fileBytes) {
			singleChar = (char) b;
			if (singleChar == '\n') {
				setLine(fileUrl, index, currentLine.toString());
				currentLine = new StringBuffer();
				index++;
			} else {
				currentLine.append(singleChar);
			}
		}
		// Don't forget to add the last line!
		setLine(fileUrl, index, currentLine.toString());
	}
}
