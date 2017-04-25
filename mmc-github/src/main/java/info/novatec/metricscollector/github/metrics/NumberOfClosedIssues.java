package info.novatec.metricscollector.github.metrics;

import javax.json.JsonArray;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


@Component
public class NumberOfClosedIssues extends GithubMetricAbstract implements GithubMetric {

    public NumberOfClosedIssues(RestService restService, GithubMetricsResult metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        String url = BASE_URL + projectName + "/issues/events";
        JsonArray closedIssues = createJsonArray(restService.sendRequest(url).getBody());
        metrics.setClosedIssues(closedIssues.size());
    }
}
