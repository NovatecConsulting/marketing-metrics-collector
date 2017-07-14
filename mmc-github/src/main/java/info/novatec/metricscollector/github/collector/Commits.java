package info.novatec.metricscollector.github.collector;

import javax.json.JsonArray;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.commons.rest.RestService;
import info.novatec.metricscollector.github.Metrics;


@Component
public class Commits extends GithubBasicMetricCollector implements GithubMetricCollector {

    public Commits(RestService restService, Metrics metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        String url = getBaseRequestUrl() + "/commits";
        JsonArray commits = createJsonArray(restService.sendRequest(url).getBody());
        metrics.addMetric("commits", commits.size());
    }
}
