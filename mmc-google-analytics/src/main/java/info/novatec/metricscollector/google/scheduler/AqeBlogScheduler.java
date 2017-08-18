package info.novatec.metricscollector.google.scheduler;

import javax.validation.constraints.NotNull;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.google.MetricsRepository;
import info.novatec.metricscollector.google.collector.AqeBlog;


/**
 * This is a scheduler for the AQE blog website, that collects all the defined metrics for the blog and saves them in the
 * database.
 * The scheduler execution period is defined in a cron expression.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AqeBlogScheduler {

    @NotNull
    private final AqeBlog aqeBlogCollector;

    @NotNull
    private final MetricsRepository metricsRepository;

    @Scheduled(cron = "${ga.cron}")
    public void updateMetrics() {
        aqeBlogCollector.collect();
        metricsRepository.saveMetrics(aqeBlogCollector.getMetrics(), "AqeBlogMeasurement");
    }
}
