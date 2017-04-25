package info.novatec.metricscollector.github;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.SortedMap;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

import info.novatec.metricscollector.commons.DailyClicks;


@Getter
@Setter
@Component
public class GithubMetricsResult {

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
        }

        return false;
    }
}
