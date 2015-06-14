package org.target

import org.junit.Test
import org.assertj.core.api.Assertions._

/**
 * Created by Viddu on 6/14/2015.
 */
class PredicateTest {
  val identity: Predicate[Boolean] = new Predicate[Boolean](t => t)
  val invert: Predicate[Boolean] = new Predicate[Boolean](t => !t)

  @Test
  def testAnd() {
    assertThat(identity(true) && identity(false)).isFalse
    assertThat(identity(true) && invert(true)).isFalse
  }

  def testOr: Unit = {
    assertThat(identity(true) || identity(false)).isTrue
    assertThat(identity(true) || invert(true)).isTrue
  }

  def testNegation: Unit = {
    assertThat(!identity(true)).isFalse
    assertThat(!invert(true)).isTrue
  }
}
