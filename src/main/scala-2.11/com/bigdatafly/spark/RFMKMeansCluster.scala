package com.bigdatafly.spark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.{Vector,Vectors}
import org.apache.spark.mllib.regression.LabeledPoint

object RFMKMeansCluster {
  val file_path = "F:\\data\\mahout\\order_norm_data.txt"
  val result_path = "F:\\data\\mahout\\kmeans";
  def main(args : Array[String]):Unit = {
    val conf = new SparkConf().setMaster("local[4]").setAppName("")
    val sc = new SparkContext(conf)
    val raw_data =sc.textFile(file_path, 4).map(line=>{
        val values = line.split("\t")
        new LabeledPoint(values(0).toInt,Vectors.dense(values.drop(1).map(_.toDouble)))
      }
    ).cache();
    
    val data = raw_data.map { x => x.features }
    //data.foreach(print)
    
    val numClusters = 15
    val numIterations = 20
    val runs = 10
    val clusters = KMeans.train(data,numClusters, numIterations,runs)
    
    println("aggr of cluser: ")
    raw_data.map(v => (clusters.predict(v.features),1)).reduceByKey(_+_).collect().foreach(print);
    
    val WSSSE = clusters.computeCost(data)
    println("Within Set Sum of Squared Errors = " + WSSSE)

    System.out.println("Cluster centers:");
    for(center <- clusters.clusterCenters)
      println(" " + center);
  	
    //进行一些预测
  	println("Prediction of (0.0,0,163,4): "
  		  + clusters.predict(Vectors.dense(Array(0.0,0,163,4))));
  	println("Prediction of (95.53499886661372,1,124,0): "
  		  + clusters.predict(Vectors.dense(Array(95.53499886661372,1,124,0))));
  	println("Prediction of (92.89709742183328,1,31,0): "
  		  + clusters.predict(Vectors.dense(Array(92.89709742183328,1,31,0))));
    // Save and load model
    clusters.save(sc, result_path)
    val sameModel = KMeansModel.load(sc, result_path)
    
    
  }
}