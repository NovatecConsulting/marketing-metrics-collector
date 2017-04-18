package info.novatec.metricscollector.twitter;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import info.novatec.metricscollector.twitter.data.DataProvider;


@TestConfiguration
public class TestConfig {

    @Bean
    @Scope("prototype")
    public TwitterMetricsResult twitterMetricsResult() {
        return new TwitterMetricsResult(DataProvider.AT_USERNAME);
    }
}
