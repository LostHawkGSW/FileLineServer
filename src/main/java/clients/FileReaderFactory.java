package clients;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.setup.Environment;
import services.FileReaderService;

public class FileReaderFactory {
	@NotEmpty
	public String fileUrl;
	
	@NotEmpty
	public String cacheStrategy;
	
	// If we are using a remote cache
	public String host;
	public int port;
	
	@JsonProperty
	public String getFileUrl() {
		return fileUrl;
	}
	
	@JsonProperty
	public String getCacheStrategy() {
		return cacheStrategy;
	}

	public FileReaderService build(Environment environment) {
		return new FileReaderService(fileUrl, cacheStrategy, host, port);
	}
}
