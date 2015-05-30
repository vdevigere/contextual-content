package org.target.filters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;
import org.target.Campaign;
import org.target.Content;
import org.target.IncorrectWeightException;

import com.fasterxml.uuid.Generators;

public class CampaignFilterTest {

    private Campaign currentCampaign;
    private Campaign futureCampaign;
    private Campaign campaignStartsToday;
    private Campaign campaignEndsToday;
    private List<Campaign> campaignList;
    private Content<String> contentA;
    private Content<String> contentB;
    private DateTime today = new DateTime();

    private DateTime _1WeekFromToday = new DateTime().plusWeeks(1);

    private DateTime _2WeeksFromToday = new DateTime().plusWeeks(2);

    private DateTime _1WeekAgoFromToday = new DateTime().minusWeeks(1);

    @Before
    public void setupCampaignDateData() throws IncorrectWeightException {
        this.contentA = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
        this.contentB = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);

        this.currentCampaign = new Campaign(1L, "CURRENT", _1WeekAgoFromToday, _1WeekFromToday, Sets.newSet(contentA,
                contentB));
        this.futureCampaign = new Campaign(1L, "FUTURE", _1WeekFromToday, _2WeeksFromToday, Sets.newSet(contentA,
                contentB));
        this.campaignStartsToday = new Campaign(1L, "START_TODAY", today, _1WeekFromToday, Sets.newSet(contentA,
                contentB));
        this.campaignEndsToday = new Campaign(1L, "END_TODAY", _1WeekAgoFromToday, today, Sets.newSet(contentA,
                contentB));
        
        campaignList = new LinkedList<Campaign>();
        campaignList.add(currentCampaign);
        campaignList.add(campaignStartsToday);
        campaignList.add(campaignEndsToday);
        campaignList.add(futureCampaign);
    }

    @Test
    public void testSuccessfulFilter() {
        UUID blahUUID = Generators.nameBasedGenerator().generate("blahblahblahblahblah".getBytes());
        CampaignFilter campaignFilter = new CampaignFilter(blahUUID);
        Map<String, Content<?>> resolvedContent = campaignFilter.filter(campaignList,
                campaign -> true);
        assertThat(resolvedContent.keySet(), containsInAnyOrder("CURRENT", "FUTURE", "START_TODAY", "END_TODAY"));
        assertThat(resolvedContent.size(), equalTo(4));
    }

    @Test
    public void testUnsuccessfulFilter() {
        UUID blahUUID = Generators.nameBasedGenerator().generate("blahblahblahblahblah".getBytes());
        CampaignFilter campaignFilter = new CampaignFilter(blahUUID);
        Map<String, Content<?>> resolvedContent = campaignFilter.filter(campaignList,
                campaign -> false);
        assertThat(resolvedContent.keySet(),empty());
    }

}
