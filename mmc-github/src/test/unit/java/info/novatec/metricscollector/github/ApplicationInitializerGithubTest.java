package info.novatec.metricscollector.github;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
public class ApplicationInitializerGithubTest {

    private static final String TOKEN = "aToken";

    ApplicationInitializerGithub initializer;

    @PostConstruct
    public void init(){
        initializer = new ApplicationInitializerGithub();
    }

    @Test
    public void createGithubCollector(){
        GithubCollector githubCollector = initializer.githubCollector(mock(RestService.class));
        assertThat(githubCollector).isNotNull();
    }

    @Test
    public void createToken(){
        initializer.setToken(TOKEN);
        String token = initializer.token();
        assertThat(token).isEqualTo(TOKEN);
    }

}
