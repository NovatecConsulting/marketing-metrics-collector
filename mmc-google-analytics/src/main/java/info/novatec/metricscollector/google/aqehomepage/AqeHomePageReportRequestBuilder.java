package info.novatec.metricscollector.google.aqehomepage;

import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import info.novatec.metricscollector.google.ReportRequestBuilder;
import org.springframework.stereotype.Component;

@Component
public class AqeHomePageReportRequestBuilder implements ReportRequestBuilder {
    @Override
    public GetReportsResponse sendReportRequest() {
        return null;
    }
}
