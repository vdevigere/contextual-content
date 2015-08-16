package org.target.api

import javax.servlet.http.HttpServletRequest

import akka.actor.Actor
import akka.event.Logging

/**
 * Created by Viddu on 7/25/2015.
 */
trait TokenizingActor extends Actor {
  val logger = Logging(context.system, this)

  def tokenize(request: HttpServletRequest): Iterable[String]

  def fieldName: String

  override def receive = {
    case request: HttpServletRequest => {
      val field: Tuple2[String, Iterable[String]] = Tuple2(fieldName, tokenize(request))
      sender ! field
    }
  }
}
