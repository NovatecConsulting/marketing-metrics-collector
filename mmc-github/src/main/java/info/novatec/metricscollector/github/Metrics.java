package info.novatec.metricscollector.github;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

import info.novatec.metricscollector.commons.PageViews;


@Data
@Component
@NoArgsConstructor
public class Metrics {

    private static final String GITHUB_BASE_URL = "https://github.com/";

    private String githubUrl;
    private String repositoryName;
    private String projectName;

    private SortedMap<String, Integer> releaseDownloads;
    private Map<String, PageViews> referringSitesLast14Days;

    private Map<String, Integer> metrics;

    public Metrics(String githubUrl) {
        this.githubUrl = githubUrl;
        extractProjectAndRepositoryNameFromGithubUrl();
        metrics = new HashMap<>();
    }

    public void addMetric(String metricName, Integer value){
        metrics.put(metricName, value);
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
