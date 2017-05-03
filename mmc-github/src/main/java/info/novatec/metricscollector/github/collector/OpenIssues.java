package info.novatec.metricscollector.github.collector;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.github.RestService;
import info.novatec.metricscollector.github.Metrics;


@Component
public class OpenIssues extends GithubBasicMetricCollector implements GithubMetricCollector {

    public OpenIssues(RestService restService, Metrics metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        metrics.setOpenIssues( getProjectRepository().getInt("open_issues_count"));
    }
}
