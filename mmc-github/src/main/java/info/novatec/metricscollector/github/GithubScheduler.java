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

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.commons.GeneralMetric;
import info.novatec.metricscollector.commons.exception.UserDeniedException;
import info.novatec.metricscollector.github.metrics.GithubMetric;
import info.novatec.metricscollector.github.metrics.GithubMetricAbstract;


@Slf4j
@Setter
@Component
@ConfigurationProperties(prefix="github")
public class GithubScheduler implements ApplicationContextAware {

    private GithubCollector collector;

    private GithubRepository repository;

    @Getter
    private List<String> urls;

    private ApplicationContext applicationContext;

    @Autowired
    public GithubScheduler(GithubCollector collector, GithubRepository repository) {
        this.collector = collector;
        this.repository = repository;
    }

    @Scheduled(cron = "${github.cron}")
    void scheduleUpdateAllGithubProjectsMetrics(){
        updateAllGithubProjectsMetrics(GithubMetric.class);
    }

    void updateAllGithubProjectsMetrics(Class<? extends GeneralMetric> metricClass) {
        List<String> appliedUrls = new ArrayList<>();
        try {
            urls.forEach(githubProjectUrl -> {
                GithubMetricsResult metrics = new GithubMetricsResult(githubProjectUrl);
                Collection<? extends GeneralMetric> metricBeans = applicationContext.getBeansOfType(metricClass).values();
                log.info("Apply " + metricBeans.size() + " metrics to GitHub repository '"+metrics.getRepositoryName()+"'");
                for(GeneralMetric metricBean : metricBeans){
                    ((GithubMetricAbstract)metricBean).setMetrics(metrics);
                    collector.collect((GithubMetricAbstract)metricBean);
                }
                repository.saveMetrics(metrics);
                appliedUrls.add(githubProjectUrl);
            });
        } catch (UserDeniedException e) {
            urls.removeAll(appliedUrls);
            log.warn("Cannot collect github metrics for '" + urls + "'.\n" + e.getMessage());
        }

    }
}
