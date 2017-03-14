package info.novatec.metricCollector.github;

import java.util.SortedMap;
import java.util.TreeMap;


public class GithubMetrics {

    private Integer numberOfContributors;

    private Integer numberOfStars;

    private Integer numberOfForks;

    private Integer numberOfWatchers;

    private Integer numberOfOpenIssues;

    private Integer numberOfCommits;

    private SortedMap<String, Integer> downloadsPerRelease;

    private Integer numberOfUniqueVisitors;

    private SortedMap<String, Integer> referringSitesLast14Days;

    public GithubMetrics(){

        downloadsPerRelease = new TreeMap<>();
        referringSitesLast14Days = new TreeMap<>();
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

    public Integer getNumberOfCommits() {
        return numberOfCommits;
    }

    public void setNumberOfCommits(Integer numberOfCommits) {
        this.numberOfCommits = numberOfCommits;
    }

    public SortedMap<String, Integer> getDownloadsPerRelease() {
        return downloadsPerRelease;
    }

    public void addDownloadsPerRelease(String releaseAndOrArtifact, int numberOfDownloads ) {
        downloadsPerRelease.put(releaseAndOrArtifact, numberOfDownloads);
    }

    public Integer getNumberOfUniqueVisitors() {
        return numberOfUniqueVisitors;
    }

    public void setNumberOfUniqueVisitors(Integer numberOfUniqueVisitors) {
        this.numberOfUniqueVisitors = numberOfUniqueVisitors;
    }

    public SortedMap<String, Integer> getReferringSitesLast14Days() {
        return referringSitesLast14Days;
    }

    public void addReferringSitesLast14Days(String referringSite, int visits) {
        referringSitesLast14Days.put(referringSite, visits);
    }
}
