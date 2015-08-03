package org.target.core

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

import org.apache.lucene.index.memory.MemoryIndex
import org.scalatest.PrivateMethodTester
import org.slf4j.LoggerFactory
import org.target.{UUIDGenarator, UnitSpec}

import scala.collection.JavaConversions._

/**
 * Created by Viddu on 7/8/2015.
 */
class CampaignTest extends UnitSpec {
  val logger = LoggerFactory.getLogger(classOf[CampaignTest])

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
    //val userDate: DateTime = DateTimeFormat.forPattern("yyyyMMdd").parseDateTime("20140701")
    val memoryIndex = new MemoryIndex()
    memoryIndex.addField("timeStamp", memoryIndex.keywordTokenStream(List("20140701")))
    campaign20140101_20150101.condition(memoryIndex) shouldBe true
  }

  "User request date outside of the campaign date range" should "NOT match campaign" in new ContentFixture {
    val queryString = "timeStamp:[20140101 TO  20150101]"
    val campaign20140101_20150101 = new Campaign("DUMMY", Array(contentA, contentB).toSet, queryString)
    val memoryIndex = new MemoryIndex()
    memoryIndex.addField("timeStamp", memoryIndex.keywordTokenStream(List("20160701")))
    campaign20140101_20150101.condition(memoryIndex) shouldBe false
  }


  "Campaigns with no query strings" should "match all user requests" in new ContentFixture {
    val memoryIndex = new MemoryIndex()
    memoryIndex.addField("timeStamp", memoryIndex.keywordTokenStream(List("20160701")))
    campaign.condition(memoryIndex) shouldBe true
  }

  "Campaign with no query string" should "be serialzable" in new ContentFixture {
    // Serialize
    val bufout = new ByteArrayOutputStream()
    val obout = new ObjectOutputStream(bufout)
    obout.writeObject(campaign)

    //De-Serialize
    val bufin = new ByteArrayInputStream(bufout.toByteArray)
    val obin = new ObjectInputStream(bufin)
    val deSerializedCampaign = obin.readObject().asInstanceOf[Campaign]

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
    val deSerializedCampaign = obin.readObject().asInstanceOf[Campaign]

    deSerializedCampaign should equal(campaign20140101_20150101)
  }

  "Campaign with query string" should "be serializable/de-serializable from/to JSON" in new ContentFixture {
    val campaign20140101_20150101 = new Campaign("DUMMY", Array(contentA, contentB).toSet, "timeStamp:[20140101 TO  20150101]", 1L)
    val campaignJson = mapper.writeValueAsString(campaign20140101_20150101)
    logger.debug("Campaign JSON={}", campaignJson)
    val deserializedCampaign = mapper.readValue(campaignJson, classOf[Campaign])
    campaign20140101_20150101 should equal(deserializedCampaign)
  }

  "Campaign" should "have the treeMap constructed correctly" in new ContentFixture with PrivateMethodTester {

    class CampaignSubClass(name: String, contentSet: scala.collection.immutable.Set[Content], queryString: String = "*:*", id: Long = UUIDGenarator.generate.getMostSignificantBits) extends Campaign(name: String, contentSet: scala.collection.immutable.Set[Content], queryString: String, id: Long) {
      def getTreeMap = treeMap
    }

    private val contentC: Content = Content("C", "C Content", 1L, "C".getBytes, 30)
    val myCampaign = new CampaignSubClass("DUMMY", Array(contentA, contentB, contentC).toSet, "timeStamp:[20140101 TO  20150101]", 1L)
    myCampaign.getTreeMap.size should equal(3)
    myCampaign.getTreeMap.lastKey should equal(130.0)
    myCampaign.getTreeMap.get(75.0) should equal(contentA)
    myCampaign.getTreeMap.get(100.0) should equal(contentB)
    myCampaign.getTreeMap.get(130.0) should equal(contentC)
  }
}