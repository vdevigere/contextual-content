package org.target;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Calendar;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.uuid.Generators;

public class CampaignTest {
    private static final Logger logger = LoggerFactory.getLogger(CampaignTest.class);

    private Content<String> contentA;
    private Content<String> contentB;

    @Before
    public void initContent() throws IncorrectWeightException {
        this.contentA = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
        this.contentB = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);
    }

    @Test
    public void ensureContentIsResolvedConsistently() {
        Calendar now = Calendar.getInstance();
        Calendar dayFromNow = Calendar.getInstance();
        dayFromNow.add(Calendar.DATE, 1);
        Campaign campaign = new Campaign(1L, "DUMMY", now, dayFromNow, Sets.newSet(contentA, contentB));
        assertThat(campaign.resolveContent("blahblahblahblahblah".getBytes()), equalTo(contentA));
        assertThat(campaign.resolveContent("fourfourfourfourfour".getBytes()), equalTo(contentB));
    }

    @Test
    public void ensureContentIsResolvedConsistentlyHashCode() {
        Calendar now = Calendar.getInstance();
        Calendar dayFromNow = Calendar.getInstance();
        dayFromNow.add(Calendar.DATE, 1);
        Campaign campaign = new Campaign(1L, "DUMMY", now, dayFromNow, Sets.newSet(contentA, contentB));

        UUID blahUUID = Generators.nameBasedGenerator().generate("blahblahblahblahblah".getBytes());
        logger.debug("BlahUUID:{}", blahUUID);
        assertThat(campaign.resolveContent(blahUUID), equalTo(contentA));
    }

    @Test
    public void testHashCode() throws IncorrectWeightException {
        Calendar now = Calendar.getInstance();
        Calendar dayFromNow = Calendar.getInstance();

        Content<String> content1 = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
        Content<String> content2 = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);
        Campaign campaign1 = new Campaign(1L, "DUMMY", now, dayFromNow, Sets.newSet(content1, content2));

        Campaign campaign2 = new Campaign(1L, "DUMMY", now, dayFromNow, Sets.newSet(contentA, contentB));

        assertThat(campaign1, equalTo(campaign2));
    }
}
