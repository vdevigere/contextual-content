package org.target.condition

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.target.Predicate
import org.target.context.UserContext

/**
 * Created by Viddu on 6/11/2015.
 */
object UserTimeStamp {
  val formatter = DateTimeFormat.forPattern("MM/dd/yyyy")

  def between(sDate: String, eDate: String): Predicate[UserContext] =
    new Predicate[UserContext](u => u.timeStamp.isAfter(DateTime.parse(sDate, formatter)) && u.timeStamp.isBefore(DateTime.parse(eDate, formatter)))


  def >(sDate: String) = new Predicate[UserContext](u => u.timeStamp.isAfter(DateTime.parse(sDate, formatter)))

  def <(sDate: String) = new Predicate[UserContext](u => u.timeStamp.isBefore(DateTime.parse(sDate, formatter)))

  def ==(sDate: String) = new Predicate[UserContext](u => u.timeStamp.isEqual(DateTime.parse(sDate, formatter)))
}
