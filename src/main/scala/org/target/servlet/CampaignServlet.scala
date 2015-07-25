package org.target.servlet

import java.util.UUID
import javax.servlet.http.HttpServletRequest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.uuid.Generators
import com.google.inject.Inject
import org.joda.time.DateTime
import org.scalatra._
import org.slf4j.LoggerFactory
import org.target.context.UserContext
import org.target.core.{Campaign, Content}
import org.target.db.CampaignDb

class CampaignServlet @Inject()(campaignDb: CampaignDb) extends ScalatraServlet {

  import org.target.servlet.CampaignServlet._

  before() {
    contentType = "application/json"
  }

  override protected def renderPipeline: RenderPipeline = renderToJson orElse super.renderPipeline

  def renderToJson: RenderPipeline = {
    case a: Any => mapper.writeValue(response.getWriter, a)
  }

  val logger = LoggerFactory.getLogger(classOf[CampaignServlet])

  /**
   * Get all campaigns
   */
  get("/campaigns") {
    campaignDb.readAll()
  }

  /**
   * Create a new campaign
   */
  post("/campaigns") {
    val campaignName = params("campaignName")
    val names = multiParams("name")
    val descriptions = multiParams("description")
    val contents = multiParams("content")
    val weights = multiParams("weight")
    val queryString = params.getOrElse("queryString", "*:*")
    logger.debug("Campaign Name: {}", campaignName)
    logger.debug("Content Name: {}", names)
    logger.debug("Content Desc: {}", descriptions)
    logger.debug("Weight: {}", weights)

    val contentList = for (index <- 0 to names.length - 1) yield new Content(names(index), descriptions(index), contents(index), weights(index).toDouble)
    val campaign = new Campaign(campaignName, contentList.toSet, queryString)
    campaignDb.create(campaign)
    campaign.id
  }

  /**
   * Get a campaign by Id
   */
  get("/campaigns/:id") {
    campaignDb.read(params("id").toLong)
  }

  /**
   * Update an existing campaign by Id
   */
  put("/campaigns/:id") {
    val campaignName = params("campaignName")
    val queryString = params("queryString")
    val names = multiParams("name")
    val descriptions = multiParams("description")
    val contents = multiParams("content")
    val weights = multiParams("weight")
    logger.debug("Campaign Name: {}", campaignName)
    logger.debug("Content Name: {}", names)
    logger.debug("Content Desc: {}", descriptions)
    logger.debug("Weight: {}", weights)
    logger.debug("QueryString: {}", queryString)

    val contentList = for (index <- 0 to names.length - 1) yield new Content(names(index), descriptions(index), contents(index), weights(index).toDouble)
    val campaign = new Campaign(campaignName, contentList.toSet, queryString, params("id").toLong)
    campaignDb.update(campaign)
    campaign.id
  }

  /**
   * Delete a campaign by Id
   */
  delete("/campaigns/:id") {
    campaignDb.delete(params("id").toLong)
  }

  def resetSeedCookie(uuid: UUID) = {
    response.addCookie(new Cookie("SEED", uuid.toString))
  }

  def getSeedCookie(request: HttpServletRequest): UUID = {
    if (request.cookies.contains("SEED"))
      UUID.fromString(request.cookies("SEED"))
    else
      Generators.randomBasedGenerator().generate()
  }

  /**
   * Weighted random selection of a content for a given campaign Id
   */
  get("/campaigns/:id/content/random") {
    val seedUUID = getSeedCookie(request)
    resetSeedCookie(seedUUID)
    campaignDb.read(params("id").toLong).resolveContent(seedUUID)
  }

  /**
   * Weighted random selection of content(s) for all Campaigns
   */
  get("/campaigns/content/random") {
    val seedUUID = getSeedCookie(request)
    resetSeedCookie(seedUUID)
    val userContext = new UserContext(new DateTime)
    campaignDb.readAll().filter(_.condition(userContext.memoryIndex)).map(_.resolveContent(seedUUID))
  }
}

object CampaignServlet {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
}
