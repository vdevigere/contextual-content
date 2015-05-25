package org.target.context;

import java.util.Calendar;

import org.apache.lucene.analysis.NumericTokenStream;
import org.apache.lucene.index.memory.MemoryIndex;
import org.target.Campaign;

public class UserContext {
    private Calendar timeStamp = Calendar.getInstance();
    private MemoryIndex index = new MemoryIndex();

    public Calendar getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Calendar timeStamp) {
        this.timeStamp = timeStamp;
        NumericTokenStream stream = new NumericTokenStream();
        stream.setLongValue(timeStamp.getTimeInMillis());
        index.addField(Campaign.DATE_FIELD, stream);
    }

    public boolean isSatisfiedBy(Campaign campaign) {
        return index.search(campaign.getQuery()) == 1.0;
    }
}
