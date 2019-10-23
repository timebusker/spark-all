package com.timebusker.scala

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * @Description:SparkSQLWithHive
 * @Author:Administrator
 * @Date2019/10/19 20:21
 **/
object SparkSQLWithHive {

  /**
   * 1、配置本地spark环境，整合hive
   *
   * 2、拷贝hive-site.xml等文件到项目classpath路径下：/resources
   *
   * 3、hive连接元数据驱动也要配置，重点，不配置获取不到元数据不抛异常，直接使用本地目录
   *
   * @param args
   */
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local[*]")
    conf.setAppName("SparkSQLWithHive")

    val spark = SparkSession.builder.config(conf).enableHiveSupport().getOrCreate()

    import spark.sql

    sql("drop table if exists src")
    sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING) stored as textfile")
    sql("LOAD DATA INPATH '/hdfs-test/example-data/kv1.txt' INTO TABLE src")

    val src = sql("select * from src")

    // RDD与DataFrame互相转化
    val srcRDD = src.rdd.map(x => (x.get(0) + ":" + x.get(1)))
    srcRDD.foreach(println(_))
    spark.stop()
  }

}
