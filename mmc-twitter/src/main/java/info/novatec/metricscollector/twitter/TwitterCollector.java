package info.novatec.metricscollector.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Setter;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.metrics.TwitterMetricAbstract;


@Component
@Setter
public class TwitterCollector{

    private Twitter twitter;

    @Autowired
    public TwitterCollector(Twitter twitter) {
        this.twitter = twitter;
    }

    void collect(TwitterMetricAbstract metric) throws TwitterException {
        metric.setTwitter(twitter);
        metric.collect();
    }

}
