package info.novatec.metricscollector.github;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.commons.exception.UserDeniedException;

@Slf4j
@Component
public class GithubScheduler {

    private GithubCollector collector;

    private GithubRepository repository;

    private List<String> urls;

    @Autowired
    public GithubScheduler(GithubCollector collector, GithubRepository repository, List<String> urls){
        this.collector = collector;
        this.repository = repository;
        this.urls = urls;
    }

    @Scheduled(cron = "${github.cron}")
    private void updateAllGithubProjectsMetrics() {

            urls.forEach(githubProjectUrl -> {
                try {
                    GithubMetricsResult metrics = collector.collect(githubProjectUrl);
                    repository.saveMetrics(metrics);
                } catch (UserDeniedException e) {
                    log.warn("Cannot collect github metrics for '"+githubProjectUrl+"'. " + e.getMessage());
                }
            });

    }
}
