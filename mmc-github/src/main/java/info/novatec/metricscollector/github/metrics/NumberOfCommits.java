package info.novatec.metricscollector.github.metrics;

import javax.json.JsonArray;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.github.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


@Component
public class NumberOfCommits extends GithubMetricAbstract implements GithubMetric {

    public NumberOfCommits(RestService restService, GithubMetricsResult metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        String url = getBaseRequestUrl() + "/commits";
        JsonArray commits = createJsonArray(restService.sendRequest(url).getBody());
        metrics.setCommits(commits.size());
    }
}
