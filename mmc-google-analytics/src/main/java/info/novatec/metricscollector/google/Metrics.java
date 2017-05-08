package info.novatec.metricscollector.google;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;


@Data
public class Metrics {
    private String pagePath;
    private Map<String, String> dimensions; // <google analytics key of dimension, responded value>
    private Map<String, Double> metrics; // <google analytics key of metric, responded value>

    public Metrics() {
        dimensions = new HashMap<>();
        metrics = new HashMap<>();
    }

    void addDimension(String name, String value){
        dimensions.put(name, value);
    }

    void addMetric(String name, Double value){
        metrics.put(name, value);
    }

}
