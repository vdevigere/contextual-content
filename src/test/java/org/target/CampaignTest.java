package org.target;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

import org.assertj.core.api.Condition;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.target.context.UserContext;
import org.target.filters.DateTimeCondition;

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
        DateTime now = new DateTime();
        DateTime dayFromNow = new DateTime().plusDays(1);
        Campaign campaign = new Campaign(1L, "DUMMY", Sets.newSet(contentA, contentB));
        assertThat(campaign.resolveContent("blahblahblahblahblah".getBytes())).isEqualTo(contentA);
        assertThat(campaign.resolveContent("fourfourfourfourfour".getBytes())).isEqualTo(contentB);
    }

    @Test
    public void ensureContentIsResolvedConsistentlyHashCode() {
        DateTime now = new DateTime();
        DateTime dayFromNow = new DateTime().plusDays(1);
        Campaign campaign = new Campaign(1L, "DUMMY", Sets.newSet(contentA, contentB));

        UUID blahUUID = Generators.nameBasedGenerator().generate("blahblahblahblahblah".getBytes());
        logger.debug("BlahUUID:{}", blahUUID);
        assertThat(campaign.resolveContent(blahUUID)).isEqualTo(contentA);
    }

    @Test
    public void testHashCode() throws IncorrectWeightException {
        DateTime now = new DateTime();
        DateTime dayFromNow = new DateTime().plusDays(1);

        Content<String> content1 = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
        Content<String> content2 = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);
        Campaign campaign1 = new Campaign(1L, "DUMMY", Sets.newSet(content1, content2));

        Campaign campaign2 = new Campaign(1L, "DUMMY", Sets.newSet(contentA, contentB));

        assertThat(campaign1).isEqualTo(campaign2);
    }

    @Test
    public void testForNullConditions() throws IncorrectWeightException {
        DateTime now = new DateTime();
        DateTime dayFromNow = new DateTime().plusDays(1);
        UserContext userContext = new UserContext();

        Content<String> content1 = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
        Content<String> content2 = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);
        Campaign campaign = new Campaign(1L, "DUMMY", Sets.newSet(content1, content2));
        assertThat(campaign.matches(userContext)).isFalse();
    }

    @Test
    public void testForAllOfConditionsMatching() throws IncorrectWeightException {
        DateTime now = new DateTime();
        DateTime dayFromNow = new DateTime().plusDays(1);
        UserContext userContext = new UserContext();

        Content<String> content1 = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
        Content<String> content2 = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);
        Campaign campaign = new Campaign(1L, "DUMMY", Sets.newSet(content1, content2));
        Collection<Condition<UserContext>> conditions = new LinkedList<Condition<UserContext>>();
        conditions.add(new Condition<UserContext>(user -> true, "always false"));
        conditions.add(new Condition<UserContext>(user -> true, "always false"));
        campaign.setConditions(conditions);
        assertThat(campaign.matches(userContext)).isTrue();

    }

    @Test
    public void testForSomeOfConditionsMatching() throws IncorrectWeightException {
        DateTime now = new DateTime();
        DateTime dayFromNow = new DateTime().plusDays(1);
        UserContext userContext = new UserContext();

        Content<String> content1 = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
        Content<String> content2 = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);
        Campaign campaign = new Campaign(1L, "DUMMY", Sets.newSet(content1, content2));
        Collection<Condition<UserContext>> conditions = new LinkedList<Condition<UserContext>>();
        conditions.add(new Condition<UserContext>(user -> false, "always false"));
        conditions.add(new Condition<UserContext>(user -> true, "always false"));
        campaign.setConditions(conditions);
        assertThat(campaign.matches(userContext)).isFalse();
    }

    @Test
    public void testForNullUserContext() throws IncorrectWeightException {
        DateTime now = new DateTime();
        DateTime dayFromNow = new DateTime().plusDays(1);

        Content<String> content1 = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
        Content<String> content2 = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);
        Campaign campaign = new Campaign(1L, "DUMMY", Sets.newSet(content1, content2));
        Collection<Condition<UserContext>> conditions = new LinkedList<Condition<UserContext>>();
        conditions.add(new Condition<UserContext>(user -> false, "always false"));
        conditions.add(new Condition<UserContext>(user -> true, "always false"));
        campaign.setConditions(conditions);
        assertThat(campaign.matches(null)).isFalse();
    }

    @Test
    public void testMatchEqualToStartDate() throws IncorrectWeightException {
        DateTime now = new DateTime();
        DateTime weekFromNow = new DateTime().plusWeeks(1);

        Content<String> content1 = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
        Content<String> content2 = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);
        Campaign campaign = new Campaign(1L, "DUMMY", Sets.newSet(content1, content2));
        
        Collection<Condition<UserContext>> conditions = new LinkedList<Condition<UserContext>>();
        conditions.add(DateTimeCondition.isAfterOrEqual(now));
        conditions.add(DateTimeCondition.isBefore(weekFromNow));
        campaign.setConditions(conditions);
        UserContext userContext = new UserContext();
        userContext.setTimeStamp(now);
        assertThat(campaign.matches(userContext)).isTrue();
    }
}
