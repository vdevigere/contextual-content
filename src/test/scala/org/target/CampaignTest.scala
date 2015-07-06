package org.target

import java.io.StringReader

import com.fasterxml.uuid.Generators
import org.apache.lucene.analysis.core.KeywordTokenizer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.DateTools
import org.apache.lucene.document.DateTools.Resolution
import org.apache.lucene.index.memory.MemoryIndex
import org.apache.lucene.queryparser.classic.QueryParser
import org.assertj.core.api.Assertions._
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import org.junit.{Before, Test}
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

  @Test
  def testUUID(): Unit = {
    val content1 = new Content("A", "A Content", 0L, "Banner A", 75.0)
    val content2 = new Content("B", "B Content", 0L, "Banner B", 25.0)
    val campaign1 = new Campaign("DUMMY", Array(content1, content2).toSet)
    val campaign2 = new Campaign("FOO", Array(content1, content2).toSet)
    assertThat(campaign1.id).isNotEqualTo(campaign2.id)
  }

  @Test
  def testValidCondition(): Unit = {
    val content1 = new Content("A", "A Content", 0L, "Banner A", 75.0)
    val content2 = new Content("B", "B Content", 0L, "Banner B", 25.0)
    val queryString = "timeStamp:[20140101 TO  20150101]"
    val campaign = new Campaign("DUMMY", Array(content1, content2).toSet, queryString)
    val userDate = DateTimeFormat.forPattern("yyyyMMdd").parseDateTime("20140701")
    val user = new UserContext(userDate)
    assertThat(campaign.condition(user)).isTrue
  }

  @Test
  def testInValidCondition(): Unit = {
    val content1 = new Content("A", "A Content", 0L, "Banner A", 75.0)
    val content2 = new Content("B", "B Content", 0L, "Banner B", 25.0)
    val queryString = "timeStamp:[20140101 TO  20150101]"
    val campaign = new Campaign("DUMMY", Array(content1, content2).toSet, queryString)

    val userDate = DateTimeFormat.forPattern("yyyyMMdd").parseDateTime("20160101")
    val user = new UserContext(userDate)
    assertThat(campaign.condition(user)).isFalse
  }

  @Test
  def testNoCondition(): Unit ={
    val content1 = new Content("A", "A Content", 0L, "Banner A", 75.0)
    val content2 = new Content("B", "B Content", 0L, "Banner B", 25.0)
    val campaign = new Campaign("DUMMY", Array(content1, content2).toSet)
    val userDate = DateTimeFormat.forPattern("yyyyMMdd").parseDateTime("20160101")
    val user = new UserContext(userDate)
    assertThat(campaign.condition(user)).isTrue

  }
}
