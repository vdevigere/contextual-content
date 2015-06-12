package org.target.condition

import org.joda.time.{DateTimeComparator, DateTime}
import org.joda.time.format.DateTimeFormat
import org.target.Predicate
import org.target.context.UserContext

/**
 * Created by Viddu on 6/11/2015.
 */
object UserTimeStamp {
  private val formatter = DateTimeFormat.forPattern("MM/dd/yyyy")

  def >(sDate: String) = new Predicate[UserContext](u => DateTimeComparator.getDateOnlyInstance().compare(u.timeStamp, DateTime.parse(sDate, formatter)) > 0)

  def <(sDate: String) = new Predicate[UserContext](u => DateTimeComparator.getDateOnlyInstance().compare(u.timeStamp, DateTime.parse(sDate, formatter)) < 0)

  def ==(sDate: String) = new Predicate[UserContext](u => DateTimeComparator.getDateOnlyInstance().compare(u.timeStamp, DateTime.parse(sDate, formatter)) == 0)
}
