package info.novatec.metricscollector.github.collector;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.github.RestService;
import info.novatec.metricscollector.github.Metrics;


@Component
public class Stars extends GithubBasicMetricCollector implements GithubMetricCollector {

    public Stars(RestService restService, Metrics metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        metrics.setStars( getProjectRepository().getInt("stargazers_count"));
    }
}
