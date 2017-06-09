package info.novatec.metricscollector.google;

import static info.novatec.metricscollector.google.DimensionFilterOperators.EXACT;
import static info.novatec.metricscollector.google.DimensionFilterOperators.NOT;
import static info.novatec.metricscollector.google.GoogleAnalyticsProperties.*;
import static java.time.format.DateTimeFormatter.ofPattern;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.DimensionFilter;
import com.google.api.services.analyticsreporting.v4.model.DimensionFilterClause;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;

import lombok.RequiredArgsConstructor;

import info.novatec.metricscollector.google.exception.IORuntimeException;

@Component
@RequiredArgsConstructor
public class RequestBuilder {

    private static final String AND = "AND";

    private final GoogleAnalyticsProperties properties;
    private final AnalyticsReporting service;
    private List<Metric> metrics;
    private List<Dimension> dimensions;
    private List<DimensionFilter> dimensionFilters;
    private DateRange dateRange;
    private GetReportsRequest reportsRequest;

    //TODO Add Java Doc
    public RequestBuilder prepareRequest() {
        metrics = new ArrayList<>();
        dimensions = new ArrayList<>();
        dimensionFilters = new ArrayList<>();

        addDimensionFilters(GA_HOSTNAME, EXACT, Collections.singletonList(properties.getAqeBlog().getHostName()));
        LocalDate currentDateMinusOne = LocalDate.now().minusDays(1);
        addDateRange(currentDateMinusOne, currentDateMinusOne);
        return this;
    }

    /**
     * @param metricNames a list of the Google Analytics Metric identifier
     * Adds all required metrics into the Request Builder. Metric is a termin coming from Google Analytics Reporting API.
     * Maximum number of metrics per single request is 10
     */
    public RequestBuilder addMetrics(List<String> metricNames) {
        metricNames.forEach(metricKey -> metrics.add(new Metric().setExpression(metricKey)));
        return this;
    }

    /**
     * @param dimensionNames a list of the Google Analytics Dimension identifier
     * Adds all required dimensions into the Request Builder. Dimension is a termin coming from Google Analytics Reporting API.
     * Maximum number of Dimensions per single request is 7.
     */
    public RequestBuilder addDimensions(List<String> dimensionNames) {
        dimensionNames.forEach(dimension -> dimensions.add(new Dimension().setName(dimension)));
        return this;
    }

    //TODO This method does more than one thing - creates and adds. Should be splitted.
    /**
     * DimensionFilters are used when additional filtering condition is required on a dimension.
     * @param dimensionName    the name of the dimension to be filtered on
     * @param operator         operator that can be used for filtering
     * @param comparisonValues value against which the dimension is compared
     */
    public RequestBuilder addDimensionFilters(String dimensionName, DimensionFilterOperators operator, List<String> comparisonValues) {
        DimensionFilter filter = new DimensionFilter()
                .setDimensionName(dimensionName)
                .setExpressions(comparisonValues);
        if (operator.equals(NOT)) {
            filter.setOperator(EXACT.toString()).setNot(true);
        } else {
            filter.setOperator(operator.toString());
        }
        dimensionFilters.add(filter);
        return this;
    }

    /**
     * Defines the date range in which metrics should be collected.
     * In case this method is not invoked, a default value for yesterday's data is used for collection.
     * @param startDate the begin of the period as String
     * @param endDate   the end of the period as String
     */
    private RequestBuilder addDateRange(LocalDate startDate, LocalDate endDate) {
        String pattern = "yyyy-MM-dd";
        dateRange = new DateRange();
        dateRange.setStartDate(startDate.format(ofPattern(pattern)));
        dateRange.setEndDate(endDate.format(ofPattern(pattern)));
        return this;
    }

    /**
     * This method builds the body of the request to be sent
     * @return
     */
    public RequestBuilder buildRequest() {
        List<ReportRequest> reportRequests = new ArrayList<>();

        DimensionFilterClause dimensionFilterClause = createDimensionFilterClause(AND, dimensionFilters);

        ReportRequest reportRequest = createReportRequest(properties.getAqeBlog().getViewId(), dateRange, dimensionFilterClause);
        reportRequests.add(reportRequest);

        reportsRequest = new GetReportsRequest().setReportRequests(reportRequests);
        return this;
    }

    //TODO Add Java Doc
    public GetReportsResponse sendRequest() {
        try {
            return service.reports().batchGet(reportsRequest).execute();
        } catch (IOException e) {
            throw new IORuntimeException("Requesting Google Analytics failed!\n" + e.getMessage());
        }
    }

    /**
     * Creates DimensionFilterClause object. It serves as a unification of more than one dimension filters.
     *
     * @param operator         the operator for joining the dimension filters like AND, OR.
     * @param dimensionFilters list of DimensionFilter objects
     * @return the DimensionFilterClause object
     */
    private DimensionFilterClause createDimensionFilterClause(String operator, List<DimensionFilter> dimensionFilters) {
        return new DimensionFilterClause().setOperator(operator).setFilters(dimensionFilters);
    }

    private ReportRequest createReportRequest(String viewId, DateRange dateRange,
                                              DimensionFilterClause dimensionFilterClause) {
        return new ReportRequest().setViewId(viewId)
                .setDateRanges(Collections.singletonList(dateRange))
                .setMetrics(metrics)
                .setDimensions(dimensions)
                .setDimensionFilterClauses(Collections.singletonList(dimensionFilterClause));
    }

}
