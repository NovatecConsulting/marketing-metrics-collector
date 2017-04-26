package info.novatec.metricscollector.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.commons.GeneralMetric;
import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.commons.exception.UserDeniedException;
import info.novatec.metricscollector.github.metrics.GithubMetricAbstract;


@Slf4j
@Component
@Setter
class GithubCollector {

    private RestService restService;

    private String token;

    @Autowired
    public GithubCollector(RestService restService) {
        this.restService = restService;
    }

    void collect(GithubMetricAbstract metric) {
        setHeadersForGithub();
        try {
            metric.collect();
        } catch (HttpClientErrorException e) {
            throw new UserDeniedException("No authorized user logged in! Please add a valid oauth token to the properties.");
        }
    }

    void setHeadersForGithub() {
        if (token == null || token.equals("")) {
            log.error("Token '" + token + "' is not a valid Token. Please specify in properties.");
            token = "";
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "token " + token);
        restService.setHttpHeaders(httpHeaders);
    }

}
