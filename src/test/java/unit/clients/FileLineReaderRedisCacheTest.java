package unit.clients;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Resources;

import clients.cache.FileLineReaderCache;
import clients.cache.FileLineReaderCacheFactory;
import external_clients.mock_redis_server.RedisServer;

public class FileLineReaderRedisCacheTest {
	private static final String FILE_NAME = "test_file";
	private static final String NEW_FILE_NAME = "SomeNewFile";
	private static FileLineReaderCache cache;
	private static File file = null;
	private static final String BASIC_SENTENCE = "Hello, World!";
	private static RedisServer server; // Embedded redis server used for testing purposes.
	private static final int REDIS_PORT = 6379;

	@Before
	public void setUp() throws Exception {
		server = new RedisServer(REDIS_PORT);
		server.start();
		cache = FileLineReaderCacheFactory.getFileLineReaderCache("remote", "localhost", REDIS_PORT);
		file = new File(Resources.getResource(FILE_NAME).toURI());
		cache.initializeFile(FILE_NAME);
		cache.releaseFile(NEW_FILE_NAME);
	}

	@After
	public void tearDown() {
		server.stop();
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
		assertFalse(cache.isFileCached(NEW_FILE_NAME));
		assertTrue(cache.isFileCached(FILE_NAME));
	}

	@Test
	public void testInitializeAndReleaseFile() {
		assertFalse(cache.isFileCached(NEW_FILE_NAME));
		cache.initializeFile(NEW_FILE_NAME);
		assertTrue(cache.isFileCached(NEW_FILE_NAME));
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
		// Not going through every line; there is a strange memory issue with the mock redis client that is being used
		// Runs out of memory around reading 23 lines; just testing first and last line.
		assertEquals("package com.example.helloworld;", cache.getLine(FILE_NAME, 1).get());
		assertEquals("}", cache.getLine(FILE_NAME, 103).get());
	}
}
