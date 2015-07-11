package org.target.core

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

import org.joda.time.format.DateTimeFormat
import org.target.UnitSpec
import org.target.context.UserContext

/**
 * Created by Viddu on 7/8/2015.
 */
class CampaignTest extends UnitSpec {
  "for a given seed the content" should "consitently resolve to the same value" in new ContentFixture {
    campaign.resolveContent("blahblahblahblahblah".getBytes()) should equal(contentA)
    campaign.resolveContent("fourfourfourfourfour".getBytes()) should equal(contentB)
  }

  "every new campaign instance" should "have unique id" in new ContentFixture {
    val campaign1 = new Campaign("DUMMY", Array(contentA, contentB).toSet)
    val campaign2 = new Campaign("FOO", Array(contentA, contentB).toSet)

    campaign1.id should not equal campaign2.id
  }

  "User request date inside of the campaign date range" should "match campaign" in new ContentFixture {
    val queryString = "timeStamp:[20140101 TO  20150101]"
    val campaign20140101_20150101 = new Campaign("DUMMY", Array(contentA, contentB).toSet, queryString)
    val userDate = DateTimeFormat.forPattern("yyyyMMdd").parseDateTime("20140701")
    val user = new UserContext(userDate)
    campaign20140101_20150101.condition(user) shouldBe true
  }

  "User request date outside of the campaign date range" should "NOT match campaign" in new ContentFixture {
    val queryString = "timeStamp:[20140101 TO  20150101]"
    val campaign20140101_20150101 = new Campaign("DUMMY", Array(contentA, contentB).toSet, queryString)
    val userDate = DateTimeFormat.forPattern("yyyyMMdd").parseDateTime("20160701")
    val user = new UserContext(userDate)
    campaign20140101_20150101.condition(user) shouldBe false
  }


  "Campaigns with no query strings" should "match all user requests" in new ContentFixture {
    val userDate = DateTimeFormat.forPattern("yyyyMMdd").parseDateTime("20160701")
    val user = new UserContext(userDate)
    campaign.condition(user) shouldBe true
  }

  "Campaign with no query string" should "be serialzable" in new ContentFixture {
    // Serialize
    val bufout = new ByteArrayOutputStream()
    val obout = new ObjectOutputStream(bufout)
    obout.writeObject(campaign)

    //De-Serialize
    val bufin = new ByteArrayInputStream(bufout.toByteArray)
    val obin = new ObjectInputStream(bufin)
    val deSerializedCampaign = obin.readObject().asInstanceOf[Campaign[String]]

    deSerializedCampaign should equal(campaign)
  }

  "Campaign with query string" should "be serializable" in new ContentFixture {
    val campaign20140101_20150101 = new Campaign("DUMMY", Array(contentA, contentB).toSet, "timeStamp:[20140101 TO  20150101]")
    val bufout = new ByteArrayOutputStream()
    val obout = new ObjectOutputStream(bufout)
    obout.writeObject(campaign20140101_20150101)

    //De-Serialize
    val bufin = new ByteArrayInputStream(bufout.toByteArray)
    val obin = new ObjectInputStream(bufin)
    val deSerializedCampaign = obin.readObject().asInstanceOf[Campaign[String]]

    deSerializedCampaign should equal(campaign20140101_20150101)
  }
}