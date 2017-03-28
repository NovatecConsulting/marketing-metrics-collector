package info.novatec.metricscollector.github;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import lombok.extern.slf4j.Slf4j;
import info.novatec.metricscollector.commons.ConfigProperties;
import info.novatec.metricscollector.commons.DailyVisitsEntity;
import info.novatec.metricscollector.commons.RestRequester;
import info.novatec.metricscollector.commons.exception.UserDeniedException;


@Slf4j
@Component
class GithubCollector {

    private static final String BASE_URL = "https://api.github.com/repos/";

    private RestRequester restRequester;

    private ConfigProperties properties;

    @Autowired
    GithubCollector(RestRequester restRequester, ConfigProperties properties) {
        this.restRequester = restRequester;
        this.properties = properties;
    }

    private JsonArray buildJsonArray(String serializedJsonObject) {
        JsonReader jsonReader = Json.createReader(new StringReader(serializedJsonObject));
        return jsonReader.readArray();
    }

    private JsonObject buildJsonObject(String serializedJsonObject) {
        JsonReader jsonReader = Json.createReader(new StringReader(serializedJsonObject));
        return jsonReader.readObject();
    }

    GithubMetrics collect(String githubUrl) {

        String projectName = githubUrl.substring("https://github.com/".length());
        setHeadersForGithub();
        GithubMetrics metrics = new GithubMetrics();
        metrics.setRepositoryName(projectName.split("/")[1]);

        try {
            JsonObject projectRepository = buildJsonObject(getProjectRepository(projectName).getBody());

            metrics.setContributors(collectNumberOfContributor(projectName));
            metrics.setStars(collectNumberOfStars(projectRepository));
            metrics.setForks(collectNumberOfForks(projectRepository));
            metrics.setWatchers(collectNumberOfWatchers(projectRepository));
            metrics.setOpenIssues(collectNumberOfOpenIssues(projectRepository));
            metrics.setClosedIssues(collectNumberOfClosedIssues(projectName));
            metrics.setCommits(collectNumberOfCommits(projectName));
            metrics.setDailyVisits(collectDailyVisitsLast14Days(projectName));
            metrics.setReferringSitesLast14Days(collectReferringSitesLast14Days(projectName));
            metrics.setReleaseDownloads(collectNumberOfDownloads(projectRepository, projectName));

            log.info("Collected metrics for " + metrics.getRepositoryName());

        } catch (HttpClientErrorException e) {
            throw new UserDeniedException("No authorized user logged in! Please add a valid oauth token to the properties.");
        }

        return metrics;
    }

