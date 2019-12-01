package com.timebusker.test.demo

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import scala.util.Random

/**
 * @Description:LinearRegressionMain线性回归分析
 * @Author:Administrator
 * @Date2019/11/25 20:31
 **/
object LinearRegressionMain {
  // 设置日志级别
  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf();
    conf.setMaster("local[4]")
    conf.setAppName(this.getClass.getSimpleName)
    val spark = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate();
    import spark.implicits._
    val random = new Random()
    val fileLines = spark.read.format("csv").option("sep", ";").option("header", true).load("test-data/house.csv")
    val data = fileLines.select("square", "price").map(row => {
      val square = row.getAs[String](0).toDouble
      val price = row.getAs[String](1).toDouble
      (square, price, random.nextDouble())
    }).toDF("square", "price", "rand").sort("rand")
    data.show()
  }

}
