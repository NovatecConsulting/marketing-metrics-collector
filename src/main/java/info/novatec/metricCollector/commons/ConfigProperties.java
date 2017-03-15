package info.novatec.metricCollector.commons;

import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
@Component
public class ConfigProperties {

    private static final String PROPERTIES_FILE_DEFAULT = "config.properties";

    String gitHubToken;

    private Properties properties;

    public ConfigProperties(String propertiesFileName) {
        try {
            properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream(propertiesFileName));
        }catch (IOException e){
            log.error("File '"+propertiesFileName+"' not found. Please provide it in resource folder.");
        }
        gitHubToken = properties.getProperty("github.token");
    }

    public ConfigProperties() {
        this(PROPERTIES_FILE_DEFAULT);
    }

}
