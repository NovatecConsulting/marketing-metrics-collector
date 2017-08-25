package info.novatec.metricscollector.google.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.google.MetricsRepository;
import info.novatec.metricscollector.google.collector.AqeHomePage;


/**
 * This is a scheduler for the AQE homepage website, that collects all the defined metrics and saves them in the
 * database.
 * The scheduler execution period is defined in a cron expression.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AqeHomePageScheduler {
    private final AqeHomePage aqeHomePageCollector;

    private final MetricsRepository metricsRepository;

    @Scheduled(cron = "${ga.cron}")
    public void updateMetrics() {
        aqeHomePageCollector.collect();
        metricsRepository.saveMetrics(aqeHomePageCollector.getMetrics(), "AqeHomePageMetric");
    }
}
