package info.novatec.metricscollector.twitter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.commons.InfluxService;


@Slf4j
@Component
public class TwitterRepository {

    private InfluxService influx;

    @Autowired
    TwitterRepository(InfluxService influx) {
        this.influx = influx;
    }

    void setRetention(String retention) {
        influx.setRetention(retention);
    }

    void saveMetrics(TwitterMetricsResult metrics) {
        List<Point> points = createPoints(metrics);
        influx.savePoint(points);
        influx.close();
        log.info("Saved points for user '" + metrics.getUserName() + "' to InfluxDb Measurement '"
            + metrics.getAtUserName() + "' and '"+metrics.getAtUserName()+"_Likes'.");
    }

    private List<Point> createPoints(TwitterMetricsResult metrics) {
        List<Point> points = new ArrayList<>();

        log.info("Adding measurement point for '" + metrics.getUserName() + " (@" + metrics.getAtUserName() + ")'...");
        Point.Builder pointGeneral = Point.measurement(metrics.atUserName)
            .addField("tweets", metrics.getTweets())
            .addField("retweets", metrics.getReTweets())
            .addField("mentions", metrics.getMentions())
            .addField("followers", metrics.getFollowers());
        points.add(pointGeneral.build());


        metrics.getLikesOfMentions().entrySet().forEach( likes -> {
                Point.Builder pointLikes = Point.measurement(metrics.atUserName+"_Likes")
                    .time(convertDateTime(likes.getKey()), TimeUnit.SECONDS)
                    .addField("totalLikes", metrics.getLikes())
                    .addField("likesOfMentions", likes.getValue());
                points.add(pointLikes.build());
            });
        return points;
    }

    /**
     * Example input: Mon Mar 20 21:44:45 CET 2017
     * output: 2017-03-20T21:44:45
     */
    public long convertDateTime(String dateTime){
        Locale dateLocale = Locale.UK;
        DateTimeFormatter inFormatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z yyyy", dateLocale);
        TemporalAccessor date = inFormatter.parse(dateTime);
        DateTimeFormatter outFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String out = outFormatter.format(date);
        return LocalDateTime.parse(out).atZone(ZoneId.of("Z")).toEpochSecond();
    }

}
