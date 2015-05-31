package org.target.filters;

import java.util.function.Predicate;

import org.assertj.core.api.Condition;
import org.joda.time.DateTime;
import org.target.context.UserContext;

public class DateTimeCondition extends Condition<UserContext> {
    
    public static DateTimeCondition isAfterOrEqual(DateTime dateTime){
        return new DateTimeCondition(userContext -> userContext.getTimeStamp().isAfter(dateTime) || userContext.getTimeStamp().isEqual(dateTime), "%s is after or equal", dateTime);
    }

    public static DateTimeCondition isBefore(DateTime dateTime){
        return new DateTimeCondition(userContext -> userContext.getTimeStamp().isBefore(dateTime), "%s is before", dateTime);
    }

    public DateTimeCondition(Predicate<UserContext> predicate, String description, Object... args) {
        super(predicate, description, args);
    }
    
}
