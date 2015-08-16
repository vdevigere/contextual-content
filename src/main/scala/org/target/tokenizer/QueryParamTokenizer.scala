package org.target.tokenizer

import javax.servlet.http.HttpServletRequest

import akka.actor.Props
import org.target.api.TokenizingActor

/**
 * Created by Viddu on 7/25/2015.
 */
class QueryParamTokenizer(queryParam: String) extends TokenizingActor {
  override def tokenize(request: HttpServletRequest): Iterable[String] = {
    val queryParamValue = request.getParameter(queryParam)
    if (queryParamValue != null) List(queryParamValue) else List.empty
  }

  override def fieldName: String = queryParam
}

object QueryParamTokenizer {
  def props(queryParam: String) = Props(new QueryParamTokenizer(queryParam))
}
