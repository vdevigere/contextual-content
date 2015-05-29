package org.target.filters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasKey;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.target.Campaign;
import org.target.Content;
import org.target.IncorrectWeightException;
import org.target.context.UserContext;

import com.fasterxml.uuid.Generators;

public class CampaignFilterTest {
    
    private static Logger logger = LoggerFactory.getLogger(CampaignFilterTest.class);
    
    private Campaign currentCampaign;
    private Campaign futureCampaign;
    private Campaign campaignStartsToday;
    private Campaign campaignEndsToday;
    private List<Campaign> campaignList = new LinkedList<Campaign>();
    private UserContext userContext = new UserContext();

    @Before
    public void setupCampaignDateData() throws IncorrectWeightException {
        Content<String> contentA = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
        Content<String> contentB = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);

        DateTime today = new DateTime();
        
        DateTime _1WeekFromToday = new DateTime().plusWeeks(1);
        
        DateTime _2WeeksFromToday = new DateTime().plusWeeks(2);
        
        DateTime _1WeekAgoFromToday = new DateTime().minusWeeks(1);

        this.currentCampaign = new Campaign(1L, "CURRENT", _1WeekAgoFromToday, _1WeekFromToday, Sets.newSet(contentA,
                contentB));
        this.futureCampaign = new Campaign(1L, "FUTURE", _1WeekFromToday, _2WeeksFromToday, Sets.newSet(contentA,
                contentB));
        this.campaignStartsToday = new Campaign(1L, "START_TODAY", today, _1WeekFromToday, Sets.newSet(contentA,
                contentB));
        this.campaignEndsToday = new Campaign(1L, "END_TODAY", _1WeekAgoFromToday, today, Sets.newSet(contentA,
                contentB));

        campaignList.add(currentCampaign);
        campaignList.add(campaignStartsToday);
        campaignList.add(campaignEndsToday);
        campaignList.add(futureCampaign);
    }

    @Test
    public void testFilterOnToday() {
        UUID blahUUID = Generators.nameBasedGenerator().generate("blahblahblahblahblah".getBytes());
        DateTime today = new DateTime();
        userContext.setTimeStamp(today);
        CampaignFilter campaignFilter = new CampaignFilter(blahUUID);
        Map<String, Content<?>> resolvedContent = campaignFilter.filter(campaignList,
                campaign -> userContext.isSatisfiedBy(campaign));
        assertThat(resolvedContent, hasKey("CURRENT"));
        assertThat(resolvedContent, hasKey("START_TODAY"));
        assertThat(resolvedContent.size(), equalTo(2));
    }
    
    @Test
    public void testFilterOn1WeekFromToday(){
        UUID blahUUID = Generators.nameBasedGenerator().generate("blahblahblahblahblah".getBytes());
        DateTime aWeekFromToday = new DateTime().plusWeeks(1);
        userContext.setTimeStamp(aWeekFromToday);
        CampaignFilter campaignFilter = new CampaignFilter(blahUUID);
        Map<String, Content<?>> resolvedContent = campaignFilter.filter(campaignList,
                campaign -> userContext.isSatisfiedBy(campaign));
        assertThat(resolvedContent, hasKey("FUTURE"));
        assertThat(resolvedContent.size(), equalTo(1));        
    }

}
