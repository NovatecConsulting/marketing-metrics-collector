package info.novatec.metricscollector.github;

import java.util.Collection;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.commons.MetricCollector;
import info.novatec.metricscollector.github.collector.GithubBasicMetricCollector;
import info.novatec.metricscollector.github.collector.GithubMetricCollector;
import info.novatec.metricscollector.github.exception.UserDeniedException;


@Slf4j
@Setter
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "github")
public class Scheduler implements ApplicationContextAware {

    private final RestService restService;

    private final MetricsRepository repository;

    private final GithubProperties properties;

    private ApplicationContext applicationContext;

    @Scheduled(cron = "${github.cron}")
    void scheduleUpdateAllGithubProjectsMetrics() {
        updateAllGithubProjectsMetrics(GithubMetricCollector.class);
    }

    void updateAllGithubProjectsMetrics(Class<? extends MetricCollector> metricClass) {
        properties.getUrls().forEach(githubProjectUrl -> {
            try {
                Metrics metrics = new Metrics(githubProjectUrl);
                Collection<GithubBasicMetricCollector> metricBeans =
                    ( Collection<GithubBasicMetricCollector> ) applicationContext.getBeansOfType(metricClass).values();
                log.info("Apply {} metrics to GitHub repository '{}'", metricBeans.size(), metrics.getRepositoryName());
                for (GithubBasicMetricCollector metricBean : metricBeans) {
                    executeCollection(metricBean, metrics);
                }
                repository.saveMetrics(metrics);
            } catch (UserDeniedException e) {
                log.error("Cannot collect github metrics for '{}'.\n{}", githubProjectUrl, e.getMessage());
            }
        });
    }

    void executeCollection(GithubBasicMetricCollector metric, Metrics metrics) {
        metric.setMetrics(metrics);
        metric.setRestService(restService);
        try {
            metric.collect();
        }catch (HttpClientErrorException e) {
            throw new UserDeniedException("No authorized user logged in! Please add a valid github token!");
        }
    }
}
