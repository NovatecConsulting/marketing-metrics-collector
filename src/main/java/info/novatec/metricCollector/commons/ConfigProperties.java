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

    String gitHubMetricsDownloadsSuffix;

    String influxUrl;

    String influxDbName;

    private Properties properties;

    public ConfigProperties(String propertiesFileName) {
        try {
            properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream(propertiesFileName));
        }catch (IOException e){
            log.error("File '"+propertiesFileName+"' not found. Please provide it in resource folder.");
        }
        gitHubToken = properties.getProperty("github.token");
        gitHubMetricsDownloadsSuffix = properties.getProperty("github.metrics.downloads.suffix");
        influxUrl = properties.getProperty("influx.url");
        influxDbName = properties.getProperty("influx.db-name");
    }

    public ConfigProperties() {
        this(PROPERTIES_FILE_DEFAULT);
    }

}
