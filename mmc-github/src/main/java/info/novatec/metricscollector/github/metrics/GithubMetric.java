package info.novatec.metricscollector.github.metrics;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.Setter;

import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


@Component
public abstract class GithubMetric {

    public static final String BASE_URL = "https://api.github.com/repos/";

    @Setter
    String projectName;

    @Setter
    JsonObject projectRepository;

    @Autowired
    @Setter
    RestService restService;

    @Setter
    GithubMetricsResult metrics;

    public GithubMetric(RestService restService, GithubMetricsResult metrics){
        this.restService = restService;
        this.metrics = metrics;
    }

    public abstract void collect();

    ResponseEntity<String> getProjectRepository(String projectName) {
        return restService.sendRequest(BASE_URL + projectName);
    }

    JsonArray createJsonArray(String serializedJsonObject) {
        JsonReader jsonReader = Json.createReader(new StringReader(serializedJsonObject));
        return jsonReader.readArray();
    }

    JsonObject createJsonObject(String serializedJsonObject) {
        JsonReader jsonReader = Json.createReader(new StringReader(serializedJsonObject));
        return jsonReader.readObject();
    }
}
