package org.target.db

import org.junit.{Before, Test}
import org.target.{Content, Campaign}
import org.assertj.core.api.Assertions._

/**
 * Created by Viddu on 6/14/2015.
 */
class CampaignDbTest {
  var campaign: Campaign = null
  val content1 = new Content("A", "A Content", 0L, "Banner A", 75.0)
  val content2 = new Content("B", "B Content", 0L, "Banner B", 25.0)

  @Before
  def createCampaign(): Unit ={
    campaign = new Campaign("MOCK", Array(content1, content2).toSet)
    CampaignDb.create(campaign)
  }

  @Test
  def testCreate(): Unit = {
    val retrievedCampaign = CampaignDb.read(campaign.id)
    assertThat(campaign).isEqualTo(retrievedCampaign)
  }

  @Test
  def testDelete(): Unit ={
    CampaignDb.delete(campaign.id)
    val retrievedCampaign = CampaignDb.read(campaign.id)
    assertThat(retrievedCampaign).isNull
  }

  @Test
  def testUpdate(): Unit ={
    val newCampaign = new Campaign(campaign.id, "DUMMY", Array(content1, content2).toSet)
    CampaignDb.update(newCampaign)
    val retrievedCampaign = CampaignDb.read(campaign.id)
    assertThat(newCampaign).isEqualTo(retrievedCampaign)
  }
}
