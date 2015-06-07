package org.target

class IncorrectWeightException(weight: Double) extends Exception("Weight [" + weight + "] is incorrect") {
}
