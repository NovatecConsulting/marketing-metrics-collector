package info.novatec.metricscollector.twitter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import twitter4j.Twitter;

import info.novatec.metricscollector.commons.MetricCollector;
import info.novatec.metricscollector.twitter.collector.TwitterBasicMetricCollector;
import info.novatec.metricscollector.twitter.collector.TwitterMetricCollector;
import info.novatec.metricscollector.twitter.exception.TwitterRuntimeException;


@Slf4j
@Setter
@Component
@ConfigurationProperties(prefix = "twitter")
public class Scheduler implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Twitter twitter;

    private MetricsRepository repository;

    @Getter
    private Map<String, String> users;

    @Autowired
    public Scheduler(Twitter twitter, MetricsRepository repository) {
        this.twitter = twitter;
        this.repository = repository;
    }

    @Scheduled(cron = "${twitter.cron}")
    void scheduleUpdateAllTwitterUsersMetrics() {
        updateAllTwitterUsersMetrics(TwitterMetricCollector.class);
    }

    void updateAllTwitterUsersMetrics(Class<? extends MetricCollector> metricClass) {
        Map<String, String> appliedUsers = new HashMap<>();

        users.forEach((atUsername, username) -> {
            try {
                atUsername = atUsername.charAt(0) == '@' ? atUsername.substring(1) : atUsername;
                Metrics metrics = new Metrics(atUsername, username);

                Collection<? extends TwitterBasicMetricCollector> metricBeans =
                    ( Collection<TwitterBasicMetricCollector> ) applicationContext.getBeansOfType(metricClass).values();
                log.info("Apply " + metricBeans.size() + " metrics to @username '" + metrics.getAtUserName() + "'");
                for (TwitterBasicMetricCollector metricBean : metricBeans) {
                    executeCollection(metricBean, metrics);
                }
                repository.saveMetrics(metrics);
                appliedUsers.put(atUsername, username);
            } catch (TwitterRuntimeException e) {
                appliedUsers.forEach(users::remove);
                log.warn("Cannot collect twitter metrics for '" + atUsername + "'.\n" + e.getMessage());
            }
        });

    }

    void executeCollection(TwitterBasicMetricCollector metric, Metrics metrics) {
        metric.setMetrics(metrics);
        metric.setTwitter(twitter);
        metric.collect();
    }
}
