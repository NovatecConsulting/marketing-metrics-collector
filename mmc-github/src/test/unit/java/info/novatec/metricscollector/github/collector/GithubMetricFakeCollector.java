package info.novatec.metricscollector.github.collector;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.commons.rest.RestService;
import info.novatec.metricscollector.github.Metrics;
import info.novatec.metricscollector.github.util.DataProvider;


@SuppressWarnings("unused")
@Component
public class GithubMetricFakeCollector extends GithubBasicMetricCollector
    implements GithubMetricCollectorWithImplementations {

    public GithubMetricFakeCollector(RestService restService, Metrics metrics) {
        super(restService, metrics);
    }

    public void collect() {
        DataProvider.fillMetrics(metrics);
    }

}
