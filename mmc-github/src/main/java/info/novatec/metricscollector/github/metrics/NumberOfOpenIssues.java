package info.novatec.metricscollector.github.metrics;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


@Component
public class NumberOfOpenIssues extends GithubMetricAbstract implements GithubMetric {

    public NumberOfOpenIssues(RestService restService, GithubMetricsResult metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        if(projectRepository==null){
            projectRepository = createJsonObject(getProjectRepository(projectName).getBody());
        }
        metrics.setOpenIssues( projectRepository.getInt("open_issues_count"));
    }
}
