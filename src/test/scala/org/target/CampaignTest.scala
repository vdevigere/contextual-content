package org.target

import com.fasterxml.uuid.Generators
import org.assertj.core.api.Assertions.assertThat
import org.joda.time.DateTime
import org.junit.{Before, Test}

class CampaignTest {

  var contentA: Content[String] = null
  var contentB: Content[String] = null

  @Before
  def initContent = {
    this.contentA = new Content("A", "A Content", 0L, "Banner A", 75.0)
    this.contentB = new Content("B", "B Content", 0L, "Banner B", 25.0)
  }

  @Test
  def testEnsureContentIsResolvedConsistently(){
    val campaign = new Campaign(1L, "DUMMY", Array(contentA, contentB).toSet);
    assertThat(campaign.resolveContent("blahblahblahblahblah".getBytes())).isEqualTo(contentA);
    assertThat(campaign.resolveContent("fourfourfourfourfour".getBytes())).isEqualTo(contentB);
  }

  @Test
  def testEnsureContentIsResolvedConsistentlyHashCode(){
    val campaign = new Campaign(1L, "DUMMY", Array(contentA, contentB).toSet);

    val blahUUID = Generators.nameBasedGenerator().generate("blahblahblahblahblah".getBytes());
    assertThat(campaign.resolveContent(blahUUID)).isEqualTo(contentA);
  }

  @Test
  def testHashCode() {
    val content1 = new Content("A", "A Content", 0L, "Banner A", 75.0);
    val content2 = new Content("B", "B Content", 0L, "Banner B", 25.0);
    val campaign1 = new Campaign(1L, "DUMMY", Array(content1, content2).toSet);

    val campaign2 = new Campaign(1L, "DUMMY", Array(contentA, contentB).toSet);

    assertThat(campaign1).isEqualTo(campaign2);
  }

  //    @Test
  //    public void testForNullConditions() throws IncorrectWeightException {
  //        UserContext userContext = new UserContext(now);
  //        Content<String> content1 = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
  //        Content<String> content2 = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);
  //        Campaign campaign = new Campaign(1L, "DUMMY", Sets.newSet(content1, content2));
  //        assertThat(campaign.matchesAll(userContext)).isFalse();
  //    }
  //
  //    @Test
  //    public void testForAllOfConditionsMatching() throws IncorrectWeightException {
  //        UserContext userContext = new UserContext(now);
  //
  //        Content<String> content1 = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
  //        Content<String> content2 = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);
  //        Campaign campaign = new Campaign(1L, "DUMMY", Sets.newSet(content1, content2));
  //        campaign.setConditions(user -> true, user -> true);
  //        assertThat(campaign.matchesAll(userContext)).isTrue();
  //
  //    }
  //
  //    @Test
  //    public void testForSomeOfConditionsMatching() throws IncorrectWeightException {
  //        UserContext userContext = new UserContext(now);
  //
  //        Content<String> content1 = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
  //        Content<String> content2 = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);
  //        Campaign campaign = new Campaign(1L, "DUMMY", Sets.newSet(content1, content2));
  //        campaign.setConditions(user -> false, user -> true);
  //        assertThat(campaign.matchesAll(userContext)).isFalse();
  //    }
  //
  //    @Test
  //    public void testForNullUserContext() throws IncorrectWeightException {
  //        Content<String> content1 = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
  //        Content<String> content2 = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);
  //        Campaign campaign = new Campaign(1L, "DUMMY", Sets.newSet(content1, content2));
  //        campaign.setConditions(user -> false, user -> true);
  //        assertThat(campaign.matchesAll(null)).isFalse();
  //    }
  //
  //    @Test
  //    public void testMatchEqualToStartDate() throws IncorrectWeightException {
  //        DateTime weekFromNow = new DateTime().plusWeeks(1);
  //
  //        Content<String> content1 = new Content<String>("A", "A Content", 0L, "Banner A", 75.0);
  //        Content<String> content2 = new Content<String>("B", "B Content", 0L, "Banner B", 25.0);
  //        Campaign campaign = new Campaign(1L, "DUMMY", Sets.newSet(content1, content2));
  //        DateRangeCondition range = new DateRangeCondition();
  //
  //        campaign.setConditions(range.isAfterOrEqual(now).and(range.isBefore(weekFromNow)));
  //        UserContext userContext = new UserContext(now);
  //        assertThat(campaign.matchesAll(userContext)).isTrue();
  //    }
}
