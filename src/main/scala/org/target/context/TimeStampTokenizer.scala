package org.target.context

import javax.servlet.http.HttpServletRequest

import akka.actor.Props
import org.apache.lucene.document.DateTools
import org.joda.time.DateTime
import org.target.api.TokenizingActor

/**
 * Created by Viddu on 7/25/2015.
 */
class TimeStampTokenizer extends TokenizingActor {
  override def tokenize(request: HttpServletRequest): Iterable[String] = {
    val dateTime = new DateTime
    List(DateTools.dateToString(dateTime.toDate, DateTools.Resolution.DAY))

  }

  override val fieldName: String = "timeStamp"
}

object TimeStampTokenizer {
  def props = Props(new TimeStampTokenizer)
}
