package info.novatec.metricCollector.github;

import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import lombok.extern.slf4j.Slf4j;

import info.novatec.metricCollector.commons.ConfigProperties;
import info.novatec.metricCollector.commons.DailyVisitsEntity;
import info.novatec.metricCollector.commons.RestRequester;
import info.novatec.metricCollector.exception.UserDeniedException;

@Slf4j
@Component
public class GithubCollector {

    private static final String BASE_URL = "https://api.github.com";

    private RestRequester restRequester;

    private ConfigProperties properties;

    @Autowired
    GithubCollector(RestRequester restRequester) {
        this.restRequester = restRequester;
    }

    GithubMetrics collect(String projectName) {

        setHeadersForGithub();
        GithubMetrics githubMetrics = new GithubMetrics();
        githubMetrics.setProjectName(projectName);

        JSONObject projectRepository = new JSONObject(getProjectRepository(projectName).getBody());

        collectNumberOfContributor(githubMetrics, projectName);
        collectNumberOfStars(githubMetrics, projectRepository);
        collectNumberOfForks(githubMetrics, projectRepository);
        collectNumberOfWatchers(githubMetrics, projectRepository);
        collectNumberOfOpenIssues(githubMetrics, projectRepository);
        collectNumberOfClosedIssues(githubMetrics, projectName);
        collectNumberOfCommits(githubMetrics, projectName);
        collectDailyVisitsLast14Days(githubMetrics, projectName);
        collectReferringSitesLast14Days(githubMetrics, projectName);
        collectNumberOfDownloads(githubMetrics, projectRepository, projectName);

        log.info("Collection of ");
        return githubMetrics;
    }

    @Autowired
    public void setProperties(ConfigProperties properties) {
        this.properties = properties;
    }

