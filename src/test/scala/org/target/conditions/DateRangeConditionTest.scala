package org.target.conditions

import org.assertj.core.api.Assertions._
import org.joda.time.DateTime
import org.junit.Test
import org.target.conditions.DateRangeCondition$._
import org.target.context.UserContext
import org.target.conditions.DateRangeCondition._

class DateRangeConditionTest {
  @Test def testIsAfterOrEqualWithEqualCondition {
    val now: DateTime = new DateTime
    val userContext: UserContext = new UserContext(now)
    assertThat(userContext).matches(isAfterOrEqual(now))
  }

  @Test def testIsAfterOrEqualWithAfterCondition {
    val now: DateTime = new DateTime
    val userContext: UserContext = new UserContext(now.plusDays(1))
    assertThat(userContext).matches(isAfterOrEqual(now))
  }

  @Test def testIsAfterOrEqualWithBeforeCondition {
    val now: DateTime = new DateTime
    val userContext: UserContext = new UserContext(now.minusDays(1))
    assertThat(userContext).matches(isAfterOrEqual(now).negate)
  }

  @Test def testIsBeforeWithBeforeCondition {
    val now: DateTime = new DateTime
    val userContext: UserContext = new UserContext(now.minusDays(1))
    assertThat(userContext).matches(isBefore(now))
  }

  @Test def testIsBeforeWithAfterCondition {
    val now: DateTime = new DateTime
    val userContext: UserContext = new UserContext(now.plusDays(1))
    assertThat(userContext).matches(isBefore(now).negate)
  }

  @Test def testIsBeforeWithEqualCondition {
    val now: DateTime = new DateTime
    val userContext: UserContext = new UserContext(now)
    assertThat(userContext).matches(isBefore(now).negate)
  }

  @Test def testTimeStampBetweenWithStartDate {
    val startDate: DateTime = new DateTime
    val endDate: DateTime = startDate.plusWeeks(1)
    val userContext: UserContext = new UserContext(startDate)
    assertThat(userContext).matches(isBetween(startDate, endDate))
  }

  @Test def testTimeStampBetweenWithBeforeStartDate {
    val startDate: DateTime = new DateTime
    val endDate: DateTime = startDate.plusWeeks(1)
    val dateUnderTest: DateTime = startDate.minusWeeks(1)
    val userContext: UserContext = new UserContext(dateUnderTest)
    assertThat(userContext).matches(isBetween(startDate, endDate).negate)
  }
}
