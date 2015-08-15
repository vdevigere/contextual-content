package org.target.db

import org.infinispan.Cache
import org.target.api.Campaign

import scala.collection.JavaConversions._

/**
 * Created by Viddu on 6/13/2015.
 */
abstract class CampaignDb {
  val cache: Cache[Long, Campaign]

  def create(campaign: Campaign): Long = {
    cache.put(campaign.id, campaign)
    campaign.id
  }

  def update(campaign: Campaign) = {
    cache.replace(campaign.id, campaign)
  }

  def delete(id: Long) = {
    cache.remove(id)
  }

  def read(id: Long): Campaign = {
    cache.get(id)
  }

  def readAll(): collection.mutable.Set[Campaign] = {
    val keys: collection.mutable.Set[Long] = cache.keySet
    keys.map(key => cache.get(key))
  }
}
