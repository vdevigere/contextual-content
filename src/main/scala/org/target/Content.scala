package org.target

import com.google.common.base.Charsets
import com.google.common.hash.HashFunction
import com.google.common.hash.Hasher
import com.google.common.hash.Hashing

/**
 * Created by Viddu on 6/6/2015.
 */
case class Content[T](name: String,
                      description: String,
                      contentId: Long,
                      content: T,
                      weight: Double
                       ) {
  if (weight <= 0)
    throw new IncorrectWeightException(weight);

  override def hashCode = {
    Hashing.murmur3_32().newHasher().putString(name, Charsets.UTF_8)
      .putString(description, Charsets.UTF_8)
      .putLong(contentId)
      .putDouble(weight)
      .putInt(content.hashCode()).hash().asInt();
  }
}
