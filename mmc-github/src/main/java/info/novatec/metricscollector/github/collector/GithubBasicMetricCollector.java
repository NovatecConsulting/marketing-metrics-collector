package info.novatec.metricscollector.github.collector;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.commons.MetricCollector;
import info.novatec.metricscollector.commons.rest.RestService;
import info.novatec.metricscollector.github.Metrics;

@Slf4j
@Setter
@Component
public abstract class GithubBasicMetricCollector implements MetricCollector {

    public static final String GITHUB_API_URL = "https://api.github.com/repos/";

    private JsonObject projectRepository;

    RestService restService;

    Metrics metrics;

    @Autowired
    public GithubBasicMetricCollector(RestService restService, Metrics metrics) {
        this.restService = restService;
        this.metrics = metrics;
    }

    JsonObject getProjectRepository() {
        if (!projectRepositoryAlreadyRequested()) {
            projectRepository = createJsonObject(restService.sendRequest(getBaseRequestUrl()).getBody());
        }
        return projectRepository;
    }

    boolean projectRepositoryAlreadyRequested() {
        return projectRepository != null;
    }

    JsonArray createJsonArray(String serializedJsonObject) {
        JsonReader jsonReader = Json.createReader(new StringReader(serializedJsonObject));
        return jsonReader.readArray();
    }

    JsonObject createJsonObject(String serializedJsonObject) {
        JsonReader jsonReader = Json.createReader(new StringReader(serializedJsonObject));
        return jsonReader.readObject();
    }

    String getBaseRequestUrl() {
        return GITHUB_API_URL + metrics.getRepositoryName();
    }
}
