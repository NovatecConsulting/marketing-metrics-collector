package info.novatec.metricscollector.github;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.SortedMap;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import info.novatec.metricscollector.commons.DailyClicks;


@Getter
@Setter
@Component
@NoArgsConstructor
public class GithubMetricsResult {

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
    private DailyClicks dailyVisits;
    private Map<String, DailyClicks> referringSitesLast14Days;

    public GithubMetricsResult(String githubUrl) {
        this.githubUrl = githubUrl;
        extractProjectAndRepositoryNameFromGithubUrl();
    }

    boolean hasNullValues() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields){
                field.setAccessible(true);
                if(field.get(this)==null){
                    return true;
                }
            }
        }catch(IllegalAccessException e){
            //do nothing. block is never accessed
        }

        return false;
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
