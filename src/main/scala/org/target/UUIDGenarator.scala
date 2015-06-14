package org.target

import java.util.UUID

import com.fasterxml.uuid.Generators

/**
 * Created by Viddu on 6/14/2015.
 */
object UUIDGenarator {
  private val generator = Generators.timeBasedGenerator()

  def generate: UUID = generator.generate()
}
