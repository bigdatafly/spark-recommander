import AssemblyKeys._
assemblySettings

name := "mllib"

version := "1.0"

scalaVersion := "2.11.7"

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "1.6.0"
libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "1.6.0"
libraryDependencies += "org.scalanlp" % "breeze_2.11" % "0.11.2"

libraryDependencies += "org.apache.spark" % "spark-streaming-flume_2.11" % "1.6.0"

libraryDependencies += "org.apache.spark" % "spark-graphx_2.11" % "1.6.0"

// http://mvnrepository.com/artifact/org.apache.spark/spark-streaming-kafka_2.11
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.11" % "1.6.1"

libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0"

libraryDependencies += "com.google.protobuf" % "protobuf-java" % "2.6.1"

//libraryDependencies += "org.scalatest" %% "scalatest" % "2.0" % "test"

// http://mvnrepository.com/artifact/redis.clients/jedis
libraryDependencies += "redis.clients" % "jedis" % "2.8.1"



