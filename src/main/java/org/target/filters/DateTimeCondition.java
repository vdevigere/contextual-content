package org.target.filters;

import java.util.function.Predicate;

import org.joda.time.DateTime;
import org.target.context.UserContext;

public class DateTimeCondition{

    public static Predicate<UserContext> timeStampAfterOrEqual(DateTime dateTime) {
        return new Predicate<UserContext>() {

            @Override
            public boolean test(UserContext userContext) {
                return userContext.getTimeStamp().isAfter(dateTime) || userContext.getTimeStamp().isEqual(dateTime);
            }
        };
    }

    public static Predicate<UserContext> timeStampBefore(DateTime dateTime) {
        return new Predicate<UserContext>() {

            @Override
            public boolean test(UserContext userContext) {
                return userContext.getTimeStamp().isBefore(dateTime);
            }
        };
    }
}
