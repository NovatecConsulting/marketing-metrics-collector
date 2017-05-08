package info.novatec.metricscollector.google;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.google.collector.AqeBlog;

@Slf4j
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "google-analytics")
public class Scheduler {

    private final AqeBlog collectorAqeBlog;

    private final MetricsRepository metricsRepository;

    @Scheduled(cron = "${google-analytics.cron}")
    void scheduleUpdateMetricsForAllWebpages() {
        collectorAqeBlog.collect();
        metricsRepository.saveMetrics(collectorAqeBlog.getMetrics(), "AqeBlog5");
    }
}
