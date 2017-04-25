package info.novatec.metricscollector.github;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
class GithubCollector implements ApplicationContextAware{

    public static final String GITHUB_BASE_URL = "https://github.com/";

    private GithubMetricsResult metrics;

    private RestService restService;

    private String token;

    private ApplicationContext applicationContext;

    @Autowired
    public GithubCollector(GithubMetricsResult metrics, RestService restService) {
        this.metrics = metrics;
        this.restService = restService;
    }

    GithubMetricsResult collect(String githubUrl, Class<? extends GeneralMetric> metricType) {

        String projectName = extractProjectName(githubUrl);
        setHeadersForGithub();

        try {
            for (Map.Entry<String, ?> entry : getBeanNameFromApplicationContext(metricType)) {
                GithubMetricAbstract metric = ( GithubMetricAbstract )entry.getValue();
                metric.setProjectName(projectName);
                metric.setMetrics(metrics);
                metric.collect();
            }
        } catch (HttpClientErrorException e) {
            throw new UserDeniedException("No authorized user logged in! Please add a valid oauth token to the properties.");
        }

        return metrics;
    }

    String extractProjectName(String githubUrl){
        if(githubUrl==null || !githubUrl.startsWith(GITHUB_BASE_URL)){
            throw new IllegalArgumentException("URL '"+githubUrl+"' must be a valid and complete Github URL");
        }
        String projectName = githubUrl.substring(GITHUB_BASE_URL.length());
        try {
            metrics.setRepositoryName(projectName.split("/")[1]);
        }catch(ArrayIndexOutOfBoundsException e){
            throw new IllegalArgumentException("URL '"+githubUrl+"' must be a valid and complete Github URL");
        }
        return projectName;
    }

    void setHeadersForGithub() {
        if(token==null || token.equals("")){
            log.error("Token '"+token+"' is not a valid Token. Please specify in properties." );
            token = "";
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "token " + token);
        restService.setHeaders(httpHeaders);
    }

    Set<? extends Map.Entry<String, ?>> getBeanNameFromApplicationContext(Class<?> targetInterface) {
       return applicationContext.getBeansOfType(targetInterface).entrySet();
    }

}
