package org.target

import java.nio.ByteBuffer
import java.util
import java.util.UUID

import com.fasterxml.uuid.Generators
import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import org.target.context.UserContext
import org.uncommons.maths.random.XORShiftRNG
import sun.nio.cs.Surrogate.Generator

/**
 * Created by Viddu on 6/7/2015.
 */
case class Campaign(id: Long, name: String, contentSet: scala.collection.immutable.Set[Content[_]]) {

  def this(name: String, contentSet: scala.collection.immutable.Set[Content[_]]) = this(UUIDGenarator.generate.getMostSignificantBits, name, contentSet)

  private val treeMap: util.NavigableMap[Double, Content[_]] = new util.TreeMap
  private var total = 0.00

  var condition: Predicate[UserContext] = new Predicate[UserContext](x => true)

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
