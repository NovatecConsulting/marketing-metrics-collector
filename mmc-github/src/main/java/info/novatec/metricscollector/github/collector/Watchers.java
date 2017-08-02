package info.novatec.metricscollector.github.collector;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.commons.rest.RestService;
import info.novatec.metricscollector.github.Metrics;


@Component
public class Watchers extends GithubBasicMetricCollector implements GithubMetricCollector {

    public Watchers(RestService restService, Metrics metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        metrics.addMetric("watchers", getProjectRepository().getInt("subscribers_count"));
    }
}
