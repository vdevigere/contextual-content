package org.target.context

import javax.servlet.http.HttpServletRequest

import akka.actor.Props
import org.target.core.TokenizingActor

/**
 * Created by Viddu on 7/25/2015.
 */
class QueryParamTokenizer(queryParam: String) extends TokenizingActor {
  override def tokenize(request: HttpServletRequest): Iterable[String] = {
    List(request.getParameter(queryParam))
  }

  override val fieldName: String = queryParam
}

object QueryParamTokenizer {
  def props(queryParam: String) = Props(new QueryParamTokenizer(queryParam))
}
