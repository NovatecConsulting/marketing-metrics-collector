package info.novatec.metricscollector.github;

import info.novatec.metricscollector.commons.exception.UserDeniedException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@ConfigurationProperties(prefix = "github.project")
public class GithubScheduler {

    @Autowired
    private GithubCollector githubCollector;

    @Autowired
    private GithubRepository githubRepository;

    @Getter
    private List<String> urls = new ArrayList<>();

    private static final String CRON_EXPRESSION = "0 0 0 * * *";

    @Scheduled(cron = CRON_EXPRESSION)
    private void updateAllGithubProjectsMetrics() {
        try {
            getUrls().forEach(githubProjectUrl -> {
                githubRepository.saveMetrics(collectMetrics(githubProjectUrl));
                log.info("SCHEDULER " + githubProjectUrl + " completed.\n");
            });
        } catch (UserDeniedException e) {
            log.warn(e.getMessage());
        }
    }

    private GithubMetrics collectMetrics(String githubProjectUrl) {
        return githubCollector.collect(githubProjectUrl);
    }
}
