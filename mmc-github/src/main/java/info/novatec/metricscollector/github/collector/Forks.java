package info.novatec.metricscollector.github.collector;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.github.RestService;
import info.novatec.metricscollector.github.Metrics;


@Component
public class Forks extends GithubBasicMetricCollector implements GithubMetricCollector {

    public Forks(RestService restService, Metrics metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        metrics.setForks( getProjectRepository().getInt("forks_count"));
    }
}
