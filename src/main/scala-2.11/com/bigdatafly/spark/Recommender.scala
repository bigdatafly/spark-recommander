package com.bigdatafly.spark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.feature.StandardScaler
import scala.math.BigDecimal
import org.apache.spark.mllib.feature.MultipleLabeledVector
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.mllib.recommendation.ALS

object Recommender {
  
  def transform(x : Double):Double = {
     (x+10)/5+1
  }
  def main(args:Array[String]) = {
    val conf = new SparkConf().setMaster("local[4]").setAppName("OrderRFM")
    val sc = new SparkContext(conf)
    val rawData = sc.textFile("F:\\data\\mahout\\evaluation_data.txt", 4).cache()
    val data = rawData.map ( line => {
         val arr = line.split('\t')
         new MultipleLabeledVector(Array(arr(0).toInt,arr(1).toInt),Vectors.dense(transform(arr(2).toDouble)))
        }   
     ).cache()
     
     val norm = new StandardScaler(withMean = true,withStd=false).fit(data.map { x => x.features })
     val ratings = data.map(x=>
       new Rating(x.label(0),x.label(1),norm.transform(x.features).toArray(0)  
     ))
   
    val rank = 1000
    val numIterations = 10 //10-20
    val model = ALS.train(ratings, rank, numIterations, 0.01)
    
    val usersProducts = ratings.map { case Rating(user, product, rate) =>
      (user, product)
    }
    val predictions =
      model.predict(usersProducts).map { case Rating(user, product, rate) =>
        ((user, product), rate)
      }
    val ratesAndPreds = ratings.map { case Rating(user, product, rate) =>
      ((user, product), rate)
    }.join(predictions)
    val MSE = ratesAndPreds.map { case ((user, product), (r1, r2)) =>
      val err = (r1 - r2)
      err * err
    }.mean()
    println("Mean Squared Error = " + MSE)
    
     sc.stop()
  }
}