package info.novatec.metricCollector.github;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricCollector.commons.ConfigProperties;
import info.novatec.metricCollector.commons.DateTimeConverter;
import info.novatec.metricCollector.commons.InfluxService;


@Slf4j
@Component
public class GithubService {

    private InfluxService influx;

    private GithubCollector collector;

    private ConfigProperties properties;

    @Getter
    private DateTimeConverter dateTimeConverter;

    @Autowired
    GithubService(InfluxService influx, GithubCollector collector, ConfigProperties properties, DateTimeConverter dateTimeConverter) {
        this.influx = influx;
        this.collector = collector;
        this.properties = properties;
        this.dateTimeConverter = dateTimeConverter;
    }

    void setRetention(String retention) {
        influx.setRetention(retention);
    }

    void collectAndSaveMetrics(String githubProjectURL) {
        saveMetrics(collectMetrics(githubProjectURL));
    }

    GithubMetrics collectMetrics(String githubProjectURL){
        String projectname = githubProjectURL.substring("https://github.com/".length());
        return collector.collect(projectname);
    }

    void saveMetrics(GithubMetrics metrics){
        influx.savePoint(createPoints(metrics));
        influx.close();
        log.info("Added points  for '"+metrics.getRepositoryName()+"' to InfluxDb Measurement '"+properties.getInfluxMeasurementNameGithub()+"'.");
    }

    private List<Point> createPoints(GithubMetrics metrics) {

        List<Point> points = new ArrayList<>();

        log.info("Add measurement points for " + metrics.getRepositoryName());

        //create point for saving todays metrics
        points.add(createTodaysPoint(metrics));
        log.info("Measurement point for today's metrics created.");

        //create point for updating yesterdays visits

        points.add(createYesterdaysPoint(metrics));
        log.info("Measurement point for yesterdays download metrics created.");
        return points;
    }

    private Point createTodaysPoint(GithubMetrics metrics) {
        Point.Builder point = Point.measurement(properties.getInfluxMeasurementNameGithub())
            .time(dateTimeConverter.getCurrentDateInSeconds(0), TimeUnit.SECONDS)
            .tag("projectName", metrics.getRepositoryName())
            .addField("contributors", metrics.getContributors())
            .addField("stars", metrics.getStars())
            .addField("forks", metrics.getForks())
            .addField("watchers", metrics.getWatchers())
            .addField("openIssues", metrics.getOpenIssues())
            .addField("closedIssues", metrics.getClosedIssues())
            .addField("commits", metrics.getCommits());
        metrics.getReleaseDownloads().entrySet().forEach(map -> point.addField(map.getKey(), map.getValue()));
        return point.build();
    }

    private Point createYesterdaysPoint(GithubMetrics metrics){
        return Point.measurement(properties.getInfluxMeasurementNameGithub())
            .time(dateTimeConverter.getCurrentDateInSeconds(1), TimeUnit.SECONDS)
            .tag("projectName", metrics.getRepositoryName())
            .addField("totalVisits", metrics.getDailyVisits().getTotalVisits())
            .addField("uniqueVisits", metrics.getDailyVisits().getUniqueVisits())
            .build();
    }

}