    private void setHeadersForGithub() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "token " + properties.getGitHubToken());
        restRequester.setHeaders(httpHeaders);
    }

    private ResponseEntity<String> getProjectRepository(String projectName) {
        return restRequester.sendRequest(BASE_URL + projectName);
    }

    private Integer collectNumberOfContributor(String projectName) {
        String url = BASE_URL + projectName + "/contributors";
        JsonArray contributors = buildJsonArray(restRequester.sendRequest(url).getBody());
        return contributors.size();
    }

    private Integer collectNumberOfStars(JsonObject projectRepository) {
        return projectRepository.getInt("stargazers_count");
    }

    private Integer collectNumberOfForks(JsonObject projectRepository) {
        return projectRepository.getInt("forks_count");
    }

    private Integer collectNumberOfWatchers(JsonObject projectRepository) {
        //note: the keys 'watchers_count' and 'watchers' are actually equals to 'stargazers_count'
        return projectRepository.getInt("subscribers_count");
    }

    private Integer collectNumberOfOpenIssues(JsonObject projectRepository) {
        return projectRepository.getInt("open_issues_count");
    }

    private Integer collectNumberOfClosedIssues(String projectName) {
        String url = BASE_URL + projectName + "/issues/events";
        JsonArray closedIssues = buildJsonArray(restRequester.sendRequest(url).getBody());
        return closedIssues.size();
    }

    private Integer collectNumberOfCommits(String projectName) {
        String url = BASE_URL + projectName + "/commits";
        JsonArray commits = buildJsonArray(restRequester.sendRequest(url).getBody());
        return commits.size();
    }

    /**
     * Collects the number of Downloads for each artifact for each Release.
     * Stores the artifact-name with pattern: 'releaseVersion:artifact'
     *
     * @param projectRepository The projectRepository retrieved from github api v3.
     * Due to improve performance the repository is used to check whether downloads exists or not before requesting for
     * download statistics.
     * If 'null' download statistics will be requested anyway.
     * @param projectName The GitHub repository. pattern: '<user>/<repository>'
     */
    private SortedMap<String, Integer> collectNumberOfDownloads(JsonObject projectRepository, String projectName) {
        SortedMap<String, Integer> allDownloads = new TreeMap<>();

        if (projectRepository == null || projectRepository.getBoolean("has_downloads")) {
            String url = BASE_URL + projectName + "/releases";
            JsonReader jsonReader = Json.createReader(new StringReader(restRequester.sendRequest(url).getBody()));
            JsonArray releases = jsonReader.readArray();
            releases.forEach(obj -> {
                JsonObject release = (( JsonObject ) obj);
                Map<String, Integer> releaseDownloads = release.getJsonArray("assets")
                    .stream()
                    .filter(asset -> (( JsonObject ) asset).getInt("download_count")>0)
                    .collect(Collectors.toMap(
                        asset -> release.getString("tag_name") + ":" + (( JsonObject ) asset).getString("name"),
                        asset -> (( JsonObject ) asset).getInt("download_count")));
                allDownloads.putAll(releaseDownloads);
            });
        }
        return allDownloads;
    }

    private DailyVisitsEntity collectDailyVisitsLast14Days(String projectName) {
        String url = BASE_URL + projectName + "/traffic/views";
        JsonObject visitors = buildJsonObject(restRequester.sendRequest(url).getBody());
        JsonObject visitAsJSON = getYesterdaysVisits(visitors.getJsonArray("views"));
        String timestamp = visitAsJSON.getString("timestamp");
        int totalVisits = visitAsJSON.getInt("count");
        int uniqueVisits = visitAsJSON.getInt("uniques");
        return new DailyVisitsEntity(timestamp, totalVisits, uniqueVisits);
    }

    /**
     * Github API v3 provides page-visits for the last fortnight. These data will be updated every morning, including
     * 'today' from midnight to mordning. To ensure correct data for the whole day, this method checks the latest entry
     * for its timestamp. If it is 'today' the return value is the entry for 'yesterday' resp. next to the last one.
     *
     * @param visits List of visits-metrics received from github api v3
     * @return The entry for yesterdays visits statistics
     */
    private JsonObject getYesterdaysVisits(JsonArray visits) {
        JsonObject yesterdaysVisits = visits.getJsonObject(visits.size() - 1);
        String timestamp = yesterdaysVisits.getString("timestamp").split("T")[0];

        if (LocalDate.parse(timestamp).toEpochDay() == LocalDate.now().toEpochDay()) {
            yesterdaysVisits = visits.getJsonObject(visits.size() - 2);
        }
        return yesterdaysVisits;
    }

    /**
     * Contribution to the traffic from sites referring to the given Github repository @projectName.
     * The method collects the top 10 referrers and their non-unique visits over the last 14 days.
     *
     * @param projectName The GitHub repository. pattern: '<user>/<repository>'
     */
    private SortedMap<String, Integer> collectReferringSitesLast14Days(String projectName) {
        String url = BASE_URL + projectName + "/traffic/popular/referrers";
        JsonArray sitesJsonArray = buildJsonArray(restRequester.sendRequest(url).getBody());
        SortedMap<String, Integer> sitesMap = new TreeMap<>();

        for (int referrerIndex = 0; referrerIndex < sitesJsonArray.size(); referrerIndex++) {
            JsonObject referringSite = sitesJsonArray.getJsonObject(referrerIndex);
            sitesMap.put(referringSite.getString("referrer"), referringSite.getInt("count"));
        }
        return sitesMap;
    }

}
