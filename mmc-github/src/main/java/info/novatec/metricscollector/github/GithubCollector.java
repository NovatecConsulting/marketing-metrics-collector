package info.novatec.metricscollector.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.commons.exception.UserDeniedException;
import info.novatec.metricscollector.github.metrics.GithubMetricAbstract;


@Slf4j
@Component
@Setter
class GithubCollector {

    private RestService restService;

    @Autowired
    public GithubCollector(RestService restService) {
        this.restService = restService;
    }

    void collect(GithubMetricAbstract metric) {
        try {
            metric.setRestService(restService);
            metric.collect();
        } catch (HttpClientErrorException e) {
            throw new UserDeniedException("No authorized user logged in! Please add a valid oauth token to the properties.");
        }
    }

}
