package org.target.context;

import java.text.SimpleDateFormat;

import org.apache.lucene.analysis.NumericTokenStream;
import org.apache.lucene.index.memory.MemoryIndex;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.target.Campaign;

public class UserContext {

    private static Logger logger = LoggerFactory.getLogger(UserContext.class);
    private DateTime timeStamp = new DateTime();
    private MemoryIndex index = new MemoryIndex();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(DateTime timeStamp) {
        this.timeStamp = timeStamp;
        NumericTokenStream stream = new NumericTokenStream();
        stream.setLongValue(timeStamp.getMillis());
        index.addField(Campaign.DATE_FIELD, stream);
    }

    public boolean isSatisfiedBy(Campaign campaign) {
        // boolean result = this.timeStamp.after(campaign.getStartDate()) &&
        // this.timeStamp.before(campaign.getEndDate());
        boolean result = index.search(campaign.getQuery()) == 1.0;
        logger.debug("Campaign:{}, startDate:{}, endDate:{}, result:{}", campaign.getName(), campaign.getStartDate(),
                campaign.getEndDate(), result);
        return result;
        // && (campaign.getQuery() != null) ? index.search(campaign.getQuery())
        // == 1.0 : true;
    }
}
