package org.target.core

import java.nio.ByteBuffer
import java.util
import java.util.UUID

import com.fasterxml.jackson.annotation.{JsonCreator, JsonIgnore, JsonProperty}
import com.google.common.hash.Hashing
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.memory.MemoryIndex
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.Query
import org.target.UUIDGenarator
import org.uncommons.maths.random.XORShiftRNG

/**
 * Created by Viddu on 6/7/2015.
 */
case class Campaign @JsonCreator()(
                                    @JsonProperty("name") name: String,
                                    @JsonProperty("contentSet") contentSet: scala.collection.immutable.Set[Content],
                                    @JsonProperty("queryString") queryString: String = "*:*",
                                    @JsonProperty("id") id: Long = UUIDGenarator.generate.getMostSignificantBits) extends Serializable {
  @transient lazy val query: Query = Campaign.queryParser.parse(queryString)

  @JsonIgnore
  def this(name: String, contentSet: scala.collection.immutable.Set[Content], query: Query, id: Long) = this(name, contentSet, query.toString, id)

  def condition(index: MemoryIndex): Boolean = index.search(query) > 0.0f

  protected val treeMap = contentSet.foldLeft(new util.TreeMap[Double, Content])((treeMap, content) => {
    val total = if (treeMap.size > 0) treeMap.lastKey else 0
    treeMap.put(total + content.weight, content)
    treeMap
  })

  private def hash = {
    val hasher = Hashing.murmur3_32().newHasher().putInt(this.hashCode)
    contentSet.foreach(content => hasher.putInt(content.hashCode))
    hasher.hash()
  }

  def resolveContent(uuid: UUID): Content = {
    val bb = ByteBuffer.wrap(new Array[Byte](20))
    bb.putLong(uuid.getMostSignificantBits)
    bb.putLong(uuid.getLeastSignificantBits)
    bb.put(this.hash.asBytes())
    resolveContent(bb.array())
  }

  def resolveContent(seed: Array[Byte]): Content = {
    val value = new XORShiftRNG(seed).nextDouble() * treeMap.lastKey
    treeMap.ceilingEntry(value).getValue
  }
}

object Campaign {
  private val defaultField: String = "timeStamp"
  val queryParser = new QueryParser(defaultField, new StandardAnalyzer())
}
