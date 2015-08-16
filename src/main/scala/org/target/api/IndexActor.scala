package org.target.api

import java.util
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import javax.servlet.http.HttpServletRequest

import akka.actor.{Actor, Props}
import akka.dispatch.Futures
import akka.event.Logging
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import org.apache.lucene.index.memory.MemoryIndex
import org.target.tokenizer.{QueryParamTokenizer, TimeStampTokenizer}

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by Viddu on 7/24/2015.
 */
class IndexActor extends Actor {
  val log = Logging(context.system, this)
  implicit val timeout = Timeout(5, TimeUnit.SECONDS) // needed for `?` below


  val consumer: Consumer[_ >: (String, util.Collection[String])] = new Consumer[(String, util.Collection[String])] {
    override def accept(t: (String, util.Collection[String])): Unit = {

    }
  }

  override def receive = {
    case request: HttpServletRequest => {
      implicit val ec: ExecutionContext = context.dispatcher
      log.debug("Request received >> {}", request.getPathInfo)
      //TODO: Broadcast to all actors responsible for generating tokens from different parts of the request.
      //Tokenize the timestamp
      val timeStampTokens = (context.actorOf(TimeStampTokenizer.props) ? request).mapTo[Tuple2[String, Iterable[String]]]
      //Tokenize the "qp" query parameter
      val queryParamTokens = (context.actorOf(QueryParamTokenizer.props("qp")) ? request).mapTo[Tuple2[String, Iterable[String]]]

      val memIndexFuture: Future[MemoryIndex] = Futures.sequence(List(timeStampTokens, queryParamTokens), context.dispatcher).map(_.foldLeft(new MemoryIndex())((memoryIndex, field) => {
        memoryIndex.addField(field._1, memoryIndex.keywordTokenStream(field._2))
        memoryIndex
      }))
      memIndexFuture pipeTo sender
    }
    case _ => log.debug("Message received is not of type Request")
  }
}

object IndexActor {
  def props = Props(new IndexActor)
}
