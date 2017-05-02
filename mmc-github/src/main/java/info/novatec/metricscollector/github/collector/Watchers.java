package info.novatec.metricscollector.github.collector;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.github.RestService;
import info.novatec.metricscollector.github.Metrics;


@Component
public class Watchers extends GithubBasicMetricCollector implements GithubMetricCollector {

    public Watchers(RestService restService, Metrics metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        if(projectRepository==null){
            projectRepository = createJsonObject(getProjectRepository(metrics.getProjectName()).getBody());
        }
        metrics.setWatchers( projectRepository.getInt("subscribers_count"));
    }
}
