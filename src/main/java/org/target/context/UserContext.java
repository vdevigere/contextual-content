package org.target.context;

import org.joda.time.DateTime;

public class UserContext {
    private DateTime timeStamp = new DateTime();

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(DateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
