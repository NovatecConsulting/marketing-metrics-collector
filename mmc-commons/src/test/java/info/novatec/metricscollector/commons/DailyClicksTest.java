package info.novatec.metricscollector.commons;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
public class DailyClicksTest {

    private static final String TIMESTAMP = "aTimestamp";
    private static final Integer TOTAL_VISITS = 2;
    private static final Integer UNIQUE_VISITS = 3;

    @Test
    public void verifyConstructorSettings() {
        DailyClicks dailyClicks = new DailyClicks(TIMESTAMP, TOTAL_VISITS, UNIQUE_VISITS);
        assertThat(dailyClicks.getTimestamp()).isEqualTo(TIMESTAMP);
        assertThat(dailyClicks.getTotalVisits()).isEqualTo(TOTAL_VISITS);
        assertThat(dailyClicks.getUniqueVisits()).isEqualTo(UNIQUE_VISITS);
    }
}
