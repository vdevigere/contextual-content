package org.target.filters;


import static org.assertj.core.api.Assertions.assertThat;
import static org.target.filters.DateTimeCondition.timeStampAfterOrEqual;
import static org.target.filters.DateTimeCondition.*;

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
    
    /*
     * Test timeStampBetween with a date which matches the startDate.
     * expect test to succeed.
     */
    @Test
    public void testTimeStampBetweenWithStartDate(){
        DateTime startDate = new DateTime();
        DateTime endDate = startDate.plusWeeks(1);
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(startDate);
        assertThat(userContext).matches(timeStampBetween(startDate, endDate));        
    }

    /*
     * Test timeStampBetween with a date which is before the startDate.
     * expect test to fail.
     */
    @Test
    public void testTimeStampBetweenWithBeforeStartDate(){
        DateTime startDate = new DateTime();
        DateTime endDate = startDate.plusWeeks(1);
        DateTime dateUnderTest = startDate.minusWeeks(1);
        
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(dateUnderTest);
        assertThat(userContext).matches(timeStampBetween(startDate, endDate).negate());        
    }}
