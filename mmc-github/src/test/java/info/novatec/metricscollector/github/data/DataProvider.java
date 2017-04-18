package info.novatec.metricscollector.github.data;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.github.metrics.GithubMetric;


@Component
public class DataProvider {

    public static final String NON_EXISTING_PROJECT = "nonExistingProject";

    public String getRestURL(){
        return GithubMetric.BASE_URL + NON_EXISTING_PROJECT;
    }
}
