package info.novatec.metricscollector.twitter;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.collector.TwitterMetricCollector;
import info.novatec.metricscollector.twitter.collector.TwitterMetricCollectorWithImplementations;
import info.novatec.metricscollector.twitter.collector.TwitterMetricCollectorWithoutImplementations;
import info.novatec.metricscollector.twitter.collector.TwitterMetricFakeCollector;
import info.novatec.metricscollector.twitter.exception.TwitterRuntimeException;
import info.novatec.metricscollector.twitter.util.DataProvider;


@RunWith(SpringRunner.class)
public class SchedulerTest {

    @MockBean
    private TwitterProperties properties;

    @MockBean
    private Twitter twitter;

    @MockBean
    private MetricsRepository repository;

    private Scheduler scheduler;

    @Before
    public void init() {
        Map<String, String> users = new HashMap<>();
        users.put(DataProvider.AT_USERNAME + "1", DataProvider.USERNAME + "1");
        users.put(DataProvider.AT_USERNAME + "2", DataProvider.USERNAME + "2");
        scheduler = spy(new Scheduler(properties, twitter, repository));
        when(properties.getUsers()).thenReturn(users);
    }

    @Test
    public void checkIfCollectorAndRepositoryAreInvokedWithUsersTest() throws TwitterException {
        mockApplicationContext(scheduler);
        scheduler.updateAllTwitterUsersMetrics(TwitterMetricCollectorWithImplementations.class);
        verify(scheduler, times(2)).executeCollection(anyObject(), anyObject());
    }

    @Test
    public void tryFindingCollectorsWithInterfaceThatHasNoImplementations() {
        mockApplicationContext(scheduler);
        scheduler.updateAllTwitterUsersMetrics(TwitterMetricCollectorWithoutImplementations.class);
        //since there is no collector implemented, execution is not invoked
        verify(scheduler, times(0)).executeCollection(anyObject(), anyObject());
    }

    @Test
    public void checkThatNextCollectionIsInvokedAfterTwitterExceptionTest() throws TwitterException {
        mockApplicationContext(scheduler);
        doThrow(TwitterRuntimeException.class).when(scheduler).executeCollection(anyObject(), anyObject());
        scheduler.updateAllTwitterUsersMetrics(TwitterMetricCollectorWithImplementations.class);
        verify(repository, times(0)).saveMetrics(anyObject());
        verify(scheduler, times(2)).executeCollection(anyObject(), anyObject());
    }

    @Test
    public void checkIfCollectionIsInvokedBySchedulerMethodTest() {
        doNothing().when(scheduler).updateAllTwitterUsersMetrics(TwitterMetricCollector.class);
        scheduler.scheduleUpdateAllTwitterUsersMetrics();
        verify(scheduler, times(1)).updateAllTwitterUsersMetrics(TwitterMetricCollector.class);
    }

    private void mockApplicationContext(Scheduler scheduler) {
        ApplicationContext mockedContext = mock(ApplicationContext.class);
        scheduler.setApplicationContext(mockedContext);
        Map<String, TwitterMetricFakeCollector> mockedBeanMap = mock(Map.class);
        TwitterMetricFakeCollector mockedBean = mock(TwitterMetricFakeCollector.class);
        List<TwitterMetricFakeCollector> beanList = new ArrayList<>();
        beanList.add(mockedBean);
        doReturn(mockedBeanMap).when(mockedContext).getBeansOfType(TwitterMetricCollectorWithImplementations.class);
        doReturn(beanList).when(mockedBeanMap).values();
    }
}
