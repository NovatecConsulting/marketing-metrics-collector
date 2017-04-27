package info.novatec.metricscollector.twitter;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import twitter4j.TwitterException;

import info.novatec.metricscollector.commons.GeneralMetric;
import info.novatec.metricscollector.twitter.metrics.TwitterMetric;
import info.novatec.metricscollector.twitter.metrics.TwitterMetricAbstract;


@Slf4j
@Component
public class TwitterScheduler implements ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

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
    void scheduleUpdateAllTwitterUsersMetrics(){
        updateAllTwitterUsersMetrics(TwitterMetric.class);
    }


    void updateAllTwitterUsersMetrics(Class<? extends GeneralMetric> metricClass) {

            users.forEach((atUsername, username) -> {
                atUsername = atUsername.charAt(0)=='@' ? atUsername.substring(1) : atUsername;
                try {
                    TwitterMetricsResult metrics = new TwitterMetricsResult(atUsername, username);
                    Collection<? extends GeneralMetric> metricBeans = applicationContext.getBeansOfType(metricClass).values();
                    log.info("Apply " + metricBeans.size() + " metrics to @username '"+metrics.getAtUserName()+"'");
                    for(GeneralMetric metricBean : metricBeans){
                        ((TwitterMetricAbstract )metricBean).setMetrics(metrics);
                        collector.collect((TwitterMetricAbstract )metricBean);
                    }
                    repository.saveMetrics(metrics);
                } catch (TwitterException e) {
                    log.warn("Cannot collect twitter metrics for '"+atUsername+"'.\n" + e.getMessage());
                }
            });

    }
}
