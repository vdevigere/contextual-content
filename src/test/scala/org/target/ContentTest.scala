package org.target

import org.hamcrest.CoreMatchers._
import org.hamcrest.MatcherAssert._
import org.junit.Test

/**
 * Created by Viddu on 6/7/2015.
 */
class ContentTest {
  @Test(expected = classOf[IllegalArgumentException])
  def testContentCreationWithIncorrectWeight(): Unit = {
    new Content[String]("A", "A Content", 0L, "Banner A", 0.0)
  }

  def testContentEquality {
    val A: Content[String] = new Content[String]("A", "A Content", 0L, "Banner A", 0.0)
    val B: Content[String] = new Content[String]("A", "A Content", 0L, "Banner A", 0.0)
    assertThat(A, equalTo(B))
  }
}
