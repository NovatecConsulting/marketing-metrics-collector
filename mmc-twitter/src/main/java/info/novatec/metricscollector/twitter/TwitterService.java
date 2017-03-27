package info.novatec.metricscollector.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import twitter4j.TwitterException;

import info.novatec.metricscollector.commons.ConfigProperties;
import info.novatec.metricscollector.commons.DateTimeConverter;
import info.novatec.metricscollector.commons.InfluxService;


@Slf4j
@Component
public class TwitterService {

    private InfluxService influx;

    private TwitterCollector collector;

    private ConfigProperties properties;

    @Getter
    private DateTimeConverter dateTimeConverter;

    @Autowired
    TwitterService(InfluxService influx, TwitterCollector collector, ConfigProperties properties, DateTimeConverter dateTimeConverter) {
        this.influx = influx;
        this.collector = collector;
        this.properties = properties;
        this.dateTimeConverter = dateTimeConverter;
    }

    void setRetention(String retention) {
        influx.setRetention(retention);
    }

    void collectAndSaveMetrics(String userName, String atUserName) throws TwitterException {
        saveMetrics(collectMetrics(userName, atUserName));
    }

    TwitterMetrics collectMetrics(String userName, String atUserName) throws TwitterException{
        return collector.collect(userName, atUserName);
    }

    void saveMetrics(TwitterMetrics metrics){
        List<Point> points = createPoints(metrics);
        influx.savePoint(points);
        influx.close();
        log.info("Saved points for user '"+metrics.getUserName()+"' to InfluxDb Measurement '"+properties.getInfluxMeasurementNameTwitter()+"'.");
    }

    private List<Point> createPoints(TwitterMetrics metrics) {

        List<Point> points = new ArrayList<>();

        log.info("Adding measurement points for '" + metrics.getUserName()+"'...");

        //create point for saving general metrics
        points.add(createGeneralPoint(metrics));
        log.info("General measurement point created.");

        //create point for updating likes of mentions
        points.addAll(createLikesPoints(metrics));
        log.info("Likes Measurement point created.");
        return points;
    }

    private Point createGeneralPoint(TwitterMetrics metrics) {
        Point.Builder point = Point.measurement(properties.getInfluxMeasurementNameTwitter())
            .time(dateTimeConverter.getCurrentDateInSeconds(0), TimeUnit.SECONDS)
            .tag("atUserName", metrics.getAtUserName())
            .addField("tweets", metrics.getTweets())
            .addField("retweets", metrics.getReTweets())
            .addField("mentions", metrics.getMentions())
            .addField("likes", metrics.getLikes())
            .addField("followers", metrics.getFollowers());
        return point.build();
    }

    private List<Point> createLikesPoints(TwitterMetrics metrics){
        List<Point> points = new ArrayList<>(metrics.getLikesOfMentions().size());
        metrics.getLikesOfMentions().entrySet().forEach(likesOfMention -> {
            Point point = Point.measurement(properties.getInfluxMeasurementNameTwitter())
                .time(dateTimeConverter.getDateInSecondsMinusDays(likesOfMention.getKey(), 0), TimeUnit.SECONDS)
                .tag("atUserName", metrics.getAtUserName())
                .addField("likesOfMentions", likesOfMention.getValue())
                .build();
            points.add(point);
        });
        return points;
    }
}
