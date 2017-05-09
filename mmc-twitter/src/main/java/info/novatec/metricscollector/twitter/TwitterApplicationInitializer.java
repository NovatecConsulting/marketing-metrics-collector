package info.novatec.metricscollector.twitter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.Setter;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import info.novatec.metricscollector.commons.CommonsApplicationInitializer;


@Setter
@SpringBootApplication
@EnableScheduling
@Import(CommonsApplicationInitializer.class)
public class TwitterApplicationInitializer {

    @Setter
    @Autowired
    TwitterProperties properties;

    public static void main(String[] args) {
        SpringApplication.run(TwitterApplicationInitializer.class, args);
    }

    @Bean
    public Twitter twitter(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(properties.getConsumerKey())
            .setOAuthConsumerSecret(properties.getConsumerSecret())
            .setOAuthAccessToken(properties.getAccessToken())
            .setOAuthAccessTokenSecret(properties.getAccessSecret());
        return new TwitterFactory(cb.build()).getInstance();
    }

    @Bean
    public Map<String, String> users(){
        return properties.getUsers();
    }
}
