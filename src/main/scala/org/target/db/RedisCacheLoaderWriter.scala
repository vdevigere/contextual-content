package org.target.db

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.ByteBuffer

import org.infinispan.marshall.core.MarshalledEntry
import org.infinispan.persistence.spi.{InitializationContext, CacheLoader, CacheWriter}
import org.target.Campaign
import redis.clients.jedis.{Jedis, Protocol, JedisPoolConfig, JedisPool}

/**
 * Created by Viddu on 6/14/2015.
 */
class RedisCacheLoaderWriter[K, V] extends CacheWriter[K, V] with CacheLoader[K, V] {
  private var pool: JedisPool = null
  private var configuration: RedisStoreConfiguration = null
  private var context: InitializationContext = null

  override def write(entry: MarshalledEntry[_ <: K, _ <: V]): Unit = {
    var jedis: Jedis = null
    try {
      jedis = pool.getResource
      jedis.set(context.getMarshaller.objectToByteBuffer(entry.getKey), context.getMarshaller.objectToByteBuffer(entry.getValue))
    } finally {
      if (jedis != null) jedis.close()
    }
  }

  override def delete(key: scala.Any): Boolean = {
    var jedis: Jedis = null
    try {
      jedis = pool.getResource
      if (jedis.del(context.getMarshaller.objectToByteBuffer(key)) > 0) true else false
    } finally {
      if (jedis != null) jedis.close()
    }
  }

  override def init(ctx: InitializationContext): Unit = {
    this.context = ctx
    this.configuration = ctx.getConfiguration.asInstanceOf[RedisStoreConfiguration]
    val timeService = ctx.getTimeService
    this.pool = new JedisPool(new JedisPoolConfig(),
      configuration.host(),
      configuration.port(),
      Protocol.DEFAULT_TIMEOUT);
  }

  override def load(key: scala.Any): MarshalledEntry[K, V] = {
    var jedis: Jedis = null
    try {
      jedis = pool.getResource
      val entry = jedis.get(context.getMarshaller.objectToByteBuffer(key))
      if (entry != null) context.getMarshalledEntryFactory.newMarshalledEntry(key, context.getMarshaller.objectFromByteBuffer(entry), null).asInstanceOf[MarshalledEntry[K, V]] else null
    } finally {
      if (jedis != null) jedis.close()
    }
  }

  override def contains(key: scala.Any): Boolean = {
    var jedis: Jedis = null
    var exists = false
    try {
      jedis = pool.getResource()
      exists = jedis.exists(key.toString)
    } finally {
      if (jedis != null) jedis.close()
    }
    exists
  }

  override def stop(): Unit = {
    pool.destroy
  }

  override def start(): Unit = {
  }
}
