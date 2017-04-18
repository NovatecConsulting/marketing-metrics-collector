package info.novatec.metricscollector.github.metrics;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


@Component
public class NumberOfForks extends GithubMetric{

    public NumberOfForks(RestService restService, GithubMetricsResult metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        if(projectRepository==null){
            projectRepository = createJsonObject(getProjectRepository(projectName).getBody());
        }
        metrics.setForks( projectRepository.getInt("forks_count"));
    }
}
