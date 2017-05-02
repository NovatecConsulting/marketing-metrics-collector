package info.novatec.metricscollector.github;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;


@TestConfiguration
@Import(GithubApplicationInitializer.class)
public class TestConfig {

}
