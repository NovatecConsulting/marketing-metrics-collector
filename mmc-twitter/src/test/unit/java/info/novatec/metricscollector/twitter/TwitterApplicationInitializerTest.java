package info.novatec.metricscollector.twitter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import twitter4j.conf.Configuration;


@RunWith(SpringRunner.class)
public class TwitterApplicationInitializerTest {

    private static final String CONSUMER_KEY = "aConsumerKey";
    private static final String CONSUMER_SECRET = "aConsumerSecret";
    private static final String ACCESS_TOKEN = "aAccessToken";
    private static final String ACCESS_SECRET = "aAccessSecret";

    @MockBean
    private TwitterProperties properties;

    TwitterApplicationInitializer initializer;

    @Before
    public void init() {
        initializer = new TwitterApplicationInitializer();
        initializer.setProperties(properties);
    }

    @Test
    public void checkIfAllAuthorizationAreInvokedTest() {
        when(properties.getConsumerKey()).thenReturn(CONSUMER_KEY);
        when(properties.getConsumerSecret()).thenReturn(CONSUMER_SECRET);
        when(properties.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(properties.getAccessSecret()).thenReturn(ACCESS_SECRET);
        Configuration configuration = initializer.twitter().getConfiguration();
        assertThat(configuration.getOAuthConsumerKey()).isEqualTo(CONSUMER_KEY);
        assertThat(configuration.getOAuthConsumerSecret()).isEqualTo(CONSUMER_SECRET);
        assertThat(configuration.getOAuthAccessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(configuration.getOAuthAccessTokenSecret()).isEqualTo(ACCESS_SECRET);
    }

    @Test
    public void createUsers() {
        Map<String, String> users = new HashMap<>();
        users.put("at_user1", "full username1");
        users.put("at_user2", "full username2");
        when(properties.getUsers()).thenReturn(users);
        Map<String, String> bean = initializer.users();
        assertThat(bean.size()).isEqualTo(2);
    }
}
