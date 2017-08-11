package info.novatec.metricscollector.github.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.commons.exceptions.UserDeniedException;


@RunWith(SpringRunner.class)
public class UserDeniedExceptionTest {

    private static final String EXCEPTION_MESSAGE = "exception message!";

    @Test
    public void verifyExceptionMessage() {
        try {
            throw new UserDeniedException(EXCEPTION_MESSAGE);
        } catch (UserDeniedException e) {
            assertThat(e.getMessage()).isEqualTo(EXCEPTION_MESSAGE);
        }
    }
}
