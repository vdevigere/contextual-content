package org.target.filters;


import static org.assertj.core.api.Assertions.assertThat;
import static org.target.filters.DateTimeCondition.timeStampAfterOrEqual;
import static org.target.filters.DateTimeCondition.timeStampBefore;

import org.joda.time.DateTime;
import org.junit.Test;
import org.target.context.UserContext;

public class DateTimeConditionTest {

    @Test
    public void testIsAfterOrEqualWithEqualCondition() {
        DateTime now = new DateTime();
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(now);
        assertThat(userContext).matches(timeStampAfterOrEqual(now));
    }

    @Test
    public void testIsAfterOrEqualWithAfterCondition() {
        DateTime now = new DateTime();
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(now.plusDays(1));
        assertThat(userContext).matches(timeStampAfterOrEqual(now));
    }
    @Test
    public void testIsAfterOrEqualWithBeforeCondition() {
        DateTime now = new DateTime();
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(now.minusDays(1));
        assertThat(userContext).matches(timeStampAfterOrEqual(now).negate());
    }
    
    @Test
    public void testIsBeforeWithBeforeCondition() {
        DateTime now = new DateTime();
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(now.minusDays(1));
        assertThat(userContext).matches(timeStampBefore(now));
    }

    @Test
    public void testIsBeforeWithAfterCondition() {
        DateTime now = new DateTime();
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(now.plusDays(1));
        assertThat(userContext).matches(timeStampBefore(now).negate());
    }
    
    @Test
    public void testIsBeforeWithEqualCondition() {
        DateTime now = new DateTime();
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(now);
        assertThat(userContext).matches(timeStampBefore(now).negate());
    }
}
