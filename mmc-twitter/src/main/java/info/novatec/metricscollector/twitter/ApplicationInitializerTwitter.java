package info.novatec.metricscollector.twitter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.Getter;
import lombok.Setter;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import info.novatec.metricscollector.commons.ApplicationInitializerCommons;


@SpringBootApplication
@EnableScheduling
@Import(ApplicationInitializerCommons.class)
@ConfigurationProperties(prefix="twitter")
public class ApplicationInitializerTwitter {

    @Setter
    private String consumerKey;

    @Setter
    private String consumerSecret;

    @Setter
    private String accessToken;

    @Setter
    private String accessSecret;

    @Getter
    private Map<String, String> users = new HashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(ApplicationInitializerTwitter.class, args);
    }

    @Bean
    public TwitterCollector twitterCollector(TwitterMetricsResult metrics, Twitter twitter){
        return new TwitterCollector(metrics, twitter);
    }

    @Bean
    public Twitter twitter(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(consumerKey)
            .setOAuthConsumerSecret(consumerSecret)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessSecret);
        return new TwitterFactory(cb.build()).getInstance();
    }

    @Bean
    public Map<String, String> users(){
        return users;
    }
}
