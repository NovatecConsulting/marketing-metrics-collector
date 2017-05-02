package info.novatec.metricscollector.twitter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import twitter4j.Twitter;
import twitter4j.conf.Configuration;


@RunWith(SpringRunner.class)
public class TwitterApplicationInitializerTest {

    private static final String CONSUMER_KEY = "aConsumerKey";
    private static final String CONSUMER_SECRET = "aConsumerSecret";
    private static final String ACCESS_TOKEN = "aAccessToken";
    private static final String ACCESS_SECRET = "aAccessSecret";

    TwitterApplicationInitializer initializer;

    @Before
    public void init(){
        initializer = new TwitterApplicationInitializer();
    }

    @Test
    public void checkIfAllAuthorizationAreInvokedTest(){
        initializer.setConsumerKey(CONSUMER_KEY);
        initializer.setConsumerSecret(CONSUMER_SECRET);
        initializer.setAccessToken(ACCESS_TOKEN);
        initializer.setAccessSecret(ACCESS_SECRET);
        Twitter twitter = initializer.twitter();
        Configuration configuration = twitter.getConfiguration();
        assertThat(configuration.getOAuthConsumerKey()).isEqualTo(CONSUMER_KEY);
        assertThat(configuration.getOAuthConsumerSecret()).isEqualTo(CONSUMER_SECRET);
        assertThat(configuration.getOAuthAccessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(configuration.getOAuthAccessTokenSecret()).isEqualTo(ACCESS_SECRET);
    }

    @Test
    public void createUsers(){
        Map<String, String> users = new HashMap<>();
        users.put("at_user1", "full username1");
        users.put("at_user2", "full username2");
        initializer.setUsers(users);
        Map<String, String> bean = initializer.users();
        assertThat(bean.size()).isEqualTo(2);
    }
}
