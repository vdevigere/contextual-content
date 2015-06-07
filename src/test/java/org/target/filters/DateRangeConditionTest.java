package org.target.filters;


import static org.assertj.core.api.Assertions.assertThat;

import org.joda.time.DateTime;
import org.junit.Test;
import org.target.context.UserContext;

public class DateRangeConditionTest {

    @Test
    public void testIsAfterOrEqualWithEqualCondition() {
        DateTime now = new DateTime();
        DateRangeCondition dtc = new DateRangeCondition().isAfterOrEqual(now);
        UserContext userContext = new UserContext(now);
        assertThat(userContext).matches(dtc);
    }

    @Test
    public void testIsAfterOrEqualWithAfterCondition() {
        DateTime now = new DateTime();
        DateRangeCondition dtc = new DateRangeCondition().isAfterOrEqual(now);
        UserContext userContext = new UserContext(now.plusDays(1));
        assertThat(userContext).matches(dtc);
    }
    @Test
    public void testIsAfterOrEqualWithBeforeCondition() {
        DateTime now = new DateTime();
        DateRangeCondition dtc = new DateRangeCondition().isAfterOrEqual(now);
        UserContext userContext = new UserContext(now.minusDays(1));
        assertThat(userContext).matches(dtc.negate());
    }
    
    @Test
    public void testIsBeforeWithBeforeCondition() {
        DateTime now = new DateTime();
        DateRangeCondition dtc = new DateRangeCondition().isBefore(now);
        UserContext userContext = new UserContext(now.minusDays(1));
        assertThat(userContext).matches(dtc);
    }

    @Test
    public void testIsBeforeWithAfterCondition() {
        DateTime now = new DateTime();
        DateRangeCondition dtc = new DateRangeCondition().isBefore(now);
        UserContext userContext = new UserContext(now.plusDays(1));
        assertThat(userContext).matches(dtc.negate());
    }
    
    @Test
    public void testIsBeforeWithEqualCondition() {
        DateTime now = new DateTime();
        DateRangeCondition dtc = new DateRangeCondition().isBefore(now);
        UserContext userContext = new UserContext(now);
        assertThat(userContext).matches(dtc.negate());
    }
    
    /*
     * Test timeStampBetween with a date which matches the startDate.
     * expect test to succeed.
     */
    @Test
    public void testTimeStampBetweenWithStartDate(){
        DateTime startDate = new DateTime();
        DateTime endDate = startDate.plusWeeks(1);
        DateRangeCondition dtc = new DateRangeCondition().isBetween(startDate, endDate);
        UserContext userContext = new UserContext(startDate);
        assertThat(userContext).matches(dtc);
    }

    /*
     * Test timeStampBetween with a date which is before the startDate.
     * expect test to fail.
     */
    @Test
    public void testTimeStampBetweenWithBeforeStartDate(){
        DateTime startDate = new DateTime();
        DateTime endDate = startDate.plusWeeks(1);
        DateRangeCondition dtc = new DateRangeCondition().isBetween(startDate, endDate);
        DateTime dateUnderTest = startDate.minusWeeks(1);
        
        UserContext userContext = new UserContext(dateUnderTest);
        assertThat(userContext).matches(dtc.negate());
    }}
