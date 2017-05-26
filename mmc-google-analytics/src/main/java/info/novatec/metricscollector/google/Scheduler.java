package info.novatec.metricscollector.google;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.google.collector.AqeBlog;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final AqeBlog collectorAqeBlog;

    private final MetricsRepository metricsRepository;

    @Scheduled(cron = "${ga.cron}")
    void scheduleUpdateMetricsForAllWebpages() {
        collectorAqeBlog.collect();
        metricsRepository.saveMetrics(collectorAqeBlog.getMetrics(), "AqeBlog5");
    }
}
