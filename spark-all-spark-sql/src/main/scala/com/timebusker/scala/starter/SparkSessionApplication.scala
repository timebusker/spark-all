package com.timebusker.scala.starter

import org.apache.spark.{SparkContext, sql}
import org.apache.spark.sql.SparkSession

/**
  * @DESC:SparkSessionApplication:使用SparkSession测试
  * @author:timebusker
  * @date:2019/8/27
  */
object SparkSessionApplication {

  /**
    * 在spark 2.X以后，程序统一入口为spark session，可以使用spark session进行任何操作
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {

    val path = "D:\\WorkSpaces\\ideaProjectes\\spark-all\\spark-all-spark-sql\\src\\main\\resources"

    val spark = new sql.SparkSession.Builder().appName("SparkSessionApplication").master("local[2]").getOrCreate()

    val people = spark.read.json(path)
    people.printSchema()

    people.select("firstName").show()
    people.select((people.col("firstName") + people.col("lastName")).alias("name"),people.col("email")).show()

    spark.stop()
  }

}
