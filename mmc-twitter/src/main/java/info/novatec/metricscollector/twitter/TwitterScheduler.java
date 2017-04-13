package info.novatec.metricscollector.twitter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import twitter4j.TwitterException;


@Slf4j
@Component
public class TwitterScheduler {

    private TwitterCollector collector;

    private TwitterRepository repository;

    private Map<String, String> users;

    @Autowired
    public TwitterScheduler(TwitterCollector collector, TwitterRepository repository, Map<String, String> users){
        this.collector = collector;
        this.repository = repository;
        this.users = users;
    }

    @Scheduled(cron = "${twitter.cron}")
    private void updateAllTwitteUsersrMetrics() {

            users.forEach((atUsername, username) -> {
                atUsername = atUsername.charAt(0)=='@' ? atUsername.substring(1) : atUsername;
                try {
                    TwitterMetricsResult metrics = collector.collect(atUsername, username);
                    repository.saveMetrics(metrics);
                } catch (TwitterException e) {
                    log.warn("Cannot collect twitter metrics for '"+atUsername+"'. " + e.getMessage());
                }
            });

    }
}
