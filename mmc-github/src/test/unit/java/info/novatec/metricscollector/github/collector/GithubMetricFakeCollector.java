package info.novatec.metricscollector.github.collector;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.github.util.DataProvider;

import info.novatec.metricscollector.github.RestService;
import info.novatec.metricscollector.github.Metrics;


@SuppressWarnings("unused")
@Component
public class GithubMetricFakeCollector extends GithubBasicMetricCollector implements
    GithubMetricCollectorWithImplementations {

    public GithubMetricFakeCollector(RestService restService, Metrics metrics) {
        super(restService, metrics);
    }

    public void collect() {
        DataProvider.fillMetrics(getMetrics());
    }

}
