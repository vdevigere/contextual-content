package org.target.servlet

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json.JacksonJsonSupport
import org.target.api.{Campaign, Content}
import org.target.db.CampaignDb

class CampaignServlet @Inject()(campaignDb: CampaignDb) extends ScalatraServlet with JacksonJsonSupport with LazyLogging{

  before() {
    contentType = formats("json")
  }

  /**
   * Get a campaign by Id
   */
  get("/:id") {
    campaignDb.read(params("id").toLong)
  }

  /**
   * Update an existing campaign by Id
   */
  put("/:id") {
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

    val contentList = for (index <- 0 to names.length - 1) yield new Content(names(index), descriptions(index), contents(index).getBytes, weights(index).toDouble)
    val campaign = new Campaign(campaignName, contentList.toSet, queryString, params("id").toLong)
    campaignDb.update(campaign)
    campaign.id
  }

  /**
   * Delete a campaign by Id
   */
  delete("/:id") {
    campaignDb.delete(params("id").toLong)
  }

  /**
   * Get all campaigns
   */
  get("/") {
    campaignDb.readAll()
  }

  /**
   * Create a new campaign
   */
  post("/") {
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

    val contentList = for (index <- 0 to names.length - 1) yield new Content(names(index), descriptions(index), contents(index).getBytes, weights(index).toDouble)
    val campaign = new Campaign(campaignName, contentList.toSet, queryString)
    campaignDb.create(campaign)
    campaign.id
  }
  override protected implicit def jsonFormats: Formats = DefaultFormats
}
