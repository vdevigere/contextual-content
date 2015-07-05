package org.target.context

import java.util

import org.apache.lucene.document.DateTools
import org.apache.lucene.index.memory.MemoryIndex
import org.joda.time.DateTime

/**
 * Created by Viddu on 6/7/2015.
 */
case class UserContext(timeStamp: DateTime) {
  val memoryIndex = new MemoryIndex
  val sTimeStamp = DateTools.dateToString(timeStamp.toDate, DateTools.Resolution.DAY)
  memoryIndex.addField("timeStamp", memoryIndex.keywordTokenStream(util.Arrays.asList(sTimeStamp)))
}
