package org.target.servlet

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.mockito.Mockito._
import org.scalatra.test.scalatest.ScalatraSuite
import org.slf4j.LoggerFactory
import org.target.UnitSpec
import org.target.core.{Campaign, Content, ContentFixture}
import org.target.db.CampaignDb

/**
 * Created by Viddu on 7/12/2015.
 */
class CampaignServletTest extends UnitSpec with ScalatraSuite with ContentFixture {
  val logger = LoggerFactory.getLogger(classOf[CampaignServletTest])
  implicit val formats = DefaultFormats
  val mockCampaignDb = mock(classOf[CampaignDb])
  addServlet(new CampaignServlet(mockCampaignDb), "/*")


  "get all campaigns" should "return no results " in {
    val campaignSet = collection.mutable.Set[Campaign] {
      campaign
    }
    when(mockCampaignDb.readAll()).thenReturn(campaignSet)
    get("/campaigns") {
      status should equal(200)
      logger.debug("Body:{}", body)
      val jsonAst = parse(body)
      val id = compact(render(jsonAst \ "id"))
      val name = compact(render(jsonAst \ "name"))
      id should equal("1")
      name should equal("\"DUMMY\"")
      val content = (jsonAst \ "contentSet").extract[List[Content]]
      logger.debug("Content name={}", content(0).name)
      val queryString = compact(render(jsonAst \ "queryString"))
      logger.debug("query string = {}", queryString)

    }
  }
}
