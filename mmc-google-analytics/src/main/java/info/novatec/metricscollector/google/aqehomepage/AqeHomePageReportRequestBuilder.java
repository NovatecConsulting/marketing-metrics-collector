package info.novatec.metricscollector.google.aqehomepage;

import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import info.novatec.metricscollector.google.IGaReportRequestBuilder;
import org.springframework.stereotype.Component;

@Component
public class AqeHomePageReportRequestBuilder implements IGaReportRequestBuilder {
    @Override
    public GetReportsResponse sendReportRequest() {
        return null;
    }
}
