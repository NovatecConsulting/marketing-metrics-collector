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
public class GithubService {

    private InfluxService influx;

    private GithubCollector collector;

    private ConfigProperties properties;

    @Autowired
    GithubService(InfluxService influx, GithubCollector collector, ConfigProperties properties) {
        this.influx = influx;
        this.collector = collector;
        this.properties = properties;
    }

    void setRetention(String retention) {
        influx.setRetention(retention);
    }

    void collectAndSaveMetrics(String githubProjectURL) {
        saveMetrics(collectMetrics(githubProjectURL));
    }

    GithubMetrics collectMetrics(String githubProjectURL) {
        String projectname = githubProjectURL.substring("https://github.com/".length());
        return collector.collect(projectname);
    }

    void saveMetrics(GithubMetrics metrics) {
        influx.savePoint(createPoints(metrics));
        influx.close();
        log.info("Added point  for '" + metrics.getRepositoryName() + "' to InfluxDb Measurement '"
            + properties.getInfluxMeasurementNameGithub() + "'.");
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
