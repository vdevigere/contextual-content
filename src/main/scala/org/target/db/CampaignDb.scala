package org.target.db

import org.infinispan.Cache
import org.infinispan.configuration.cache.ConfigurationBuilder
import org.infinispan.eviction.EvictionStrategy._
import org.infinispan.manager.{DefaultCacheManager, EmbeddedCacheManager}
import org.target.Campaign
import collection.JavaConversions._

/**
 * Created by Viddu on 6/13/2015.
 */
object CampaignDb {
  val manager = new DefaultCacheManager()
  manager.defineConfiguration("campaign-cache", new ConfigurationBuilder().build())
  private val cache = manager.getCache[Long, Campaign]("campaign-cache")

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
