package info.novatec.metricCollector.github;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricCollector.commons.DailyVisitsEntity;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTest {

    @Autowired
    private GithubCollector githubCollector;

    private static final String URL_PROJECT = "testIT-LivingDoc/livingdoc-core";

    @Test
    public void sendRequest() throws Exception {
        GithubMetrics metrics = githubCollector.collect(URL_PROJECT);

        //Ausgabe aller Metriken
        System.out.println("Statistics for '" + metrics.getProjectName() + "' \n");
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
        System.out.println("Visits:");
        for (DailyVisitsEntity dailyVisits : metrics.getDailyVisits()){
            System.out.println("\t"
                + "Timestamp:"+dailyVisits.getTimestamp()+"; "
                + "total visits:"+dailyVisits.getTotalVisits()+"; "
                + "unique visits:"+dailyVisits.getUniqueVisits());
        }
        System.out.println("Contributors:"+metrics.getNumberOfContributors());
        System.out.println("Stars:"+metrics.getNumberOfStars());
        System.out.println("Forks:"+metrics.getNumberOfForks());
        System.out.println("Watchers:"+metrics.getNumberOfWatchers());
        System.out.println("Open Issues:"+metrics.getNumberOfOpenIssues());
        System.out.println("Closed Issues:"+metrics.getNumberOfClosedIssues());
        System.out.println("Commits:"+metrics.getNumberOfCommits());

    }

}
