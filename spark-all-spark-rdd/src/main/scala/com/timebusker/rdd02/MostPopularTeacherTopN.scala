package com.timebusker.rdd02

import java.util.UUID

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

import scala.collection.mutable

/**
 * @DESC:MostPopularTeacherTopN:计算最受欢迎的老师TopN
 * @author:timebusker
 * @date:2019 /7/9
 */
object MostPopularTeacherTopN {

  val input = "D:\\WorkSpaces\\ideaProjectes\\spark-all\\spark-all-spark-rdd\\src\\main\\resources\\02"

  val output = "hdfs://hdpcentos:9000/output/02/"

  val timestamps = System.currentTimeMillis();

  val TopN = 3

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.OFF)
    Logger.getLogger("org.apache.hadoop").setLevel(Level.OFF)
    Logger.getLogger("org.spark_project").setLevel(Level.OFF)

    val conf = new SparkConf().setAppName("MostPopularTeacherTopN").setMaster("local[4]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

    val lines = sc.textFile(input, 1)

    // 最受欢迎的老师
    mostPopularTeacher(lines)

    // 统计每个学科老师的喜好度（每个学科最受欢迎的老师TopN）
    mostPopularCourseAndTeacher(lines)

    // 计算每个学科最受欢迎的前3名（每个学科最受欢迎的老师TopN）==> 分组topN
    mostPopularCourseAndTeacherTopN(lines)

    // 释放资源
    sc.stop()
  }

  def mostPopularTeacher(lines: RDD[String]) = {
    val teacherRDD = lines.map(x => {
      var teacher = x.substring(x.lastIndexOf("/") + 1)
      (teacher, 1)
    })
    teacherRDD.saveAsTextFile(output + timestamps + "_001")

    val reduceRDD = teacherRDD.reduceByKey(_ + _).sortBy(_._2, false)
    reduceRDD.saveAsTextFile(output + timestamps + "_002")
  }

  def mostPopularCourseAndTeacher(lines: RDD[String]) = {
    val courseAndTeacherRDD = lines.map(x => {
      val arr: Array[String] = x.replaceAll("http://", "").replaceAll(".edu360.cn", "").split("/")
      (arr(0), arr(1))
    })
    courseAndTeacherRDD.saveAsTextFile(output + timestamps + "_003")
    val mapRDD = courseAndTeacherRDD.map((_, 1))
    mapRDD.saveAsTextFile(output + timestamps + "_004")

    val reduceRDD = mapRDD.reduceByKey(_ + _).sortBy(_._2)
    reduceRDD.saveAsTextFile(output + timestamps + "_005")
  }

  /**
   * 计算每个学科最受欢迎的前3名（每个学科最受欢迎的老师TopN）==> 分组topN
   *
   * @param lines
   */
  def mostPopularCourseAndTeacherTopN(lines: RDD[String]) = {
    val courseAndTeacherRDD = lines.map(x => {
      val arr: Array[String] = x.replaceAll("http://", "").replaceAll(".edu360.cn", "").split("/")
      (arr(0), arr(1))
    })
    val mapRDD = courseAndTeacherRDD.map((_, 1))

    // 设置重分区
    // 计算有多少学科
    val subjects = mapRDD.map(_._1._1).distinct().collect()
    val partitioner = new MinePartitioner(subjects)
    val reduceRDD = mapRDD.reduceByKey(partitioner, _ + _)
    reduceRDD.saveAsTextFile(output + timestamps + "_006")

    // 针对分区内元素求出TopN
    val sorted = reduceRDD.mapPartitions(iterator => {
      // 将迭代器转换成list，然后排序，在转换成迭代器返回
      iterator.toList.sortBy(_._2).reverse.take(TopN).iterator
    })
    sorted.saveAsTextFile(output + timestamps + "_007")
    // 组合Key的分组topN不能只用简单的key聚合   groupByKey、CombineByKey
    // 求取分组后的TopN，目前是使用加载到内存后再排序求取，如果数据量大时不可行
  }
}

class MinePartitioner(subjects: Array[String]) extends Partitioner {

  /**
   * 相当于主构造器（new的时候回执行一次），用于存放规则的一个map
   */
  val rules = new mutable.HashMap[String, Int]()
  var i = 0
  for (subject <- subjects) {
    rules.put(subject, i)
    i += 1
  }

  override def numPartitions: Int = rules.size

  override def getPartition(key: Any): Int = {
    //获取学科名称
    val subject = key.asInstanceOf[(String, String)]._1
    //根据规则计算分区编号
    rules(subject)
  }
}
