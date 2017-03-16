package info.novatec.metricCollector.github;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import info.novatec.metricCollector.commons.InfluxService;


@Slf4j
@Component
public class GithubService {

    private static final String MEASUREMENT = "github";

    private InfluxService influx;

    private GithubCollector collector;

    @Autowired
    GithubService(InfluxService influx, GithubCollector collector) {
        this.influx = influx;
        influx.setRetention("daily");
        this.collector = collector;
    }

    void setRetention(String retention) {
        influx.setRetention(retention);
    }

    void collectAndSaveGithubMetrics(String githubProjectURL) {
        String projectname = githubProjectURL.substring("https://github.com/".length());
        GithubMetrics metrics = collector.collect(projectname);
        influx.savePoint(createPoints(metrics));
        influx.close();
    }

    private GithubMetrics retrieveGithubMetricsFromInflux(String timestamp) {
        GithubMetrics metrics = new GithubMetrics();
        Map<String, Object> metricsMap = influx.getEntry(timestamp, MEASUREMENT);
        metricsMap.remove("time");
        metricsMap.remove("totalVisits");
        metricsMap.remove("uniqueVisits");

        metricsMap.forEach((key, value) -> {
            try {
                String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
                if (value instanceof Double) {
                    metrics.getClass().getMethod(methodName, Integer.class).invoke(metrics, (( Double ) value).intValue());
                } else {
                    metrics.getClass().getMethod(methodName, String.class).invoke(metrics, ( String ) value);
                }

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        });

        return metrics;
    }

    private List<Point> createPoints(GithubMetrics metrics) {

        List<Point> points = new ArrayList<>(2);

        log.info("Add measurement point for " + metrics.getProjectName());

        //create point for saving todays metrics
        Point pointForTodaysMetrics = createPoint(metrics, getCurrentDateAsSeconds()).build();
        points.add(pointForTodaysMetrics);

        //create point for updating yesterdays metrics
        String yesterdaysDate = metrics.getDailyVisits().getTimestamp();
        GithubMetrics yesterdaysMetrics = retrieveGithubMetricsFromInflux(yesterdaysDate);
        Point pointForYesterdaysMetrics = createPoint(yesterdaysMetrics, getDateAsSeconds(yesterdaysDate))
            .addField("totalVisits", metrics.getDailyVisits().getTotalVisits())
            .addField("uniqueVisits", metrics.getDailyVisits().getUniqueVisits())
            .build();

        points.add(pointForYesterdaysMetrics);

        return points;
    }

    private Point.Builder createPoint(GithubMetrics metrics, long timeInSeconds) {
        return Point.measurement(MEASUREMENT)
            .time(timeInSeconds, TimeUnit.SECONDS)
            .tag("projectName", metrics.getProjectName())
            .addField("numberOfContributors", metrics.getNumberOfContributors())
            .addField("numberOfStars", metrics.getNumberOfStars())
            .addField("numberOfForks", metrics.getNumberOfForks())
            .addField("numberOfWatchers", metrics.getNumberOfWatchers())
            .addField("numberOfOpenIssues", metrics.getNumberOfOpenIssues())
            .addField("numberOfClosedIssues", metrics.getNumberOfClosedIssues())
            .addField("numberOfCommits", metrics.getNumberOfCommits());
    }

    private long getDateAsSeconds(String dateTime) {
        String date = dateTime.split("T")[0];
        return LocalDate.parse(date).atStartOfDay(ZoneId.of("Z")).toEpochSecond();
    }

    private long getCurrentDateAsSeconds() {
        return LocalDate.now().atStartOfDay(ZoneId.of("Z")).toEpochSecond();
    }

}
