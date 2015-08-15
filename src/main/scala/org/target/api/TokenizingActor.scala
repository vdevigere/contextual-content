package org.target.api

import javax.servlet.http.HttpServletRequest

import akka.actor.Actor

/**
 * Created by Viddu on 7/25/2015.
 */
trait TokenizingActor extends Actor {

  def tokenize(request: HttpServletRequest): Iterable[String]

  val fieldName: String

  override def receive = {
    case request: HttpServletRequest => {
      val field: Tuple2[String, Iterable[String]] = Tuple2(fieldName, tokenize(request))
      sender ! field
    }
  }
}
