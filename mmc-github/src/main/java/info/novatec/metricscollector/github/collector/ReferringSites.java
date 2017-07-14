package info.novatec.metricscollector.github.collector;

import java.util.SortedMap;
import java.util.TreeMap;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.commons.model.PageViews;
import info.novatec.metricscollector.commons.rest.RestService;
import info.novatec.metricscollector.github.Metrics;


/**
 * Contribution to the traffic from sites referring to the given Github repository @projectName.
 * The method collects the top 10 referrers and their total and unique visits over the last 14 days.
 */
@Component
public class ReferringSites extends GithubBasicMetricCollector implements GithubMetricCollector {

    public ReferringSites(RestService restService, Metrics metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        String url = getBaseRequestUrl() + "/traffic/popular/referrers";
        JsonArray referringSites = createJsonArray(restService.sendRequest(url).getBody());
        SortedMap<String, PageViews> sitesMap = new TreeMap<>();

        for (int referrerIndex = 0; referrerIndex < referringSites.size(); referrerIndex++) {
            JsonObject referringSite = referringSites.getJsonObject(referrerIndex);
            String nameOfReferringSite = referringSite.getString("referrer");
            int totalClicks = referringSite.getInt("count");
            int uniqueClicks = referringSite.getInt("uniques");

            PageViews referringSiteVisits = new PageViews(null, totalClicks, uniqueClicks);
            sitesMap.put(nameOfReferringSite, referringSiteVisits);
        }
        metrics.setReferringSitesLast14Days(sitesMap);
    }
}
