package info.novatec.metricscollector.commons;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;


@Getter
@Configuration
@PropertySource(value={"classpath:config.properties"})
public class ConfigProperties {

    private static final String PROPERTIES_FILE_DEFAULT = "config.properties";

    @Value("${github.token}")
    String gitHubToken;

    @Value("${influx.url}")
    String influxUrl;

    @Value("${influx.db-name}")
    String influxDbName;

    @Value("${influx.measurement.name.github}")
    String influxMeasurementNameGithub;

    @Value("${influx.measurement.name.twitter}")
    String influxMeasurementNameTwitter;

    @Value("${twitter.consumer.key}")
    String twitterConsumerKey;

    @Value("${twitter.consumer.secret}")
    String twitterConsumerSecret;

    @Value("${twitter.access.token}")
    String twitterOAuthToken;

    @Value("${twitter.access.token.secret}")
    String twitterOauthTokenSecret;

    @Value("${twitter.username}")
    String twitterUserName;

    @Value("${twitter.password}")
    String twitterPassword;

}
