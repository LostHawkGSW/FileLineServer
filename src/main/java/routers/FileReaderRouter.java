package routers;

import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;

import com.codahale.metrics.annotation.Timed;

import services.FileReaderService;

@Path("/lines")
public class FileReaderRouter {
	private final FileReaderService fileReaderService;

	public FileReaderRouter(final FileReaderService fileReaderService) {
		this.fileReaderService = fileReaderService;
	}

	@GET
	@Timed
	@Produces("text/plain")
	@Path("{index}")
	public Response getLine(@PathParam("index") int index, @HeaderParam("fileName") String fileName) {
		if(index < 1) {
			return Response.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE_416).build();
		}
		if(fileName == null) {
			fileName = fileReaderService.getDefaultFileName();
		}
		if(fileReaderService.isIndexBeyondMax(fileName, index)) {
			return Response.status(HttpStatus.REQUEST_ENTITY_TOO_LARGE_413).build();
		}
		try {
			Optional<String> line = fileReaderService.getLine(fileName, index);
			if(line.isPresent()) {
				return Response.ok(line.orElse("")).build();
			} else {
				return Response.status(HttpStatus.REQUEST_ENTITY_TOO_LARGE_413).build();
			}
		} catch(RuntimeException e) {
			return Response.status(HttpStatus.SERVICE_UNAVAILABLE_503).build();
		}
	}
}
