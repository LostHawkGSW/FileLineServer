package routers;

import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import org.eclipse.jetty.http.HttpStatus;

import results.BaseResult;
import services.FileReaderService;

@Path("/lines")
@Produces(MediaType.APPLICATION_JSON)
public class FileReaderRouter {
	private final FileReaderService fileReaderService;

	public FileReaderRouter(final FileReaderService fileReaderService) {
		this.fileReaderService = fileReaderService;
	}

	@GET
	@Timed
	@Path("{index}")
	public BaseResult<String> getLine(@PathParam("index") int index, @HeaderParam("fileName") String fileName) {
		if(fileName == null) {
			fileName = fileReaderService.getDefaultFileName();
		}
		Optional<String> line = fileReaderService.getLine(fileName, index);
		return new BaseResult<String>(
			line.isPresent() ? HttpStatus.OK_200 : HttpStatus.REQUEST_ENTITY_TOO_LARGE_413, 
			line.orElse(null)
		);
	}
}
