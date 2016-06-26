package org.apache.spark.mllib.feature

import org.apache.spark.annotation.Since
import scala.beans.BeanInfo
import org.apache.spark.mllib.linalg.{Vector, Vectors}

@Since("1.6.1")
@BeanInfo
case class MultipleLabeledVector @Since("1.6.1") (
  @Since("1.6.1") label : Array[Int],
  @Since("1.0.0") features: Vector){
  
  override def toString: String = {
    val mullabel = label.mkString(",")
    s"($mullabel,$features)"
  }
}