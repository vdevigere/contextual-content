package org.target

/**
 * Created by Viddu on 7/10/2015.
 */
trait ContentFixture {
  val contentA = new Content("A", "A Content", 0L, "Banner A", 75.0)
  val contentB = new Content("B", "B Content", 0L, "Banner B", 25.0)

  val campaign = new Campaign(1L, "DUMMY", Array(contentA, contentB).toSet)

}
