package info.novatec.metricCollector.github;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTest {

    @Autowired
    GithubCollector githubCollector;

    private static final String URL_ZEN = "https://api.github.com/zen";

    private static final String URL_PROJECT = "testIT-LivingDoc/livingdoc-core";

    @PostConstruct
    public void init() {
        RestTemplateBuilder templateBuilder = new RestTemplateBuilder();
    }

    @Test
    public void sendRequest() throws Exception {
        GithubMetrics metrics = githubCollector.collect(URL_PROJECT);

        //Ausgabe aller Metriken
        System.out.println("Downloads:");
        int totalDownloads = 0;
        for (String key: metrics.getDownloadsPerRelease().keySet() ) {
            System.out.println("\t"+key + " : " + metrics.getDownloadsPerRelease().get(key));
            totalDownloads += metrics.getDownloadsPerRelease().get(key);
        }
        System.out.println("Total Downloads:"+totalDownloads);
        System.out.println("Referrers:");
        for (String key: metrics.getReferringSitesLast14Days().keySet() ) {
            System.out.println("\t"+key + " : " + metrics.getReferringSitesLast14Days().get(key));
        }
        System.out.println("Contributors:"+metrics.getNumberOfContributors());
        System.out.println("Stars:"+metrics.getNumberOfStars());
        System.out.println("Forks:"+metrics.getNumberOfForks());
        System.out.println("Watchers:"+metrics.getNumberOfWatchers());
        System.out.println("Open Issues:"+metrics.getNumberOfOpenIssues());
        System.out.println("Commits:"+metrics.getNumberOfCommits());
        System.out.println("Visitors:" + metrics.getNumberOfUniqueVisitors());

    }

}
