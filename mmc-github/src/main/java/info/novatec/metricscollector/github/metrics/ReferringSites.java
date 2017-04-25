package info.novatec.metricscollector.github.metrics;

import java.util.SortedMap;
import java.util.TreeMap;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.springframework.stereotype.Component;

import info.novatec.metricscollector.commons.DailyClicks;
import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


/**
 * Contribution to the traffic from sites referring to the given Github repository @projectName.
 * The method collects the top 10 referrers and their total and unique visits over the last 14 days.
 */
@Component
public class ReferringSites extends GithubMetricAbstract implements GithubMetric {

    public ReferringSites(RestService restService, GithubMetricsResult metrics) {
        super(restService, metrics);
    }

    @Override
    public void collect() {
        String url = BASE_URL + projectName + "/traffic/popular/referrers";
        JsonArray referringSites = createJsonArray(restService.sendRequest(url).getBody());
        SortedMap<String, DailyClicks> sitesMap = new TreeMap<>();

        for (int referrerIndex = 0; referrerIndex < referringSites.size(); referrerIndex++) {
            JsonObject referringSite = referringSites.getJsonObject(referrerIndex);
            String nameOfReferringSite = referringSite.getString("referrer");
            int totalClicks = referringSite.getInt("count");
            int uniqueClicks = referringSite.getInt("uniques");

            DailyClicks referringSiteVisits = new DailyClicks(null, totalClicks, uniqueClicks);
            sitesMap.put(nameOfReferringSite, referringSiteVisits);
        }
        metrics.setReferringSitesLast14Days(sitesMap);
    }
}
