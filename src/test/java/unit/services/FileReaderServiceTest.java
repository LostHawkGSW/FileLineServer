package unit.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.File;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Resources;

import clients.FileLineReaderCache;
import services.FileReaderService;

public class FileReaderServiceTest {
	private static final String FILE_NAME = "test_file";
	
	private static final FileReaderService service = mock(FileReaderService.class);
	private static final FileLineReaderCache cache = new FileLineReaderCache("local", "local", 0);


	@Before
	public void setUp() throws Exception {
		cache.writeFileToCache(FILE_NAME,  new File(Resources.getResource(FILE_NAME).toURI()));
		when(service.getDefaultFileName()).thenReturn(FILE_NAME);
		when(service.getFileLineReaderCache()).thenReturn(cache);
		when(service.getLine(any(String.class), any(Integer.class))).thenCallRealMethod();
		when(service.isIndexBeyondMax(any(String.class), any(Integer.class))).thenCallRealMethod();
	}
	
	@After
    public void tearDown() {
        reset(service);
    }
	
	@Test
	public void testGetLine() {
		assertEquals(service.getLine(FILE_NAME, 1), Optional.of("package com.example.helloworld;"));
		assertEquals(service.getLine(FILE_NAME, 103), Optional.of("}"));
		assertEquals(service.getLine(FILE_NAME, 0), Optional.empty());
		assertEquals(service.getLine(FILE_NAME, 104), Optional.empty());
		assertEquals(service.getLine("SomeNonExistantFile", 0), Optional.empty());
	}

	@Test
	public void testIsIndexBeyondMax() {
		assertFalse(service.isIndexBeyondMax(FILE_NAME, 1));
		assertFalse(service.isIndexBeyondMax(FILE_NAME, 103));
		assertFalse(service.isIndexBeyondMax(FILE_NAME, 0));
		assertTrue(service.isIndexBeyondMax(FILE_NAME, 104));
		assertTrue(service.isIndexBeyondMax("SomeNonExistantFile", 0));
	}
}
