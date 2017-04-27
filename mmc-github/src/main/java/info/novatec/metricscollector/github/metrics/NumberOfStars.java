package info.novatec.metricscollector.github.metrics;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.github.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


@Component
public class NumberOfStars extends GithubMetricAbstract implements GithubMetric {

    public NumberOfStars(RestService restService, GithubMetricsResult metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        if(projectRepository==null){
            projectRepository = createJsonObject(getProjectRepository(metrics.getProjectName()).getBody());
        }
        metrics.setStars( projectRepository.getInt("stargazers_count"));
    }
}
