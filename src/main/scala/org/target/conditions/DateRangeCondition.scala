package org.target.conditions

import java.util.function.Predicate

import org.joda.time.DateTime
import org.target.context.UserContext

/**
 * Created by Viddu on 6/7/2015.
 */
object DateRangeCondition {
  def isAfterOrEqual(startDate: DateTime): Predicate[UserContext] = {
    new Predicate[UserContext] {
      override def test(userContext: UserContext): Boolean = {
        userContext.timeStamp.isAfter(startDate) || userContext.timeStamp.isEqual(startDate)
      }
    }
  }

  def isBefore(endDate: DateTime): Predicate[UserContext] = {
    new Predicate[UserContext] {
      override def test(t: UserContext): Boolean = {
        t.timeStamp.isBefore(endDate)
      }
    }
  }

  def isBetween(startDate: DateTime, endDate: DateTime): Predicate[UserContext] = {
    isAfterOrEqual(startDate).and(isBefore(endDate))
  }
}
