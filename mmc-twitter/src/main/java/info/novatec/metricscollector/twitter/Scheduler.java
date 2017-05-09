package info.novatec.metricscollector.twitter;

import java.util.Collection;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import twitter4j.Twitter;

import info.novatec.metricscollector.commons.MetricCollector;
import info.novatec.metricscollector.twitter.collector.TwitterBasicMetricCollector;
import info.novatec.metricscollector.twitter.collector.TwitterMetricCollector;
import info.novatec.metricscollector.twitter.exception.TwitterRuntimeException;


@Slf4j
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "twitter")
public class Scheduler implements ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    private final TwitterProperties properties;

    private final Twitter twitter;

    private final MetricsRepository repository;

    @Scheduled(cron = "${twitter.cron}")
    void scheduleUpdateAllTwitterUsersMetrics() {
        updateAllTwitterUsersMetrics(TwitterMetricCollector.class);
    }

    void updateAllTwitterUsersMetrics(Class<? extends MetricCollector> metricClass) {
        properties.getUsers().forEach((atUsername, username) -> {
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
            } catch (TwitterRuntimeException e) {
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
