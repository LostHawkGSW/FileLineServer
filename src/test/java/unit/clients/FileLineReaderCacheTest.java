package unit.clients;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Resources;

import clients.FileLineReaderCache;

public class FileLineReaderCacheTest {
	
	private static final String FILE_NAME = "test_file";
	private static final FileLineReaderCache cache = new FileLineReaderCache("local", "local", 0);
	private static File file = null;
	private static final String BASIC_SENTENCE = "Hello, World!";
	
	@Before
	public void setUp() throws Exception {
		file = new File(Resources.getResource(FILE_NAME).toURI());
		cache.initializeFile(FILE_NAME);
	}

	@Test
	public void testGetAndSetLine() {
		assertFalse(cache.getLine(FILE_NAME, 1).isPresent());
		cache.setLine(FILE_NAME, 1, BASIC_SENTENCE);
		assertTrue(cache.getLine(FILE_NAME, 1).isPresent());
		assertEquals(cache.getLine(FILE_NAME, 1), Optional.of(BASIC_SENTENCE));
	}
	
	@Test
	public void testIsFileCached() {
		assertFalse(cache.isFileCached("SomeNewFile"));
		assertTrue(cache.isFileCached(FILE_NAME));
	}

	@Test
	public void testInitializeAndReleaseFile() {
		String newFileName = "SomeNewFile";
		assertFalse(cache.isFileCached(newFileName));
		cache.initializeFile(newFileName);
		assertTrue(cache.isFileCached(newFileName));
	}
	
	@Test
	public void testGetMaxIndex() {
		assertEquals(cache.getMaxIndex(FILE_NAME), Integer.valueOf(0));
		cache.setLine(FILE_NAME, 19, BASIC_SENTENCE);
		assertEquals(cache.getMaxIndex(FILE_NAME), Integer.valueOf(19));
		assertEquals(cache.getMaxIndex("SomeNonExistantFile"), Integer.valueOf(0));
	}
	
	@Test
	public void testWriteFileToCache() throws IOException {
		cache.writeFileToCache(FILE_NAME, file);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "";
		for(int index = 1; (line = br.readLine()) != null; index++) {
			assertEquals(line, cache.getLine(FILE_NAME, index).get());
		}
		br.close();
	}
}
