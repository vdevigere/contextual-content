package org.target.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.lucene.search.MatchAllDocsQuery

/**
 * Created by Viddu on 7/10/2015.
 */
trait ContentFixture {
  val contentA = new Content("A", "A Content", 0L, "Banner A".getBytes, 75.0)
  val contentB = new Content("B", "B Content", 0L, "Banner B".getBytes, 25.0)

  val campaign = new Campaign("DUMMY", Array(contentA, contentB).toSet, new MatchAllDocsQuery, 1L)
  implicit val mapper = new ObjectMapper().registerModule(DefaultScalaModule)

}
