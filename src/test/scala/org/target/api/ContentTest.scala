package org.target.api

import org.target.UnitSpec

/**
 * Created by Viddu on 6/7/2015.
 */
class ContentTest extends UnitSpec {
  "Creating content with weight less than 0" should "throw an IllegalArgumentException" in {
    intercept[IllegalArgumentException] {
      new Content("A", "A Content", 0L, "Banner A".getBytes, 0.0)
    }
  }

  "Content with same values" should "have the same hash and thus be equal." in {
    val A: Content = new Content("A", "A Content", 0L, "Banner A".getBytes, 90.0)
    val B: Content = new Content("A", "A Content", 0L, "Banner A".getBytes, 90.0)
    A should equal(B)
  }
}
