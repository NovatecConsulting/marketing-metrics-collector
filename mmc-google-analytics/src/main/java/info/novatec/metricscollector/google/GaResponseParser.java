package info.novatec.metricscollector.google;

import com.google.api.services.analyticsreporting.v4.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

import static info.novatec.metricscollector.google.GaDimensionsEnum.GA_PAGE_PATH;

@Component
@Slf4j
public class GaResponseParser {
    private String pagePath;

    public List<Map<String, Map<String, Object>>> parseResponse(GetReportsResponse reportsResponse) {
        List<Map<String, Map<String, Object>>> responseList = new ArrayList<>();
        if (reportsResponse.getReports().size() == 0) {
            log.info("No data for the requested report has been found.");
        }
        reportsResponse.getReports().forEach(report -> {
                    ColumnHeader columnHeader = report.getColumnHeader();
                    List<String> dimensionHeaders = columnHeader.getDimensions();
                    List<MetricHeaderEntry> metricHeaders = columnHeader.getMetricHeader().getMetricHeaderEntries();
                    List<ReportRow> reportRows = report.getData().getRows();
                    Map<String, Map<String, Object>> pagePathDataMap = new HashMap<>();

                    reportRows.forEach(row -> {
                        List<String> dimensionsData = row.getDimensions();
                        List<DateRangeValues> metricsData = row.getMetrics();
                        Map<String, Object> innerDataMap = new HashMap<>();
                        List<GaDimensionsEnum> gaDimensions = getGaDimensionsAsList();

                        gaDimensions.forEach(gaDimension -> {
                            setPagePath(dimensionHeaders, dimensionsData, gaDimension.toString());
                            addReportDataElementsIntoInnerMap(dimensionHeaders, dimensionsData,
                                    gaDimension.toString(), innerDataMap);
                        });

                        metricsData.forEach(values -> {
                            List<GaMetricsEnum> gaMetrics = getGaMetricsAsList();
                            List<String> metricValues = values.getValues();
                            List<String> metricHeadersAsString = getMetricHeaderEntriesAsStringList(metricHeaders);
                            gaMetrics.forEach(gaMetric -> addReportDataElementsIntoInnerMap(metricHeadersAsString, metricValues,
                                    gaMetric.toString(), innerDataMap));
                        });

                        pagePathDataMap.put(pagePath, innerDataMap);
                    });
                    log.info(String.format("Total count of result rows is %d.", reportRows.size()));
                    responseList.add(pagePathDataMap);
                }
        );
        return responseList;
    }

    private void addReportDataElementsIntoInnerMap(List<String> headers, List<String> elementValues, String elementName,
                                                   Map<String, Object> innerDataMap) {
        int index;
        if (headers.contains(elementName)) {
            index = headers.indexOf(elementName);
            try {
                innerDataMap.put(elementName, elementValues.get(index));
                log.info("Elements ( " + elementName + ", " + elementValues.get(index) + " ) added to map.");
            } catch (IndexOutOfBoundsException e) {
                log.error(e.getMessage());
            }
        } else {
            log.info("No element with name " + elementName + " is found in header list.");
        }
    }

    private void setPagePath(List<String> dimensionHeaders, List<String> dimensionValues, String gaDimensionsValue) {
        if (gaDimensionsValue.equals(GA_PAGE_PATH.toString())) {
            int pagePathIndex = dimensionHeaders.indexOf(gaDimensionsValue);
            try {
                pagePath = dimensionValues.get(pagePathIndex);
                log.info("Page path: " + pagePath + " added.");
            } catch (IndexOutOfBoundsException e) {
                log.error(e.getMessage());
            }
        }
    }

    private List<GaDimensionsEnum> getGaDimensionsAsList() {
        return new ArrayList<>(Arrays.asList(GaDimensionsEnum.values()));
    }

    private List<GaMetricsEnum> getGaMetricsAsList() {
        return new ArrayList<>(Arrays.asList(GaMetricsEnum.values()));
    }

    private List<String> getMetricHeaderEntriesAsStringList(List<MetricHeaderEntry> metricHeaderEntries) {
        List<String> metricHeaders = new ArrayList<>();
        metricHeaderEntries.forEach(metricHeaderEntry -> metricHeaders.add(metricHeaderEntry.getName()));
        return metricHeaders;
    }
}
