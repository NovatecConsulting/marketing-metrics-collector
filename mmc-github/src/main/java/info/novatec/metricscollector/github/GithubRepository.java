package info.novatec.metricscollector.github;

import java.util.ArrayList;
import java.util.List;

import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.commons.InfluxService;
import info.novatec.metricscollector.commons.MetricsResultCheck;


@Slf4j
@Component
public class GithubRepository {

    private InfluxService influx;

    private MetricsResultCheck metricsResultCheck;

    @Autowired
    GithubRepository(InfluxService influx, MetricsResultCheck metricsResultCheck) {

        this.influx = influx;
        this.metricsResultCheck = metricsResultCheck;
    }

    void saveMetrics(GithubMetricsResult metrics) {
        influx.savePoint(createPoints(metrics));
        influx.close();
    }

    List<Point> createPoints(GithubMetricsResult metrics) {
        log.info("Start creating points for '" + metrics.getRepositoryName() + "'.");
        List<Point> points = new ArrayList<>();

        if(metricsResultCheck.hasNullValues(metrics)){
            log.error("Since there are null values in metrics result creating points for '" + metrics.getRepositoryName() + "' isn't possible!"
                + metrics.toString());
            return points;
        }

        Point.Builder point = Point.measurement(metrics.getRepositoryName())
                .addField("contributors", metrics.getContributors())
                .addField("stars", metrics.getStars())
                .addField("forks", metrics.getForks())
                .addField("watchers", metrics.getWatchers())
                .addField("openIssues", metrics.getOpenIssues())
                .addField("closedIssues", metrics.getClosedIssues())
                .addField("commits", metrics.getCommits())
                .addField("yesterdaysTotalVisits", metrics.getDailyVisits().getTotalVisits())
                .addField("yesterdaysUniqueVisits", metrics.getDailyVisits().getUniqueVisits());
        metrics.getReleaseDownloads().entrySet().forEach(
            download -> point.addField(download.getKey(), download.getValue())
        );


        metrics.getReferringSitesLast14Days().entrySet().forEach( referrer -> {
            Point pointReferrer = Point
                .measurement(metrics.getRepositoryName()+"_Referrer")
                .tag("referringSite", referrer.getKey())
                .addField("clicksLast14Days", referrer.getValue().getTotalVisits())
                .addField("uniqueClicksLast14Days", referrer.getValue().getUniqueVisits())
                .build();
            points.add(pointReferrer);
        });

        points.add(point.build());

        log.info("Created "+points.size()+" points for '" + metrics.getRepositoryName() + "'.");

        return points;
    }

}
