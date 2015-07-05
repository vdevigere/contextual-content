package org.target

import java.nio.ByteBuffer
import java.util
import java.util.UUID

import com.fasterxml.uuid.Generators
import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.{MatchAllDocsQuery, Query}
import org.target.context.UserContext
import org.uncommons.maths.random.XORShiftRNG
import rl.QueryString
import sun.nio.cs.Surrogate.Generator

/**
 * Created by Viddu on 6/7/2015.
 */
case class Campaign[T <: Any](id: Long, name: String, contentSet: scala.collection.immutable.Set[Content[T]], queryString: String) {
  //  val query: Query = Campaign.queryParser.parse(queryString)
  val condition: Predicate[UserContext] = new Predicate[UserContext](x => x.memoryIndex.search(Campaign.queryParser.parse(queryString)) > 0.0f)

  def this(id: Long, name: String, contentSet: scala.collection.immutable.Set[Content[T]], query: Query) = this(id, name, contentSet, query.toString)

  def this(id: Long, name: String, contentSet: scala.collection.immutable.Set[Content[T]]) = this(id, name, contentSet, new MatchAllDocsQuery)

  def this(name: String, contentSet: scala.collection.immutable.Set[Content[T]], queryString: String) = this(UUIDGenarator.generate.getMostSignificantBits, name, contentSet, queryString)

  def this(name: String, contentSet: scala.collection.immutable.Set[Content[T]]) = this(UUIDGenarator.generate.getMostSignificantBits, name, contentSet)


  private val treeMap: util.NavigableMap[Double, Content[_]] = new util.TreeMap
  private var total = 0.00


  contentSet.foreach(content => {
    total += content.weight
    treeMap.put(total, content)
  })

  override def hashCode: Int = {
    hash.asInt()
  }

  private def hash = {
    val hasher = Hashing.murmur3_32().newHasher().putString(name, Charsets.UTF_8).putLong(id)
    contentSet.foreach(content => hasher.putInt(content.hashCode))
    hasher.hash()
  }

  def resolveContent(uuid: UUID): Content[_] = {
    val bb = ByteBuffer.wrap(new Array[Byte](20))
    bb.putLong(uuid.getMostSignificantBits)
    bb.putLong(uuid.getLeastSignificantBits)
    bb.put(this.hash.asBytes())
    resolveContent(bb.array())
  }

  def resolveContent(seed: Array[Byte]): Content[_] = {
    val value = new XORShiftRNG(seed).nextDouble() * total
    treeMap.ceilingEntry(value).getValue
  }
}

object Campaign {
  private val defaultField: String = "timeStamp"
  val queryParser = new QueryParser(defaultField, new StandardAnalyzer())
}
