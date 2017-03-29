package info.novatec.metricscollector.github;

import java.util.Map;
import java.util.SortedMap;

import lombok.Getter;
import lombok.Setter;

import info.novatec.metricscollector.commons.DailyClicks;


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
    private DailyClicks dailyVisits;
    private Map<String, DailyClicks> referringSitesLast14Days;

}
