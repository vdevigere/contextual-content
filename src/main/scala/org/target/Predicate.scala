package org.target

/**
 * Created by Viddu on 6/12/2015.
 */
case class Predicate[T](val pred: T => Boolean) extends (T => Boolean) {
  override def apply(x: T): Boolean = pred(x)

  def &&(that: T => Boolean) = new Predicate[T](x => pred(x) && that(x))

  def ||(that: T => Boolean) = new Predicate[T](x => pred(x) || that(x))

  def unary_! = new Predicate[T](x => !pred(x))
}

object Predicate {
  implicit def toPredicate[A](pred: A => Boolean) = new Predicate(pred)

}
