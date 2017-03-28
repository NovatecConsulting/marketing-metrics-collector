package info.novatec.metricscollector.github;

import java.util.ArrayList;
import java.util.List;

import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.commons.ConfigProperties;
import info.novatec.metricscollector.commons.InfluxService;


@Slf4j
@Component
public class GithubRepository {

    private InfluxService influx;

    private GithubCollector collector;

    private ConfigProperties properties;

    @Autowired
    GithubRepository(InfluxService influx, GithubCollector collector, ConfigProperties properties) {
        this.influx = influx;
        this.collector = collector;
        this.properties = properties;
    }

    void setRetention(String retention) {
        influx.setRetention(retention);
    }

    void saveMetrics(GithubMetrics metrics) {
        influx.savePoint(createPoints(metrics));
        influx.close();
        log.info("Added point  for '" + metrics.getRepositoryName() + "' to InfluxDb Measurement '"
            + metrics.getRepositoryName() + "'.");
    }

    private List<Point> createPoints(GithubMetrics metrics) {
        log.info("Adding measurement point for '" + metrics.getRepositoryName()+"'...");

        Point.Builder point = Point.measurement(metrics.getRepositoryName())
            .addField("contributors", metrics.getContributors())
            .addField("stars", metrics.getStars())
            .addField("forks", metrics.getForks())
            .addField("watchers", metrics.getWatchers())
            .addField("openIssues", metrics.getOpenIssues())
            .addField("closedIssues", metrics.getClosedIssues())
            .addField("commits", metrics.getCommits())
            .addField("YesterdaysTotalVisits", metrics.getDailyVisits().getTotalVisits())
            .addField("YesterdaysUniqueVisits", metrics.getDailyVisits().getUniqueVisits());
        metrics.getReleaseDownloads().entrySet().forEach(map -> point.addField(map.getKey(), map.getValue()));

        List<Point> points = new ArrayList<>();
        points.add(point.build());
        return points;
    }

}
