package info.novatec.metricscollector.github.collector;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.commons.rest.RestService;
import info.novatec.metricscollector.github.Metrics;


@Component
public class Forks extends GithubBasicMetricCollector implements GithubMetricCollector {

    public Forks(RestService restService, Metrics metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        metrics.addMetric("forks", getProjectRepository().getInt("forks_count"));
    }
}
