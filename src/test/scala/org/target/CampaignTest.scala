package org.target

import com.fasterxml.uuid.Generators
import org.assertj.core.api.Assertions.assertThat
import org.joda.time.DateTime
import org.junit.{Before, Test}
import org.target.condition.UserTimeStamp
import org.target.context.UserContext

class CampaignTest {

  var contentA: Content[String] = null
  var contentB: Content[String] = null
  var now: DateTime = new DateTime()

  @Before
  def initContent = {
    this.contentA = new Content("A", "A Content", 0L, "Banner A", 75.0)
    this.contentB = new Content("B", "B Content", 0L, "Banner B", 25.0)
  }

  @Test
  def testEnsureContentIsResolvedConsistently() {
    val campaign = new Campaign(1L, "DUMMY", Array(contentA, contentB).toSet)
    assertThat(campaign.resolveContent("blahblahblahblahblah".getBytes())).isEqualTo(contentA)
    assertThat(campaign.resolveContent("fourfourfourfourfour".getBytes())).isEqualTo(contentB)
  }

  @Test
  def testEnsureContentIsResolvedConsistentlyHashCode() {
    val campaign = new Campaign(1L, "DUMMY", Array(contentA, contentB).toSet)

    val blahUUID = Generators.nameBasedGenerator().generate("blahblahblahblahblah".getBytes())
    assertThat(campaign.resolveContent(blahUUID)).isEqualTo(contentA)
  }

  @Test
  def testHashCode() {
    val content1 = new Content("A", "A Content", 0L, "Banner A", 75.0)
    val content2 = new Content("B", "B Content", 0L, "Banner B", 25.0)
    val campaign1 = new Campaign(1L, "DUMMY", Array(content1, content2).toSet)

    val campaign2 = new Campaign(1L, "DUMMY", Array(contentA, contentB).toSet)
    assertThat(campaign1).isEqualTo(campaign2)
  }
}
