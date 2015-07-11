package org.target.db

import java.net.URI

import com.viddu.infinispan.redis.configuration.RedisStoreConfigurationBuilder
import org.infinispan.configuration.cache.ConfigurationBuilder
import org.infinispan.manager.DefaultCacheManager
import org.target.Campaign

/**
 * Created by Viddu on 7/10/2015.
 */
class RedisCampaignDb extends CampaignDb{
  private val manager = new DefaultCacheManager()
  manager.defineConfiguration("campaign-cache", new ConfigurationBuilder().persistence().passivation(false).addStore(classOf[RedisStoreConfigurationBuilder])
    .url(new URI("redis://localhost:6379/0")).build())
  val cache = manager.getCache[Long, Campaign[_]]("campaign-cache")
}
