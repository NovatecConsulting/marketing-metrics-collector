package info.novatec.metricscollector.github;//package info.novatec.metricscollector.github;
//
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class GithubTest {
//
//    @Autowired
//    private GithubCollector githubCollector;
//
//    @Autowired
//    private GithubRepository githubRepository;
//
//    @Test
//    @Ignore
//    public void sendRequest() throws Exception {
//
//        final String URL_PROJECT = "testIT-LivingDoc/livingdoc-core";
//
//        GithubMetricsResult metrics = githubCollector.collect(URL_PROJECT);
//
//        //Ausgabe aller Metriken
//        System.out.println("Statistics for '" + metrics.getRepositoryName() + "' \n");
//        System.out.println("Downloads:");
//        String totalDownloads = "0";
//        metrics.getReleaseDownloads().entrySet().forEach(entry -> {
//            System.out.println("\t" + entry.getKey() + " : " + entry.getValue());
//        });
//        System.out.println("Referrers:");
//        for (String key : metrics.getReferringSitesLast14Days().keySet()) {
//            System.out.println("\t" + key + " : " + metrics.getReferringSitesLast14Days().get(key));
//        }
//        System.out.println("Visits:");
//        System.out.println("\ttimestamp:" + metrics.getDailyVisits().getTimestamp());
//        System.out.println("\ttotal:" + metrics.getDailyVisits().getTotalVisits());
//        System.out.println("\tunique:" + metrics.getDailyVisits().getUniqueVisits());
//        System.out.println("Contributors:" + metrics.getContributors());
//        System.out.println("Stars:" + metrics.getStars());
//        System.out.println("Forks:" + metrics.getForks());
//        System.out.println("Watchers:" + metrics.getWatchers());
//        System.out.println("Open Issues:" + metrics.getOpenIssues());
//        System.out.println("Closed Issues:" + metrics.getClosedIssues());
//        System.out.println("Commits:" + metrics.getCommits());
//
//    }
//
//    @Test
//    //    @Ignore
//    public void testInflux() {
//        GithubMetricsResult metrics = githubCollector.collect("https://github.com/testIT-LivingDoc/livingdoc-core");
////        metrics.setRepositoryName(metrics.getRepositoryName() + "_suffixTest");
////        githubRepository.setRetention("daily");
//        githubRepository.saveMetrics(metrics);
//    }
//
//}
