package info.novatec.metricscollector.google;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.MetricHeader;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.ReportData;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;
import com.google.common.collect.Lists;


public class ResponseParserTest {

    ResponseParser responseParser;
    List<Metrics> metricsList;

    @Before
    public void init() {
        responseParser = new ResponseParser();
        metricsList = new ArrayList<>();
    }

    @Test
    public void parseResponseWithNoReportRows() {
        responseParser.parse(createGetReportsResponse(0), metricsList);
        assertThat(metricsList).isEmpty();
    }

    @Test
    public void parseResponseWithOnePageTest() {
        responseParser.parse(createGetReportsResponse(1), metricsList);
        assertMetrics();
    }

    @Test
    public void parseResponseWithMoreThanOneReportsRowTest() {
        responseParser.parse(createGetReportsResponse(3), metricsList);
        assertMetrics();
    }

    @Test
    public void parseDimensionAsPagePathTest() {
        Map<String, String> dimensions = new TreeMap<>();
        dimensions.put(DataProvider.GA_DIMENSION, DataProvider.DIMENSION_VALUE);
        dimensions.put(DataProvider.GA_PAGE_PATH, DataProvider.PAGE_PATH_VALUE);
        GetReportsResponse reportsResponse = createGetReportsResponse(1);
        reportsResponse.getReports().get(0).getColumnHeader().setDimensions(Lists.newArrayList(dimensions.keySet()));
        reportsResponse.getReports()
            .get(0)
            .getData()
            .getRows()
            .get(0)
            .setDimensions(Lists.newArrayList(dimensions.values()));

        responseParser.parse(reportsResponse, metricsList);

        assertMetrics();
        assertThat(metricsList).isNotEmpty();
        assertThat(metricsList.get(0).getPagePath()).isEqualTo(DataProvider.PAGE_PATH_VALUE);
    }

    private void assertMetrics() {
        metricsList.forEach(metrics -> {
            assertThat(metrics.getMetrics().entrySet().size()).isEqualTo(1);
            assertThat(metrics.getMetrics().get(DataProvider.GA_METRIC)).isEqualTo(DataProvider.METRIC_VALUE);

            assertThat(metrics.getDimensions().entrySet().size()).isEqualTo(1);
            assertThat(metrics.getDimensions().get(DataProvider.GA_DIMENSION)).isEqualTo(DataProvider.DIMENSION_VALUE);
        });
    }

    private GetReportsResponse createGetReportsResponse(int numberOfPages) {
        List<ReportRow> rows = createReportRows(numberOfPages);
        List<String> dimensions = Collections.singletonList(DataProvider.GA_DIMENSION);
        MetricHeaderEntry metricHeaderEntry = new MetricHeaderEntry();
        metricHeaderEntry.setName(DataProvider.GA_METRIC);

        List<MetricHeaderEntry> metricHeaderEntries = Collections.singletonList(metricHeaderEntry);

        ReportData reportData = new ReportData();
        reportData.setRows(rows);

        MetricHeader metricHeader = new MetricHeader();
        metricHeader.setMetricHeaderEntries(metricHeaderEntries);

        ColumnHeader columnHeader = new ColumnHeader();
        columnHeader.setDimensions(dimensions);
        columnHeader.setMetricHeader(metricHeader);

        Report report = new Report();
        report.setData(reportData);
        report.setColumnHeader(columnHeader);

        List<Report> reports = Collections.singletonList(report);

        GetReportsResponse getReportsResponse = new GetReportsResponse();
        getReportsResponse.setReports(reports);
        return getReportsResponse;
    }

    private List<ReportRow> createReportRows(int numberOfRows) {
        if (numberOfRows == 0) {
            return null;
        }
        List<ReportRow> rows = new ArrayList<>();
        for (int i = 0; i < numberOfRows; i++) {
            DateRangeValues metric = new DateRangeValues();

            //expect input value 1.12345 returned as 1.12
            metric.setValues(Collections.singletonList(String.valueOf(DataProvider.METRIC_VALUE + 0.00345)));

            List<String> dimensions = Collections.singletonList(DataProvider.DIMENSION_VALUE);
            List<DateRangeValues> metrics = Collections.singletonList(metric);
            ReportRow row = new ReportRow();
            row.setDimensions(dimensions);
            row.setMetrics(metrics);
            rows.add(row);
        }
        return rows;
    }
}
