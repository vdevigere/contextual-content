package org.target.servlet

import java.util.UUID
import javax.servlet.http.HttpServletRequest

import com.fasterxml.uuid.Generators
import org.joda.time.DateTime
import org.scalatra.{Cookie, ScalatraServlet}
import org.slf4j.LoggerFactory
import org.target.context.UserContext
import org.target.db.CampaignDb
import org.target.{Campaign, Content}

class CampaignServlet(campaignDb: CampaignDb) extends ScalatraServlet {
  val logger = LoggerFactory.getLogger(classOf[CampaignServlet])

  get("/campaigns") {
    campaignDb.readAll()
  }

  post("/campaigns") {
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
    logger.debug("Condition:{}", queryString)

    val contentList = for (index <- 0 to names.length - 1) yield new Content[String](names(index), descriptions(index), contents(index), weights(index).toDouble)
    val campaign = new Campaign(campaignName, contentList.toSet, queryString)
    campaignDb.create(campaign)
    campaign.id
  }

  get("/campaigns/:id") {
    campaignDb.read(params("id").toLong)
  }

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

    val contentList = for (index <- 0 to names.length - 1) yield new Content[String](names(index), descriptions(index), contents(index), weights(index).toDouble)
    val campaign = new Campaign(params("id").toLong, campaignName, contentList.toSet, queryString)
    campaignDb.update(campaign)
    campaign.id
  }

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

  get("/campaigns/:id/content/random") {
    val seedUUID = getSeedCookie(request)
    resetSeedCookie(seedUUID)
    campaignDb.read(params("id").toLong).resolveContent(seedUUID)
  }

  get("/campaigns/content/random") {
    val seedUUID = getSeedCookie(request)
    resetSeedCookie(seedUUID)
    val userContext = new UserContext(new DateTime)
    campaignDb.readAll().filter(_.condition(userContext)).map(_.resolveContent(seedUUID))
  }
}
