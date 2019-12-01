package com.timebusker.test.spark_ml_practice.ch02

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.mllib.linalg.{Matrix, Vectors}
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.sql.SparkSession

/**
 * @Description:GowallaDatasetExploration:用户签到时间数据样例分析
 * @Author:Administrator
 * @Date2019/11/26 16:25
 * @数据下载：https://snap.stanford.edu/data/loc-gowalla_totalCheckins.txt.gz
 **/
object GowallaDatasetExploration {
  // 设置应用日志级别
  Logger.getLogger("org").setLevel(Level.WARN)

  // 创建用户签到数据封装类
  case class UserCheckIn(userId: String, checkInTime: String, latitude: Double, longitude: Double, location: String)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName(this.getClass.getSimpleName)
    conf.setMaster("local[*]")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    // 数据解析
    val files = spark.sparkContext.textFile("D:\\test-data\\loc-gowalla_totalCheckins.txt")
    val data = files.map(_.split("\t")).map(line => {
      var time = line(1).replaceAll("[T|Z]", " ").trim
      UserCheckIn(line(0).trim, time, line(2).trim.toDouble, line(3).trim.toDouble, line(4).trim)
    }).cache()

    // 统计计算
    val static = data.map(line => {
      (line.userId, (1, Set(line.checkInTime.substring(0, 10)), line.location))
    }).reduceByKey((left, right) => {
      (left._1 + right._1, left._2.union(right._2), left._3.union(right._3))
    }).map(line => {
      Vectors.dense(line._2._1, line._2._2.size, line._2._3.toDouble)
    })

    // 保存数据结果
    // 两种方法是可以重设RDD分区：分别是coalesce()方法和repartition()
    // coalesce()方法的作用是返回指定一个新的指定分区的RDD，如果是生成一个窄依赖的结果，那么不会发生shuffle。比如：1000个分区被重新设置成10个分区，这样不会发生shuffle
    data.coalesce(1).saveAsTextFile("/out-path")

    // 统计方法
    val summary = Statistics.colStats(static);
    // 均值
    println("mean:\t" + summary.mean)
    // 最值
    println("Max:\t" + summary.max)
    // 方差
    println("Variance:\t" + summary.variance)
    // 非零元素的目录
    println("NumNonzeros:\t" + summary.numNonzeros)

    // 皮尔逊矩阵
    // val matrix = Statistics.corr(data,"pearson")
    // println("correlMatrix" + matrix.toString)
    spark.stop()
  }
}
