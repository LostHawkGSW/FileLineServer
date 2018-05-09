package integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.File;

import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.io.Resources;

import clients.FileLineReaderCache;
import io.dropwizard.testing.junit.ResourceTestRule;
import routers.FileReaderRouter;
import services.FileReaderService;

@RunWith(PowerMockRunner.class)
@PrepareForTest( FileReaderService.class )
public class FileReaderRouterTest {
	private static final String FILE_NAME = "test_file";
	private static final String FILE_READER_ROUTER_ENDPOINT = "/lines";
	
	private static final FileReaderService service = mock(FileReaderService.class);
	private static final FileLineReaderCache cache = new FileLineReaderCache("local", "local", 0);
	
	@Rule
    private final ResourceTestRule resources = ResourceTestRule.builder().addResource(new FileReaderRouter(service)).build();

	@Before
	public void setUp() throws Exception {
		cache.writeFileToCache(FILE_NAME,  new File(Resources.getResource(FILE_NAME).toURI()));
		when(service.getDefaultFileName()).thenReturn(FILE_NAME);
		when(service.getFileLineReaderCache()).thenReturn(cache);
		when(service.getLine(any(String.class), any(Integer.class))).thenCallRealMethod();
		when(service.isIndexBeyondMax(any(String.class), any(Integer.class))).thenCallRealMethod();
	}
	
	@After
    public void tearDown(){
        reset(service);
    }

	@Test
	public void testGetGoodLine() {
		Response response = resources.client().target(FILE_READER_ROUTER_ENDPOINT + "/1").request().get();
		assertEquals(HttpStatus.OK_200, response.getStatus());
		assertEquals("package com.example.helloworld;", response.readEntity(String.class));
	}
	
	@Test
	public void testGetBadOutOfBoundsLowLine() {
		Response response = resources.client().target(FILE_READER_ROUTER_ENDPOINT + "/0").request().get();
		assertEquals(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE_416, response.getStatus());
	}
	
	@Test
	public void testGetBadOutOfBoundsHighLine() {
		Response response = resources.client().target(FILE_READER_ROUTER_ENDPOINT + "/10000").request().get();
		assertEquals(HttpStatus.REQUEST_ENTITY_TOO_LARGE_413, response.getStatus());
	}
}