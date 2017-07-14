package info.novatec.metricscollector.google;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.Report;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class ResponseParser {

    private static final String GA_PAGE_PATH = "ga:pagePath";

    public void parse(GetReportsResponse reportsResponse, List<Metrics> targetListOfMetrics) {
        Report report = reportsResponse.getReports().get(0);
        List<String> dimensionsHeaders = report.getColumnHeader().getDimensions();
        List<MetricHeaderEntry> metricsHeaders = report.getColumnHeader().getMetricHeader().getMetricHeaderEntries();
        final int[] numberOfMCollectedMetrics = { 0 };
        report.getData().getRows().forEach(row -> {
            Metrics metrics = new Metrics();
            List<String> dimensionsValues = row.getDimensions();
            List<String> metricsValues = row.getMetrics().get(0).getValues(); //parse only first daterange
            getDimensionsValues(dimensionsHeaders, dimensionsValues, metrics);
            getMetricsValues(metricsHeaders, metricsValues, metrics);
            numberOfMCollectedMetrics[0] += metrics.getMetrics().size();
            targetListOfMetrics.add(metrics);
        });
        log.info("Collected {} metrics for {} pages.", numberOfMCollectedMetrics[0], targetListOfMetrics.size());
    }

    private void getDimensionsValues(List<String> dimensionsHeaders, List<String> dimensionsValues, Metrics metrics) {
        for (int headersIndex = 0; headersIndex < dimensionsHeaders.size(); headersIndex++) {
            String headerName = dimensionsHeaders.get(headersIndex);
            String fieldValue = dimensionsValues.get(headersIndex);
            if (headerName.equals(GA_PAGE_PATH)) {
                metrics.setPagePath(fieldValue);
            } else {
                metrics.addDimension(headerName, fieldValue);
            }
        }
    }

    private void getMetricsValues(List<MetricHeaderEntry> metricsHeaders, List<String> metricsValues, Metrics metrics) {
        for (int headersIndex = 0; headersIndex < metricsHeaders.size(); headersIndex++) {
            String headerName = metricsHeaders.get(headersIndex).getName();
            Double fieldValue = cutDecimals(metricsValues.get(headersIndex));
            metrics.addMetric(headerName, fieldValue);
        }
    }

    private Double cutDecimals(String number) {
        String[] result = number.split("\\.");
        if (result.length > 1 && result[1].length() > 1) {
            number = result[0] + "." + result[1].substring(0, 2);
        }
        return Double.parseDouble(number);
    }

}
