package com.timebusker.test.spark_ml_practice.ch04

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.evaluation.ClusteringEvaluator
import org.apache.spark.sql.SparkSession

/**
 * @Description:KMeansModel:采用了UCI数据库中提供的鸢尾花卉数据集进行实验
 * @Author:Administrator
 * @Date:2019/12/11 22:42
 **/
object KMeansModel {

  // 设置应用日志级别
  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local[4]")
    conf.setAppName(this.getClass.getSimpleName)
    val spark = SparkSession.builder().config(conf).getOrCreate()

    val data = spark.read.format("libsvm").load("D:\\test-data\\book2-master\\2rd_data\\ch04\\iris_kmeans.txt")

    val kMeans = new KMeans().setK(3).setSeed(1L)
    val model = kMeans.fit(data)

    // Make predictions
    val predictions = model.transform(data)
    predictions.show(200)

    import spark.implicits._
    predictions.toDF().printSchema()
    predictions.toDF.filter($"label" === $"prediction").show(200)

    // Evaluate clustering by computing Silhouette score
    val evaluator = new ClusteringEvaluator()

    val silhouette = evaluator.evaluate(predictions)
    println(s"Silhouette with squared euclidean distance = $silhouette")

    // Shows the result.
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)

    spark.stop()
  }

}
