package info.novatec.metricscollector.github.metrics;

import javax.json.JsonArray;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


@Component
public class NumberOfContributors extends GithubMetricAbstract implements GithubMetric {

    public NumberOfContributors(RestService restService, GithubMetricsResult metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        String url = BASE_URL + projectName + "/contributors";
        JsonArray contributors = createJsonArray(restService.sendRequest(url).getBody());
        metrics.setContributors(contributors.size());
    }

}
