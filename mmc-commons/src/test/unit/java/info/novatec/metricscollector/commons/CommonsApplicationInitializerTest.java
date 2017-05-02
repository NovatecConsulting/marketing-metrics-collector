package info.novatec.metricscollector.commons;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;


@RunWith(SpringRunner.class)
public class CommonsApplicationInitializerTest {

    CommonsApplicationInitializer initializer;

    @PostConstruct
    public void init(){
        initializer = new CommonsApplicationInitializer();
    }

    @Test
    public void createRestTemplate(){
        RestTemplate influxService = initializer.restTemplate();
        assertThat(influxService).isNotNull();
    }
}
