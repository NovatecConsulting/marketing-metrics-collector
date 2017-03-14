package info.novatec.metricCollector.github;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import info.novatec.metricCollector.commons.DailyVisitsEntity;


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

    private List<DailyVisitsEntity> dailyVisits;

    private SortedMap<String, Integer> referringSitesLast14Days;

    public GithubMetrics() {

        downloadsPerRelease = new TreeMap<>();
        referringSitesLast14Days = new TreeMap<>();

    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getNumberOfContributors() {
        return numberOfContributors;
    }

    public void setNumberOfContributors(Integer numberOfContributors) {
        this.numberOfContributors = numberOfContributors;
    }

    public Integer getNumberOfStars() {
        return numberOfStars;
    }

    public void setNumberOfStars(Integer numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    public Integer getNumberOfForks() {
        return numberOfForks;
    }

    public void setNumberOfForks(Integer numberOfForks) {
        this.numberOfForks = numberOfForks;
    }

    public Integer getNumberOfWatchers() {
        return numberOfWatchers;
    }

    public void setNumberOfWatchers(Integer numberOfWatchers) {
        this.numberOfWatchers = numberOfWatchers;
    }

    public Integer getNumberOfOpenIssues() {
        return numberOfOpenIssues;
    }

    public void setNumberOfOpenIssues(Integer numberOfOpenIssues) {
        this.numberOfOpenIssues = numberOfOpenIssues;
    }

    public Integer getNumberOfClosedIssues() {
        return numberOfClosedIssues;
    }

    public void setNumberOfClosedIssues(Integer numberOfClosedIssues) {
        this.numberOfClosedIssues = numberOfClosedIssues;
    }

    public Integer getNumberOfCommits() {
        return numberOfCommits;
    }

    public void setNumberOfCommits(Integer numberOfCommits) {
        this.numberOfCommits = numberOfCommits;
    }

    public SortedMap<String, Integer> getDownloadsPerRelease() {
        return downloadsPerRelease;
    }

    public void addDownloadsPerRelease(String releaseAndOrArtifact, int numberOfDownloads) {
        downloadsPerRelease.put(releaseAndOrArtifact, numberOfDownloads);
    }

    public List<DailyVisitsEntity> getDailyVisits() {
        return dailyVisits;
    }

    public void setDailyVisits(List<DailyVisitsEntity> dailyVisits) {
        this.dailyVisits = dailyVisits;
    }

    public SortedMap<String, Integer> getReferringSitesLast14Days() {
        return referringSitesLast14Days;
    }

    public void addReferringSitesLast14Days(String referringSite, int visits) {
        referringSitesLast14Days.put(referringSite, visits);
    }
}
