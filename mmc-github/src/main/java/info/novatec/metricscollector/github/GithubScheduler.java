package info.novatec.metricscollector.github;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.commons.GeneralMetric;
import info.novatec.metricscollector.commons.exception.UserDeniedException;
import info.novatec.metricscollector.github.metrics.GithubMetric;
import info.novatec.metricscollector.github.metrics.GithubMetricAbstract;


@Slf4j
@Component
public class GithubScheduler implements ApplicationContextAware {

    private GithubCollector collector;

    private GithubRepository repository;

    private List<String> urls;

    @Setter
    private ApplicationContext applicationContext;

    @Autowired
    public GithubScheduler(GithubCollector collector, GithubRepository repository, List<String> urls) {
        this.collector = collector;
        this.repository = repository;
        this.urls = urls;
    }

    @Scheduled(cron = "${github.cron}")
    void scheduleUpdateAllGithubProjectsMetrics(){

        updateAllGithubProjectsMetrics(GithubMetric.class);
    }

    void updateAllGithubProjectsMetrics(Class<? extends GeneralMetric> metricClass) {
        try {
            urls.forEach(githubProjectUrl -> {
                GithubMetricsResult metrics = new GithubMetricsResult(githubProjectUrl);
                for(GeneralMetric metricBean : applicationContext.getBeansOfType(metricClass).values()){
                    collector.collect((GithubMetricAbstract)metricBean);
                }
                repository.saveMetrics(metrics);

            });
        } catch (UserDeniedException e) {
            log.warn("Cannot collect github metrics for '" + urls + "'. " + e.getMessage());
        }

    }
}
