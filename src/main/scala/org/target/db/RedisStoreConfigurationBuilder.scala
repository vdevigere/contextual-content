package org.target.db

import java.net.URL

import org.infinispan.commons.configuration.attributes.{AttributeDefinition, AttributeSet}
import org.infinispan.configuration.cache.{PersistenceConfigurationBuilder, AbstractStoreConfigurationBuilder}

/**
 * Created by Viddu on 6/15/2015.
 */
class RedisStoreConfigurationBuilder(builder: PersistenceConfigurationBuilder) extends AbstractStoreConfigurationBuilder[RedisStoreConfiguration, RedisStoreConfigurationBuilder](builder: PersistenceConfigurationBuilder, RedisStoreConfiguration.attributeDefinitionSet) {

  override def create(): RedisStoreConfiguration = new RedisStoreConfiguration(attributes.protect, async.create, singletonStore.create)

  override def self(): RedisStoreConfigurationBuilder = this

  def url(url: String): RedisStoreConfigurationBuilder = {
    attributes.attribute(RedisStoreConfiguration.URL).set(url)
    self()
  }

  def host(host: String): RedisStoreConfigurationBuilder = {
    attributes.attribute(RedisStoreConfiguration.HOST).set(host)
    self()
  }

  def port(port: Integer): RedisStoreConfigurationBuilder = {
    attributes.attribute(RedisStoreConfiguration.PORT).set(port.toString)
    self()
  }
}
