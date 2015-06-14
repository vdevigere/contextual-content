package org.target.db

import org.infinispan.Cache
import org.infinispan.manager.{DefaultCacheManager, EmbeddedCacheManager}
import org.target.Campaign
import collection.JavaConversions._

/**
 * Created by Viddu on 6/13/2015.
 */
object CampaignDb {
  private val cacheManager: EmbeddedCacheManager = new DefaultCacheManager("my-config-file.xml")
  private val cache: Cache[Long, Campaign] = cacheManager.getCache()

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
    val keys: scala.collection.mutable.Set[Long] = cache.keySet
    keys.map(key => cache.get(key))
  }
}
