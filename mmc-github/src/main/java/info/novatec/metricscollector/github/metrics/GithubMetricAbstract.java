package info.novatec.metricscollector.github.metrics;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

import info.novatec.metricscollector.commons.GeneralMetric;
import info.novatec.metricscollector.github.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


@Component
@Setter
public abstract class GithubMetricAbstract implements GeneralMetric{

    public static final String GITHUB_URL = "https://api.github.com/repos/";

    JsonObject projectRepository;

    @Autowired
    RestService restService;

    @Getter
    GithubMetricsResult metrics;

    public GithubMetricAbstract(RestService restService, GithubMetricsResult metrics){
        this.restService = restService;
        this.metrics = metrics;
    }

    ResponseEntity<String> getProjectRepository(String projectName) {
        return restService.sendRequest(getBaseRequestUrl());
    }

    JsonArray createJsonArray(String serializedJsonObject) {
        JsonReader jsonReader = Json.createReader(new StringReader(serializedJsonObject));
        return jsonReader.readArray();
    }

    JsonObject createJsonObject(String serializedJsonObject) {
        JsonReader jsonReader = Json.createReader(new StringReader(serializedJsonObject));
        return jsonReader.readObject();
    }

    public String getBaseRequestUrl(){
        return GITHUB_URL + metrics.getRepositoryName();
    }
}
