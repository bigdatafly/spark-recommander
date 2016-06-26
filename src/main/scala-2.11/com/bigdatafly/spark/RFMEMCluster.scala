package com.bigdatafly.spark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.mllib.clustering.{GaussianMixture , GaussianMixtureModel}
import org.apache.spark.mllib.linalg.{Vector,Vectors}
import org.apache.spark.mllib.regression.LabeledPoint


object RFMGMMCluster {
  
  val file_path = "F:\\data\\mahout\\order_norm_data.txt"
  val result_path = "F:\\data\\mahout\\gmm";
  
  def main(args : Array[String]) : Unit = {

    val conf = new SparkConf().setMaster("local[4]").setAppName("")
    val sc = new SparkContext(conf)
    val raw_data =sc.textFile(file_path, 4).map(line=>{
        val values = line.split("\t")
        new LabeledPoint(values(0).toInt,Vectors.dense(values.drop(1).map(_.toDouble)))
      }
    ).cache();
    
    val data = raw_data.map (x =>x.features)
    val numClusters = 5
    val gmm = new GaussianMixture().setK(numClusters).run(data);
    gmm.save(sc, result_path)
    val model =  GaussianMixtureModel.load(sc, result_path)
    
    // output parameters of max-likelihood model
    for (i <- 0 until gmm.k) {
      println("weight=%f\nmu=%s\nsigma=\n%s\n" format
        (gmm.weights(i), gmm.gaussians(i).mu, gmm.gaussians(i).sigma))
    }
    
  }
}