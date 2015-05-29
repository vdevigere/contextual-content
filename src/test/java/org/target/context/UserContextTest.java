package org.target.context;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;
import org.target.Campaign;
import org.target.Content;
import org.target.IncorrectWeightException;

public class UserContextTest {

    private Campaign currentCampaign;
    private Campaign futureCampaign;
    private Campaign campaignStartsToday;
    private Campaign campaignEndsToday;

    @Before
    public void setupCampaignDateData() throws IncorrectWeightException {
        Content<String> contentA = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
        Content<String> contentB = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);
        
        DateTime today = new DateTime();
        
        DateTime _1WeekFromToday = new DateTime().plusWeeks(1);
        
        DateTime _2WeeksFromToday = new DateTime().plusWeeks(2);
        
        DateTime _1WeekAgoFromToday = new DateTime().minusWeeks(1);
        
        this.currentCampaign = new Campaign(1L, "DUMMY", _1WeekAgoFromToday, _1WeekFromToday, Sets.newSet(contentA, contentB));
        this.futureCampaign = new Campaign(1L, "DUMMY", _1WeekFromToday, _2WeeksFromToday, Sets.newSet(contentA, contentB));
        this.campaignStartsToday = new Campaign(1L, "DUMMY", today, _1WeekFromToday, Sets.newSet(contentA, contentB));
        this.campaignEndsToday = new Campaign(1L, "DUMMY",_1WeekAgoFromToday, today, Sets.newSet(contentA, contentB));
        
    }


    @Test
    public void testIsSatisfiedByCampaignInRange() {
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(new DateTime());
        assertTrue(userContext.isSatisfiedBy(currentCampaign));
    }

    @Test
    public void testIsNotSatisfiedByCampaignOutOfRange() {
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(new DateTime());
        assertFalse(userContext.isSatisfiedBy(futureCampaign));
    }
    
    @Test
    public void testIsSatisfiedByCampaignLowerInclusive(){
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(new DateTime());
        assertTrue(userContext.isSatisfiedBy(campaignStartsToday));        
    }
    
    @Test
    public void testIsNotSatisfiedByCampaignUpperInclusive(){
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(new DateTime());
        assertFalse(userContext.isSatisfiedBy(campaignEndsToday));        
    }
}
