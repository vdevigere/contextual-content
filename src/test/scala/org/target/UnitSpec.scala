package org.target

import com.typesafe.scalalogging.LazyLogging
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

/**
 * Created by Viddu on 7/8/2015.
 */
@RunWith(classOf[JUnitRunner])
abstract class UnitSpec extends FlatSpec with Matchers with OptionValues with Inside with Inspectors with LazyLogging
