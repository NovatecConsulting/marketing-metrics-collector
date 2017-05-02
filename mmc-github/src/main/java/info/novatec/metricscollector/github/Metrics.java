package info.novatec.metricscollector.github;

import java.util.Map;
import java.util.SortedMap;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import info.novatec.metricscollector.commons.PageViews;


@Getter
@Setter
@Component
@NoArgsConstructor
public class Metrics {

    private static final String GITHUB_BASE_URL = "https://github.com/";

    private String githubUrl;
    private String repositoryName;
    private String projectName;
    private Integer contributors;
    private Integer stars;
    private Integer forks;
    private Integer watchers;
    private Integer openIssues;
    private Integer closedIssues;
    private Integer commits;
    private SortedMap<String, Integer> releaseDownloads;
    private PageViews dailyVisits;
    private Map<String, PageViews> referringSitesLast14Days;

    public Metrics(String githubUrl) {
        this.githubUrl = githubUrl;
        extractProjectAndRepositoryNameFromGithubUrl();
    }

    void extractProjectAndRepositoryNameFromGithubUrl() {
        if (githubUrl == null || !githubUrl.startsWith(GITHUB_BASE_URL)) {
            throw new IllegalArgumentException("URL '" + githubUrl + "' must be a valid and complete Github URL");
        }
        String repositoryName = githubUrl.substring(GITHUB_BASE_URL.length());
        try {
            projectName = repositoryName.split("/")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("URL '" + githubUrl + "' must be a valid and complete Github URL");
        }
        this.repositoryName = repositoryName;
    }

}
