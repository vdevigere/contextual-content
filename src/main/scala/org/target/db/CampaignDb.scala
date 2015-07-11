package org.target.db

import org.infinispan.Cache
import org.target.core.Campaign

import scala.collection.JavaConversions._

/**
 * Created by Viddu on 6/13/2015.
 */
abstract class CampaignDb {
  val cache: Cache[Long, Campaign[_]]

  def create(campaign: Campaign[_]): Long = {
    cache.put(campaign.id, campaign)
    campaign.id
  }

  def update(campaign: Campaign[_]) = {
    cache.replace(campaign.id, campaign)
  }

  def delete(id: Long) = {
    cache.remove(id)
  }

  def read(id: Long): Campaign[_] = {
    cache.get(id)
  }

  def readAll(): collection.mutable.Set[Campaign[_]] = {
    val keys: collection.mutable.Set[Long] = cache.keySet
    keys.map(key => cache.get(key))
  }
}
