package org.target.db

import org.infinispan.commons.configuration.{ConfigurationFor, BuiltBy}
import org.infinispan.commons.configuration.attributes.{AttributeDefinition, AttributeSet}
import org.infinispan.configuration.cache.{AsyncStoreConfiguration, SingletonStoreConfiguration, AbstractStoreConfiguration}
import org.infinispan.commons.configuration.attributes.Attribute

/**
 * Created by Viddu on 6/15/2015.
 */
@BuiltBy(classOf[RedisStoreConfigurationBuilder])
@ConfigurationFor(classOf[RedisCacheLoaderWriter[_, _]])
class RedisStoreConfiguration(attributes: AttributeSet, async: AsyncStoreConfiguration, singletonStore: SingletonStoreConfiguration) extends AbstractStoreConfiguration(attributes: AttributeSet, async: AsyncStoreConfiguration, singletonStore: SingletonStoreConfiguration) {

  def url(): String = attributes.attribute(RedisStoreConfiguration.URL).get()

  def host(): String = attributes.attribute(RedisStoreConfiguration.HOST).get()

  def port(): Integer = attributes.attribute(RedisStoreConfiguration.PORT).get().toInt
}

object RedisStoreConfiguration {
  val URL: AttributeDefinition[String] = AttributeDefinition.builder("url", "Infinispan-RedisStore").immutable.build
  val HOST: AttributeDefinition[String] = AttributeDefinition.builder("host", "Infinispan-RedisStore").immutable.build
  val PORT: AttributeDefinition[String] = AttributeDefinition.builder("port", "Infinispan-RedisStore").immutable.build

  def attributeDefinitionSet: AttributeSet = {
    return new AttributeSet(classOf[RedisStoreConfiguration], AbstractStoreConfiguration.attributeDefinitionSet, URL, HOST, PORT)
  }
}