package com.timebusker.rdd03

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @DESC:GroupTopN： 需求：排出每个科目的前三名
  * @author:timebusker
  * @date:2019/7/17
  */
object GroupTopN {

  /**
    * TopN问题显然是可以使用action算子take来完成，
    * 但是因为take需要将所有数据都拉取到Driver上才能完成操作，
    * 所以Driver的内存压力非常大，不建议使用take.
    */

  val inputPath = "D:\\WorkSpaces\\ideaProjectes\\spark-all\\spark-all-sparkrdd\\src\\main\\resources\\03\\"

  val resultPath = "D:\\result\\" + System.currentTimeMillis() + "\\";

  val TOPN = 3

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("GroupTopN").setMaster("local[8]")
    val sc = new SparkContext(conf)

    val lineRDD = sc.textFile(inputPath, 3)
  }


  def doGroupBykey(lineRDD: RDD[String]) = {
    val mapRDD = lineRDD.map(line => {
      var fields = line.split(" ")
      (fields(0), (fields(1), fields(2)))
    })
    // 进行shuffle
    val groupRDD = mapRDD.groupByKey()

  }

  def doCombineByKey(lineRDD: RDD[String]) = {

  }
}
