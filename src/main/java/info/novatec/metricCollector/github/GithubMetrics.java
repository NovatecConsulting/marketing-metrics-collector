package info.novatec.metricCollector.github;

import java.util.SortedMap;

import lombok.Getter;
import lombok.Setter;

import info.novatec.metricCollector.commons.DailyVisitsEntity;

@Getter
@Setter
class GithubMetrics {

    private String repositoryName;

    private Integer contributors;

    private Integer stars;

    private Integer forks;

    private Integer watchers;

    private Integer openIssues;

    private Integer closedIssues;

    private Integer commits;

    private SortedMap<String, Integer> releaseDownloads;

    private DailyVisitsEntity dailyVisits;

    private SortedMap<String, Integer> referringSitesLast14Days;

    GithubMetrics() { }
}
