package info.novatec.metricscollector.twitter;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;


@TestConfiguration
@Import(TwitterApplicationInitializer.class)
public class TestConfig {

}
