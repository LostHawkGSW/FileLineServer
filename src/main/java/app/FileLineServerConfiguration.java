package app;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import clients.FileReaderFactory;
import io.dropwizard.Configuration;

public class FileLineServerConfiguration extends Configuration {
	
	@Valid
    @NotNull
    @JsonProperty("filereader")
    private FileReaderFactory fileReaderFactory = new FileReaderFactory();

	public FileReaderFactory getFileReaderFactory() {
		return fileReaderFactory;
	}
//	@Valid
//    @NotNull
//    @JsonProperty
//    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();
//	
//    public JerseyClientConfiguration getJerseyClientConfiguration() {
//        return httpClient;
//    }
}
