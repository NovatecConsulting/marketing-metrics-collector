package info.novatec.metricscollector.google.aqeblog;

import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.*;
import info.novatec.metricscollector.google.ConfigProperties;
import info.novatec.metricscollector.google.ReportRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static info.novatec.metricscollector.google.GoogleAnalyticsDimensionsEnum.*;
import static info.novatec.metricscollector.google.GaFilterOperatorsEnum.*;
import static info.novatec.metricscollector.google.GoogleAnalyticsMetricsEnum.*;
import static java.time.format.DateTimeFormatter.*;

@Component
public class AqeBlogReportRequestBuilder implements ReportRequestBuilder {

    private AnalyticsReporting service;

    private ConfigProperties configProperties;

    @Autowired
    public AqeBlogReportRequestBuilder(AnalyticsReporting service, ConfigProperties configProperties) {
        this.service = service;
        this.configProperties = configProperties;
    }

    @Override
    public GetReportsResponse sendReportRequest() throws IOException {
        DateRange dateRange = createDataRangeWithEqualStartEndDate(getCurrentDateMinusNdays(1));
        List<DimensionFilter> dimensionFilters = new ArrayList<>();
        List<ReportRequest> reportRequests = new ArrayList<>();

        DimensionFilter hostNameDimensionFilter = createDimensionFilter(GA_HOST_NAME.toString(), EXACT.toString(),
                configProperties.getHostName());
        dimensionFilters.add(hostNameDimensionFilter);
        DimensionFilterClause dimensionFilterClause = createDimensionFilterClause(AND.toString(), dimensionFilters);

        ReportRequest reportRequest = createReportRequest(configProperties.getViewId(), dateRange, dimensionFilterClause);
        reportRequests.add(reportRequest);

        GetReportsRequest getReport = new GetReportsRequest()
                .setReportRequests(reportRequests);

        return service.reports().batchGet(getReport).execute();
    }

    /**
     * Creates the DataRange Object by providing it with start and end date
     *
     * @param startDate the begin of the period as String
     * @param endDate   the end of the period as String
     * @return the DataRange object
     */
    private DateRange createDateRange(String startDate, String endDate) {
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(startDate);
        dateRange.setEndDate(endDate);
        return dateRange;
    }

    private DateRange createDataRangeWithEqualStartEndDate(String date) {
        return createDateRange(date, date);
    }

    /**
     * Creates the Metric objects with its required values. Metric is a termin coming from Google Analytics Reporting API.
     * Maximum number of metrics for a GA Request is 10
     *
     * @return List of Metric objects
     */
    private List<Metric> createMetrics() {
        return Arrays.asList(
                new Metric().setExpression(GA_PAGE_VIEWS.toString()),
                new Metric().setExpression(GA_UNIQUE_PAGE_VIEWS.toString()),
                new Metric().setExpression(GA_BOUNCES.toString()),
                new Metric().setExpression(GA_SESSION.toString()),
                new Metric().setExpression(GA_BOUNCE_RATE.toString()),
                new Metric().setExpression(GA_AVG_SESSION_DURATION.toString()),
                new Metric().setExpression(GA_AVG_TIME_ON_PAGE.toString())
        );
    }

    /**
     * Creates the Dimension objects with its required values. Dimension is a termin coming from Google Analytics Reporting API.
     * Maximum number of Dimensions for a GA request is 7.
     *
     * @return List of Dimension objects
     */
    private List<Dimension> createDimensions() {
        return Arrays.asList(
                new Dimension().setName(GA_PAGE_TITLE.toString()),
                new Dimension().setName(GA_HOST_NAME.toString()),
                new Dimension().setName(GA_PAGE_PATH.toString()));
    }

    /**
     * Creates DimensionFiler object with its name, operator and value for comparison.
     * DimensionFiler is used when additional filtering condition is required on a dimension.
     *
     * @param name            the name of the dimension to be filtered on.
     * @param operator        operator that can be used for filtering.
     * @param comparisonValue value against which the dimension is compared
     * @return the DimensionFilter object
     */
    private DimensionFilter createDimensionFilter(String name, String operator, String comparisonValue) {
        return new DimensionFilter()
                .setDimensionName(name)
                .setOperator(operator)
                .setExpressions(Collections.singletonList(comparisonValue));
    }

    /**
     * Creates DimensionFilterClause object. It serves as a unification of more than one dimension filters.
     *
     * @param operator         the operator for joining the dimension filters like AND, OR.
     * @param dimensionFilters list of DimensionFilter objects
     * @return the DimensionFilterClause object
     */
    private DimensionFilterClause createDimensionFilterClause(String operator, List<DimensionFilter> dimensionFilters) {
        return new DimensionFilterClause()
                .setOperator(operator)
                .setFilters(dimensionFilters);
    }

    private ReportRequest createReportRequest(String viewId, DateRange dateRange, DimensionFilterClause dimensionFilterClause) {
        return new ReportRequest()
                .setViewId(viewId)
                .setDateRanges(Collections.singletonList(dateRange))
                .setMetrics(createMetrics())
                .setDimensions(createDimensions())
                .setDimensionFilterClauses(Collections.singletonList(dimensionFilterClause));
    }

    private String getCurrentDateMinusNdays(long nDays) {
        String gaDateFormatPattern = "yyyy-MM-dd";
        LocalDate localDate = LocalDate.now().minusDays(nDays);
        return localDate.format(ofPattern(gaDateFormatPattern));
    }
}
