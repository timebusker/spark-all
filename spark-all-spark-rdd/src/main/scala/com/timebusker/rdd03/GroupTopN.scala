//package com.timebusker.rdd03
//
//import com.timebusker.rdd02.MostPopularTeacherTopN.resultPath
//import org.apache.spark.rdd.RDD
//import org.apache.spark.{SparkConf, SparkContext}
//
//import scala.collection.mutable
//
///**
//  * @DESC:GroupTopN： 需求：排出每个科目的前三名
//  * @author:timebusker
//  * @date:2019/7/17
//  */
//object GroupTopN {
//
//  /**
//    * TopN问题显然是可以使用action算子take来完成，
//    * 但是因为take需要将所有数据都拉取到Driver上才能完成操作，
//    * 所以Driver的内存压力非常大，不建议使用take.
//    */
//
//  val inputPath = "D:\\WorkSpaces\\ideaProjectes\\spark-all\\spark-all-sparkrdd\\src\\main\\resources\\03\\"
//
//  val resultPath = "D:\\result\\" + System.currentTimeMillis() + "\\";
//
//  val TOPN = 3
//
//  def main(args: Array[String]): Unit = {
//    val conf = new SparkConf().setAppName("GroupTopN").setMaster("local[8]")
//    val sc = new SparkContext(conf)
//
//    val linesRDD = sc.textFile(inputPath)
//    doGroupBykey(linesRDD)
//  }
//
//
//  def doGroupBykey(linesRDD: RDD[String]) = {
//    val mapRDD = linesRDD.map(line => {
//      var fields = line.split(" ")
//      (fields(0), line)
//    })
//    // 进行shuffle
//    val groupRDD = mapRDD.groupByKey()
//    groupRDD.foreach(println)
//    println("==========TopN前==========")
//    val resRDD = groupRDD.map(tuple => {
//      var set = new mutable.TreeSet[String]()(new MineOrdering())
//      val subject = tuple._1 //  chinese
//      val scores = tuple._2 //  ("ls 91", "ww 56", ...)
//      for (score <- scores) {
//        // 添加到treeSet中
//        set.add(score)
//        if (set.size > TOPN) {
//          // 如果大小大于3，则弹出最后一份成绩
//          set = set.dropRight(1)
//        }
//      }
//      (subject, set)
//    })
//    resRDD.saveAsTextFile(resultPath + "001")
//
//    val storeRDD = resRDD.flatMap(x => {
//      println("---------->>>>\t" + x._2)
//      for (student <- x._2) {
//        val subject = student.split(" ")(0)
//        val name = student.split(" ")(1)
//        val score = student.split(" ")(2)
//        yield (subject, name, score)
//      }
//    })
//    storeRDD.saveAsTextFile(resultPath + "002")
//  }
//
//  def doCombineByKey(lineRDD: RDD[String]) = {
//    // https://blog.51cto.com/xpleaf/2108763
//  }
//}
//
//class MineOrdering extends Ordering[String] {
//  override def compare(x: String, y: String): Int = {
//    val xScore = x.split(" ")(2).toInt
//    val yScore = y.split(" ")(2).toInt
//    val ret = yScore - xScore
//    ret
//  }
//}
