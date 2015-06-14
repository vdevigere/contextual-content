package org.target.db

import com.fasterxml.uuid.Generators
import org.infinispan.Cache
import org.infinispan.manager.{DefaultCacheManager, EmbeddedCacheManager}
import org.target.Campaign

/**
 * Created by Viddu on 6/13/2015.
 */
object CampaignDb {
  private val cacheManager: EmbeddedCacheManager = new DefaultCacheManager("my-config-file.xml")
  private val cache: Cache[Long, Campaign] = cacheManager.getCache()

  def create(campaign: Campaign): Long = {
    val id = Generators.timeBasedGenerator().generate().getMostSignificantBits
    id
  }

  def update(campaign: Campaign) = {

  }

  def delete(id: Long): Boolean = {
    false
  }

  def read(id: Long): Campaign ={
    null
  }

  def readAll(): List[Campaign]={
    null
  }
}
