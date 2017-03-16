package info.novatec.metricCollector.github;

import java.util.SortedMap;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;

import info.novatec.metricCollector.commons.DailyVisitsEntity;

@Getter
@Setter
public class GithubMetrics {

    private String projectName;

    private Integer numberOfContributors;

    private Integer numberOfStars;

    private Integer numberOfForks;

    private Integer numberOfWatchers;

    private Integer numberOfOpenIssues;

    private Integer numberOfClosedIssues;

    private Integer numberOfCommits;

    private SortedMap<String, Integer> downloadsPerRelease;

    private DailyVisitsEntity dailyVisits;

    private SortedMap<String, Integer> referringSitesLast14Days;

    GithubMetrics() {

        downloadsPerRelease = new TreeMap<>();
        referringSitesLast14Days = new TreeMap<>();

    }

    void addDownloadsPerRelease(String releaseAndOrArtifact, int numberOfDownloads) {
        downloadsPerRelease.put(releaseAndOrArtifact, numberOfDownloads);
    }

    void addReferringSitesLast14Days(String referringSite, int visits) {
        referringSitesLast14Days.put(referringSite, visits);
    }
}
