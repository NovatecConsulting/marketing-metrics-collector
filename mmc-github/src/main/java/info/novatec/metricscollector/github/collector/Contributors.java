package info.novatec.metricscollector.github.collector;

import javax.json.JsonArray;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.github.RestService;
import info.novatec.metricscollector.github.Metrics;


@Component
public class Contributors extends GithubBasicMetricCollector implements GithubMetricCollector {

    public Contributors(RestService restService, Metrics metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        String url = getBaseRequestUrl() + "/contributors";
        JsonArray contributors = createJsonArray(restService.sendRequest(url).getBody());
        metrics.setContributors(contributors.size());
    }

}
