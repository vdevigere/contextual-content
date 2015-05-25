package org.target.context;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

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
        
        Calendar today = Calendar.getInstance();
        
        Calendar _1WeekFromToday = Calendar.getInstance();
        _1WeekFromToday.add(Calendar.DAY_OF_MONTH, 7);
        
        Calendar _2WeeksFromToday = Calendar.getInstance();
        _2WeeksFromToday.add(Calendar.DAY_OF_MONTH, 14);
        
        Calendar _1WeekAgoFromToday = Calendar.getInstance();
        _1WeekAgoFromToday.add(Calendar.DAY_OF_MONTH, -7);
        
        this.currentCampaign = new Campaign(1L, "DUMMY", _1WeekAgoFromToday, _1WeekFromToday, Sets.newSet(contentA, contentB));
        this.futureCampaign = new Campaign(1L, "DUMMY", _1WeekFromToday, _2WeeksFromToday, Sets.newSet(contentA, contentB));
        this.campaignStartsToday = new Campaign(1L, "DUMMY", today, _1WeekFromToday, Sets.newSet(contentA, contentB));
        this.campaignEndsToday = new Campaign(1L, "DUMMY",_1WeekAgoFromToday, today, Sets.newSet(contentA, contentB));
        
    }


    @Test
    public void testIsSatisfiedByCampaignInRange() {
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(Calendar.getInstance());
        assertTrue(userContext.isSatisfiedBy(currentCampaign));
    }

    @Test
    public void testIsNotSatisfiedByCampaignOutOfRange() {
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(Calendar.getInstance());
        assertFalse(userContext.isSatisfiedBy(futureCampaign));
    }
    
    @Test
    public void testIsSatisfiedByCampaignLowerInclusive(){
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(Calendar.getInstance());
        assertTrue(userContext.isSatisfiedBy(campaignStartsToday));        
    }
    
    @Test
    public void testIsNotSatisfiedByCampaignUpperInclusive(){
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(Calendar.getInstance());
        assertFalse(userContext.isSatisfiedBy(campaignEndsToday));        
    }
}
