package info.novatec.metricscollector.github.metrics;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.github.util.DataProvider;

import info.novatec.metricscollector.github.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


@SuppressWarnings("unused")
@Component
public class GithubMetricImpl extends GithubMetricAbstract implements GithubMetricDummy {

    public GithubMetricImpl(RestService restService, GithubMetricsResult metrics) {
        super(restService, metrics);
    }

    public void collect() {
        DataProvider.fillMetrics(getMetrics());
    }

}
