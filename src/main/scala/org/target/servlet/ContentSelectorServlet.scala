package org.target.servlet

import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.fasterxml.uuid.Generators
import com.google.inject.Inject
import org.apache.lucene.index.memory.MemoryIndex
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{AsyncResult, Cookie, FutureSupport, ScalatraServlet}
import org.target.api.IndexActor
import org.target.db.CampaignDb

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by Viddu on 8/9/2015.
 */
class ContentSelectorServlet @Inject()(campaignDb: CampaignDb) extends ScalatraServlet with FutureSupport with JacksonJsonSupport {

  import org.target.servlet.ContentSelectorServlet._

  override protected implicit def executor: ExecutionContext = actorSystem.dispatcher

  before() {
    contentType = "application/json"
  }

  def resetSeedCookie(uuid: UUID) = {
    response.addCookie(new Cookie("SEED", uuid.toString))
  }

  def getSeedCookie(request: HttpServletRequest): UUID = {
    if (request.cookies.contains("SEED"))
      UUID.fromString(request.cookies("SEED"))
    else
      Generators.randomBasedGenerator().generate()
  }

  /**
   * Weighted random selection of a content for a given campaign Id
   */
  get("/:id/random") {
    val seedUUID = getSeedCookie(request)
    resetSeedCookie(seedUUID)
    campaignDb.read(params("id").toLong).resolveContent(seedUUID)
  }

  /**
   * Weighted random selection of content(s) for all Campaigns
   */
  get("/random") {
    new AsyncResult() {
      override val is: Future[_] = {
        implicit val timeout = Timeout(5, TimeUnit.SECONDS)
        val indexedFuture = (indexer ? request).mapTo[MemoryIndex]
        val seedUUID = getSeedCookie(request)
        resetSeedCookie(seedUUID)
        indexedFuture.map(memoryIndex => campaignDb.readAll().filter(_.condition(memoryIndex)).map(_.resolveContent(seedUUID)))
      }
    }
  }

  override protected implicit def jsonFormats: Formats = DefaultFormats
}

object ContentSelectorServlet {
  val actorSystem = ActorSystem("contextual-content")
  val indexer = actorSystem.actorOf(IndexActor.props)
}