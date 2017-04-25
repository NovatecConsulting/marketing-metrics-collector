package info.novatec.metricscollector.github.util;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import info.novatec.metricscollector.commons.DailyClicks;
import info.novatec.metricscollector.github.GithubMetricsResult;
import info.novatec.metricscollector.github.metrics.GithubMetricAbstract;


public class DataProvider {

    public static final String NON_EXISTING_PROJECT = "nonExistingProject";

    private static final String DAILY_VISITS_TIMESTAMP_2001_01_01 = "2001-01-01T00:00:00Z";
    private static final String DAILY_VISITS_TIMESTAMP_2002_02_02 = "2002-02-02T00:00:00Z";

    public String getRestURL() {
        return GithubMetricAbstract.BASE_URL + NON_EXISTING_PROJECT;
    }

    public GithubMetricsResult fillMetrics(GithubMetricsResult metrics) {
        metrics.setRepositoryName(NON_EXISTING_PROJECT);
        metrics.setContributors(1);
        metrics.setStars(2);
        metrics.setForks(3);
        metrics.setWatchers(4);
        metrics.setOpenIssues(5);
        metrics.setClosedIssues(6);
        metrics.setCommits(7);

        SortedMap<String, Integer> releaseDownloads = new TreeMap<>();
        releaseDownloads.put("v1:project1.jar", 1);
        releaseDownloads.put("v1:project2.jar", 0);
        releaseDownloads.put("v2:project1.jar", 2);
        releaseDownloads.put("v2:project2.jar", 1);
        releaseDownloads.put("v3:project1.jar", 3);
        metrics.setReleaseDownloads(releaseDownloads);

        DailyClicks dailyClicks = new DailyClicks(DAILY_VISITS_TIMESTAMP_2001_01_01, 10, 2);
        metrics.setDailyVisits(dailyClicks);

        Map<String, DailyClicks> referringSites = new HashMap<>();
        DailyClicks referrersVisits = new DailyClicks(DAILY_VISITS_TIMESTAMP_2001_01_01, 2, 1);
        referringSites.put("www.novatec.de", referrersVisits);
        referringSites.put("www.google.de", referrersVisits);
        referrersVisits = new DailyClicks(DAILY_VISITS_TIMESTAMP_2002_02_02, 4, 2);
        referringSites.put("www.novatec.de", referrersVisits);
        referringSites.put("www.google.de", referrersVisits);
        metrics.setReferringSitesLast14Days(referringSites);

        return metrics;
    }

}
