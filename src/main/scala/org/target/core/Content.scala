package org.target.core

import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import org.target.UUIDGenarator

/**
 * Created by Viddu on 6/6/2015.
 */
case class Content(name: String,
                   description: String,
                   contentId: Long,
                   content: String,
                   weight: Double
                    ) {
  def this(name: String,
           description: String,
           content: String,
           weight: Double) = this(name, description, UUIDGenarator.generate.getMostSignificantBits, content, weight)

  require(weight > 0)

  override def hashCode = {
    Hashing.murmur3_32().newHasher().putString(name, Charsets.UTF_8)
      .putString(description, Charsets.UTF_8)
      .putLong(contentId)
      .putDouble(weight)
      .putInt(content.hashCode()).hash().asInt()
  }
}
