package org.target.filters;


import org.joda.time.DateTime;
import org.junit.Test;
import org.target.context.UserContext;

import static org.assertj.core.api.Assertions.*;

public class DateTimeConditionTest {

    @Test
    public void testIsAfterOrEqualWithEqualCondition() {
        DateTime now = new DateTime();
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(now);
        assertThat(userContext).is(DateTimeCondition.isAfterOrEqual(now));
    }

    @Test
    public void testIsAfterOrEqualWithAfterCondition() {
        DateTime now = new DateTime();
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(now.plusDays(1));
        assertThat(userContext).is(DateTimeCondition.isAfterOrEqual(now));
    }
    @Test
    public void testIsAfterOrEqualWithBeforeCondition() {
        DateTime now = new DateTime();
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(now.minusDays(1));
        assertThat(userContext).isNot(DateTimeCondition.isAfterOrEqual(now));
    }
    
    @Test
    public void testIsBeforeWithBeforeCondition() {
        DateTime now = new DateTime();
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(now.minusDays(1));
        assertThat(userContext).is(DateTimeCondition.isBefore(now));
    }

    @Test
    public void testIsBeforeWithAfterCondition() {
        DateTime now = new DateTime();
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(now.plusDays(1));
        assertThat(userContext).isNot(DateTimeCondition.isBefore(now));
    }
    
    @Test
    public void testIsBeforeWithEqualCondition() {
        DateTime now = new DateTime();
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(now);
        assertThat(userContext).isNot(DateTimeCondition.isBefore(now));
    }
}
