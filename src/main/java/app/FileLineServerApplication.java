package app;

import healthchecks.FileLineServerApplicationHealthCheck;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import routers.FileReaderRouter;
import services.FileReaderService;

public class FileLineServerApplication extends Application<FileLineServerConfiguration> {
	private static FileReaderService fileReaderService;
	
	public static void main(String[] args) throws Exception {
		new FileLineServerApplication().run(args);
	}
	
	@Override
	public void initialize(Bootstrap<FileLineServerConfiguration> bootstrap) {
		bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
	        bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor())
		);
	}

	@Override
	public void run(FileLineServerConfiguration configuration, Environment environment) throws Exception {
		fileReaderService = configuration.getFileReaderFactory().build(environment);
		environment.jersey().register(new FileReaderRouter(fileReaderService));
		environment.healthChecks().register("application", new FileLineServerApplicationHealthCheck(fileReaderService));
	}
}
