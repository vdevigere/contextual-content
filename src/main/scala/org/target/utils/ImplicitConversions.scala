package org.target.utils

import javax.servlet.Servlet

import io.undertow.servlet.api.{InstanceFactory, InstanceHandle}

/**
 * Created by Viddu on 7/11/2015.
 */
object ImplicitConversions {
  implicit def func2InstanceFactory[T <: Servlet](f: () => T): InstanceFactory[T] = {
    new InstanceFactory[T] {
      override def createInstance(): InstanceHandle[T] = {
        new InstanceHandle[T] {
          override def getInstance(): T = {
            f()
          }

          override def release(): Unit = {}
        }
      }
    }
  }
}
