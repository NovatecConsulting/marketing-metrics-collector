package info.novatec.metricscollector.github;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.commons.MetricCollector;
import info.novatec.metricscollector.commons.exception.UserDeniedException;
import info.novatec.metricscollector.github.collector.GithubMetricCollector;
import info.novatec.metricscollector.github.collector.GithubBasicMetricCollector;


@Slf4j
@Setter
@Component
@ConfigurationProperties(prefix = "github")
public class Scheduler implements ApplicationContextAware {

    private RestService restService;

    private MetricsRepository repository;

    @Getter
    private List<String> urls;

    private ApplicationContext applicationContext;

    @Autowired
    public Scheduler(RestService restService, MetricsRepository repository) {
        this.restService = restService;
        this.repository = repository;
    }

    @Scheduled(cron = "${github.cron}")
    void scheduleUpdateAllGithubProjectsMetrics() {
        updateAllGithubProjectsMetrics(GithubMetricCollector.class);
    }

    void updateAllGithubProjectsMetrics(Class<? extends MetricCollector> metricClass) {
        List<String> appliedUrls = new ArrayList<>();

        urls.forEach(githubProjectUrl -> {
            try {
                Metrics metrics = new Metrics(githubProjectUrl);
                Collection<GithubBasicMetricCollector> metricBeans =
                    ( Collection<GithubBasicMetricCollector> ) applicationContext.getBeansOfType(metricClass).values();
                log.info(
                    "Apply " + metricBeans.size() + " metrics to GitHub repository '" + metrics.getRepositoryName() + "'");
                for (GithubBasicMetricCollector metricBean : metricBeans) {
                    executeCollection(metricBean, metrics);
                }
                repository.saveMetrics(metrics);
                appliedUrls.add(githubProjectUrl);
            } catch (UserDeniedException e) {
                urls.removeAll(appliedUrls);
                log.warn("Cannot collect github metrics for '" + urls + "'.\n" + e.getMessage());
            }
        });

    }

    void executeCollection(GithubBasicMetricCollector metric, Metrics metrics) {
        metric.setMetrics(metrics);
        metric.setRestService(restService);
        try {
            metric.collect();
        }catch (HttpClientErrorException e) {
            throw new UserDeniedException("No authorized user logged in! Please add a valid oauth token to the properties.");
        }
    }
}
