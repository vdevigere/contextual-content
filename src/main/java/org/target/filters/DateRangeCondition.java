package org.target.filters;

import java.util.function.Predicate;

import org.joda.time.DateTime;
import org.target.context.UserContext;

public class DateRangeCondition implements Predicate<UserContext>{
    private DateTime startDate;
    private DateTime endDate;

    public DateRangeCondition isAfterOrEqual(DateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public  DateRangeCondition isBefore(DateTime endDate) {
        this.endDate = endDate;
        return this;
    }
    
    public DateRangeCondition isBetween(DateTime startDate, DateTime endDate){
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    @Override
    public boolean test(UserContext user) {
        DateTime userTimeStamp = user.timeStamp();
        boolean lLimitTest = (startDate==null) ? true : userTimeStamp.isAfter(startDate) || userTimeStamp.isEqual(startDate);
        boolean uLimitTest = (endDate==null) ? true : userTimeStamp.isBefore(endDate);
        return lLimitTest && uLimitTest;
    }
}
