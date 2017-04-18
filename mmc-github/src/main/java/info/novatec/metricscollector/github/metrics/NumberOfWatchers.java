package info.novatec.metricscollector.github.metrics;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


@Component
public class NumberOfWatchers extends GithubMetric{

    public NumberOfWatchers(RestService restService, GithubMetricsResult metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        if(projectRepository==null){
            projectRepository = createJsonObject(getProjectRepository(projectName).getBody());
        }
        metrics.setWatchers( projectRepository.getInt("subscribers_count"));
    }
}
