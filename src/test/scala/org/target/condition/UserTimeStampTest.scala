package org.target.condition

import org.joda.time.DateTime
import org.junit.Test
import org.assertj.core.api.Assertions._
import org.target.context.UserContext

/**
 * Created by Viddu on 6/12/2015.
 */
class UserTimeStampTest {
  val now = new DateTime()
  val user = new UserContext(now)

  @Test
  def testBeforeDate(): Unit = {
    val beforeDate = now.minusDays(1)
    val sDate = beforeDate.toString("MM/dd/yyyy")
    val eval = (UserTimeStamp > sDate)(user)
    assertThat(eval).isTrue()
  }

  @Test
  def testAfterDate(): Unit = {
    val beforeDate = now.plusDays(1)
    val sDate = beforeDate.toString("MM/dd/yyyy")
    val eval = (UserTimeStamp < sDate)(user)
    assertThat(eval).isTrue()
  }

  @Test
  def testEqualDate(): Unit = {
    val sDate = now.toString("MM/dd/yyyy")
    val eval = (UserTimeStamp == sDate)(user)
    assertThat(eval).isTrue()
  }
}
