package info.novatec.metricscollector.github.util;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import info.novatec.metricscollector.commons.PageViews;
import info.novatec.metricscollector.github.Metrics;
import info.novatec.metricscollector.github.collector.GithubBasicMetricCollector;


public class DataProvider {

    public static final String LOCALDATE_TODAY = "2017-01-01";
    public static final String LOCALDATE_YESTERDAY = "2016-12-31";
    public static final String TIMESTAMP_TODAY = "2017-01-01T00:00:00Z";
    public static final String TIMESTAMP_YESTERDAY = "2016-12-31T00:00:00Z";
    public static final String TIMESTAMP_DAY_BEFORE_YESTERDAY = "2016-12-30T00:00:00Z";

    public static final String NON_EXISTING_PROJECT = "nonExistingProject";

    public static final String VALID_GITHUB_URL = "https://github.com/nt-ca-aqe/marketing-metrics-collector";
    public static final String VALID_GITHUB_REPOSITORY = "nt-ca-aqe/marketing-metrics-collector";
    public static final String VALID_GITHUB_PROJECTNAME = "marketing-metrics-collector";

    private static final String DAILY_VISITS_TIMESTAMP_2001_01_01 = "2001-01-01T00:00:00Z";
    private static final String DAILY_VISITS_TIMESTAMP_2002_02_02 = "2002-02-02T00:00:00Z";

    public static String getRestURL(String repositoryName) {
        return GithubBasicMetricCollector.GITHUB_URL + repositoryName;
    }

    public static Metrics fillMetrics(Metrics metrics) {
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

        PageViews dailyClicks = new PageViews(DAILY_VISITS_TIMESTAMP_2001_01_01, 10, 2);
        metrics.setYesterdaysPageViews(dailyClicks);

        Map<String, PageViews> referringSites = new HashMap<>();
        PageViews referrersVisits = new PageViews(DAILY_VISITS_TIMESTAMP_2001_01_01, 2, 1);
        referringSites.put("www.novatec.de", referrersVisits);
        referringSites.put("www.google.de", referrersVisits);
        referrersVisits = new PageViews(DAILY_VISITS_TIMESTAMP_2002_02_02, 4, 2);
        referringSites.put("www.novatec.de", referrersVisits);
        referringSites.put("www.google.de", referrersVisits);
        metrics.setReferringSitesLast14Days(referringSites);

        return metrics;
    }

    public static Metrics createMetrics(){
        return fillMetrics(createEmptyMetrics());
    }

    public static Metrics createEmptyMetrics(){
        return new Metrics(VALID_GITHUB_URL);
    }

    public static String createResponseBodyWithYesterdaysData() {
        return "{\"views\":["
            + "{" + "\"timestamp\": \""+TIMESTAMP_DAY_BEFORE_YESTERDAY+"\"," + "\"count\": 26," + "\"uniques\": 4" + "},"
            + "{" + "\"timestamp\": \""+TIMESTAMP_YESTERDAY+"\"," + "\"count\": 27," + "\"uniques\": 5" + "}]"
            + "}";
    }

    public static String createResponseBodyWithYesterdaysAndTodaysData() {
        return "{\"views\":["
            + "{" + "\"timestamp\": \""+TIMESTAMP_YESTERDAY+"\"," + "\"count\": 27," + "\"uniques\": 5" + "},"
            + "{" + "\"timestamp\": \""+TIMESTAMP_TODAY+"\"," + "\"count\": 28," + "\"uniques\": 6" + "}]"
            + "}";
    }

    public static LocalDate getLocalDateToday(){
        return LocalDate.parse(LOCALDATE_TODAY);
    }

}
