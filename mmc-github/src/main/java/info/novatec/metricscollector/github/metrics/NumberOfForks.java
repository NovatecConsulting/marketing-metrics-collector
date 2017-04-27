package info.novatec.metricscollector.github.metrics;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.github.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


@Component
public class NumberOfForks extends GithubMetricAbstract implements GithubMetric {

    public NumberOfForks(RestService restService, GithubMetricsResult metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        if(projectRepository==null){
            projectRepository = createJsonObject(getProjectRepository(metrics.getProjectName()).getBody());
        }
        metrics.setForks( projectRepository.getInt("forks_count"));
    }
}
