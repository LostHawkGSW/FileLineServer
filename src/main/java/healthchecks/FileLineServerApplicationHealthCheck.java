package healthchecks;

import java.util.Optional;

import com.codahale.metrics.health.HealthCheck;

import services.FileReaderService;

public class FileLineServerApplicationHealthCheck extends HealthCheck {
	private static final String HEALTHY = "Application is healthy... :)";
	private static final String UNHEALTH = "Application is unhealthy... :(";
	
	private final FileReaderService fileReaderService;
	
	public FileLineServerApplicationHealthCheck(FileReaderService fileReaderService) {
		this.fileReaderService = fileReaderService;
	}
	
	@Override
	public Result check() throws Exception {
		Optional<String> cacheHealthCheck = fileReaderService.performHealthCheck(fileReaderService.getDefaultFileName());
		
		if(!cacheHealthCheck.isPresent()) {
			return Result.healthy(HEALTHY);
		} else {
			return Result.unhealthy(UNHEALTH);
		}
	}
}