    private void setHeadersForGithub() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Accept", "application/vnd.github.v3+json");
        httpHeaders.set("Authorization", "token "+properties.getGitHubToken());
        restRequester.setHeaders(httpHeaders);
    }

    private ResponseEntity<String> getProjectRepository(String projectName) {
        return restRequester.sendRequest(BASE_URL + "/repos/" + projectName);
    }

    private void collectNumberOfContributor(GithubMetrics githubMetrics, String projectName) {
        String url = BASE_URL + "/repos/" + projectName + "/contributors";
        JSONArray contributors = new JSONArray(restRequester.sendRequest(url).getBody());
        githubMetrics.setNumberOfContributors(contributors.length());
    }

    private void collectNumberOfStars(GithubMetrics githubMetrics, JSONObject projectRepository) {
        githubMetrics.setNumberOfStars(projectRepository.getInt("stargazers_count"));
    }

    private void collectNumberOfForks(GithubMetrics githubMetrics, JSONObject projectRepository) {
        githubMetrics.setNumberOfForks(projectRepository.getInt("forks_count"));
    }

    private void collectNumberOfWatchers(GithubMetrics githubMetrics, JSONObject projectRepository) {
        //note: the keys 'watchers_count' and 'watchers' are actually equals to 'stargazers_count'
        githubMetrics.setNumberOfWatchers(projectRepository.getInt("subscribers_count"));
    }

    private void collectNumberOfOpenIssues(GithubMetrics githubMetrics, JSONObject projectRepository) {
        githubMetrics.setNumberOfOpenIssues(projectRepository.getInt("open_issues_count"));
    }

    private void collectNumberOfClosedIssues(GithubMetrics githubMetrics, String projectName) {
        String url = BASE_URL + "/repos/" + projectName + "/issues/events";
        JSONArray closedIssues = new JSONArray(restRequester.sendRequest(url).getBody());
        githubMetrics.setNumberOfClosedIssues(closedIssues.length());
    }

    private void collectNumberOfCommits(GithubMetrics githubMetrics, String projectName) {
        String url = BASE_URL + "/repos/" + projectName + "/commits";
        JSONArray commits = new JSONArray(restRequester.sendRequest(url).getBody());
        githubMetrics.setNumberOfCommits(commits.length());
    }

    /**
     * Collects the number of Downloads for each artifact for each Release.
     * Stores the artifact-name with pattern: 'releaseVersion:artifact'
     *
     * @param githubMetrics The model where to store collected data
     * @param projectRepository The projectRepository retrieved from github api v3.
     * Due to improve performance the repository is used to check whether downloads exists or not before requesting for
     * download statistics.
     * If 'null' download statistics will be requested anyway.
     * @param projectName The GitHub repository. pattern: '<user>/<repository>'
     */
    private void collectNumberOfDownloads(GithubMetrics githubMetrics, JSONObject projectRepository, String projectName) {
        if (projectRepository == null || projectRepository.getBoolean("has_downloads")) {
            String url = BASE_URL + "/repos/" + projectName + "/releases";
            JSONArray releases = new JSONArray(restRequester.sendRequest(url).getBody());

            for (int releasesIndex = 0; releasesIndex < releases.length(); releasesIndex++) {
                JSONObject releaseRepository = releases.getJSONObject(releasesIndex);
                JSONArray assets = releaseRepository.getJSONArray("assets");
                for (int assetsIndex = 0; assetsIndex < assets.length(); assetsIndex++) {
                    JSONObject asset = assets.getJSONObject(assetsIndex);
                    String key = releaseRepository.getString("tag_name") + ":" + asset.getString("name");
                    int value = asset.getInt("download_count");
                    githubMetrics.addDownloadsPerRelease(key, value);
                }
            }
        }
    }

    private void collectDailyVisitsLast14Days(GithubMetrics githubMetrics, String projectName) {
        try {
            String url = BASE_URL + "/repos/" + projectName + "/traffic/views";
            JSONObject visitors = new JSONObject(restRequester.sendRequest(url).getBody());
            JSONObject visitAsJSON = getYesterdaysVisits(visitors.getJSONArray("views"));
            String timestamp = visitAsJSON.getString("timestamp");
            int totalVisits = visitAsJSON.getInt("count");
            int uniqueVisits = visitAsJSON.getInt("uniques");
            DailyVisitsEntity dailyVisits = new DailyVisitsEntity(timestamp, totalVisits, uniqueVisits);

            githubMetrics.setDailyVisits(dailyVisits);
        } catch (HttpClientErrorException e) {
            throw new UserDeniedException("No authorized user logged in! Please add a valid oauth token to the properties");
        }
    }

    /**
     * Github API v3 provides page-visits for the last fortnight. These data will be updated every morning, including
     * 'today' from midnight to mordning. To ensure correct data for the whole day, this method checks the latest entry
     * for its timestamp. If it is 'today' the return value is the entry for 'yesterday' resp. next to the last one.
     * @param visits List of visits-metrics received from github api v3
     * @return The entry for yesterdays visits statistics
     */
    private JSONObject getYesterdaysVisits(JSONArray visits){
        JSONObject yesterdaysVisits = visits.getJSONObject(visits.length()-1);
        String timestamp = yesterdaysVisits.getString("timestamp").split("T")[0];

        if(LocalDate.parse(timestamp).toEpochDay() == LocalDate.now().toEpochDay()){
            yesterdaysVisits = visits.getJSONObject(visits.length()-2);
        }
        return yesterdaysVisits;
    }

    /**
     * Contribution to the traffic from sites referring to the given Github repository @projectName.
     * The method collects the top 10 referrers and their non-unique visits over the last 14 days.
     *
     * @param githubMetrics The model where to store collected data
     * @param projectName The GitHub repository. pattern: '<user>/<repository>'
     */
    private void collectReferringSitesLast14Days(GithubMetrics githubMetrics, String projectName) {
        String url = BASE_URL + "/repos/" + projectName + "/traffic/popular/referrers";
        JSONArray referringSites = new JSONArray(restRequester.sendRequest(url).getBody());

        for (int referrerIndex = 0; referrerIndex < referringSites.length(); referrerIndex++) {
            JSONObject referringSite = referringSites.getJSONObject(referrerIndex);
            githubMetrics.addReferringSitesLast14Days(referringSite.getString("referrer"), referringSite.getInt("count"));
        }
    }

}
