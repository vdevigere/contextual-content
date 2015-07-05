package org.target.servlet

import org.scalatra.ScalatraServlet
import org.slf4j.LoggerFactory
import org.target.{Campaign, Content}
import org.target.db.CampaignDb

class CampaignServlet extends ScalatraServlet {
  val logger = LoggerFactory.getLogger(classOf[CampaignServlet])

  get("/campaigns") {
    CampaignDb.readAll()
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
    CampaignDb.create(campaign)
    campaign.id
  }

  get("/campaigns/:id") {
    CampaignDb.read(params("id").toLong)
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
    CampaignDb.update(campaign)
    campaign.id
  }

  delete("/campaigns/:id") {
    CampaignDb.delete(params("id").toLong)
  }
}
