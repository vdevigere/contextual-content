package org.target.db

import java.net.URI

import com.viddu.infinispan.redis.configuration.RedisStoreConfigurationBuilder
import org.infinispan.configuration.cache.ConfigurationBuilder
import org.infinispan.manager.DefaultCacheManager
import org.target.Campaign

import scala.collection.JavaConversions._

/**
 * Created by Viddu on 6/13/2015.
 */
object CampaignDb {
  val manager = new DefaultCacheManager()
  val configuration = new ConfigurationBuilder().persistence().passivation(false).addStore(classOf[RedisStoreConfigurationBuilder])
    .url(new URI("redis://localhost:6379/0")).build()
  manager.defineConfiguration("campaign-cache", configuration)
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
