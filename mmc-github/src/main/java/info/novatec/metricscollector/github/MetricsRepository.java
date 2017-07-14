package info.novatec.metricscollector.github;

import java.util.ArrayList;
import java.util.List;

import org.influxdb.dto.Point;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.commons.MetricsValidator;
import info.novatec.metricscollector.commons.database.InfluxService;


@Slf4j
@Component
@RequiredArgsConstructor
public class MetricsRepository {

    private final InfluxService influx;

    private final MetricsValidator metricsValidator;

    void saveMetrics(Metrics metrics) {
        influx.savePoint(createPoints(metrics));
        influx.close();
    }

    List<Point> createPoints(Metrics metrics) {
        log.info("Start creating points for repository '{}'.", metrics.getRepositoryName());
        List<Point> points = new ArrayList<>();

        if (metricsValidator.hasNullValues(metrics)) {
            log.error("Since there are null values in metrics, creating points for repository '{}' isn't possible!"
                + "collected metrics:\n{}", metrics.getRepositoryName(), metrics.toString());
            return points;
        }

        Point.Builder point = Point.measurement(metrics.getRepositoryName());
        metrics.getMetrics().forEach((key, value) -> {
            point.addField(key, value);
        });
        metrics.getReleaseDownloads().entrySet().forEach(download -> point.addField(download.getKey(), download.getValue()));
        points.add(point.build());

        metrics.getReferringSitesLast14Days().entrySet().forEach(referrer -> {
            Point pointReferrer = Point.measurement(metrics.getRepositoryName() + "_Referrer")
                .tag("referringSite", referrer.getKey())
                .addField("totalVisitsLast14Days", referrer.getValue().getTotalVisits())
                .addField("uniqueVisitsLast14Days", referrer.getValue().getUniqueVisits())
                .build();
            points.add(pointReferrer);
        });
        log.info("Created {} points for repository '{}'.", points.size(), metrics.getRepositoryName());
        return points;
    }

}
