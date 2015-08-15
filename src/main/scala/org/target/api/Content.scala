package org.target.api

import org.target.utils.UUIDGenarator


/**
 * Created by Viddu on 6/6/2015.
 */
case class Content(name: String,
                   description: String,
                   contentId: Long,
                   content: Seq[Byte],
                   weight: Double
                    ) {
  def this(name: String,
           description: String,
           content: Seq[Byte],
           weight: Double) = this(name, description, UUIDGenarator.generate.getMostSignificantBits, content, weight)

  require(weight > 0)
}
