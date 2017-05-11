package info.novatec.metricscollector.github;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;


@RunWith(SpringRunner.class)
public class GithubApplicationInitializerTest {

    private GithubApplicationInitializer initializer;

    @Before
    public void init(){
        initializer = new GithubApplicationInitializer();
    }

    @Test
    public void createRestTemplate(){
        RestTemplate influxService = initializer.restTemplate();
        assertThat(influxService).isNotNull();
    }
}
