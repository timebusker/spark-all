package com.timebusker.rdd02

import java.util.UUID

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

import scala.collection.mutable

/*
  * @DESC:MostPopularTeacherTopN:计算最受欢迎的老师TopN
  * @author:timebusker
  * @date:2019 /7/9
  */
object MostPopularTeacherTopN {

  val inputPath = "D:\\WorkSpaces\\ideaProjectes\\spark-all\\spark-all-sparkrdd\\src\\main\\resources\\02\\"

  val resultPath = "D:\\result\\" + System.currentTimeMillis() + "\\";

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("MostPopularTeacherTopN").setMaster("local[4]")
    val sc = new SparkContext(conf)

    val lines = sc.textFile(inputPath, 1)

    // 最受欢迎的老师
    mostPopularTeacher(lines)

    // 统计每个学科老师的喜好度
    mostPopularCourseAndTeacher(lines)

    // 计算每个学科最受欢迎的前3名
    mostPopularCourseAndTeacherTopN(lines)

    // 释放资源
    sc.stop()
  }

  def mostPopularTeacher(lines: RDD[String]) = {
    val teacherRDD = lines.map(x => {
      var teacher = x.substring(x.lastIndexOf("/") + 1)
      (teacher, 1)
    })
    teacherRDD.saveAsTextFile(resultPath + "001")

    val reduceRDD = teacherRDD.reduceByKey(_ + _).sortBy(_._2, false)
    reduceRDD.saveAsTextFile(resultPath + "002")
  }

  def mostPopularCourseAndTeacher(lines: RDD[String]) = {
    val courseAndTeacherRDD = lines.map(x => {
      val arr: Array[String] = x.replaceAll("http://", "").replaceAll(".edu360.cn", "").split("/")
      (arr(0), arr(1))
    })
    courseAndTeacherRDD.saveAsTextFile(resultPath + "003")

    val mapRDD = courseAndTeacherRDD.map((_, 1))
    mapRDD.saveAsTextFile(resultPath + "004")

    val reduceRDD = mapRDD.reduceByKey(_ + _).sortBy(_._2)
    reduceRDD.saveAsTextFile(resultPath + "005")
  }

  /**
    * 计算每个学科下最受欢迎的前几位老师
    *
    * @param lines
    */
  def mostPopularCourseAndTeacherTopN(lines: RDD[String]) = {
    val courseAndTeacherRDD = lines.map(x => {
      val arr: Array[String] = x.replaceAll("http://", "").replaceAll(".edu360.cn", "").split("/")
      (arr(0), arr(1))
    })

    val mapRDD = courseAndTeacherRDD.map((_, 1))
    val reduceRDD = mapRDD.reduceByKey(_ + _)

    // 设置重分区
    // 计算有多少学科
    val subjects = reduceRDD.map(_._1._1).distinct().collect()
    val partitioner = new MinePartitioner(subjects)
    val partitioned = reduceRDD.partitionBy(partitioner)
    partitioned.saveAsTextFile(resultPath + "006")

    // 针对分区内元素求出TopN
    val sorted = partitioned.mapPartitions(iterator => {
      // 将迭代器转换成list，然后排序，在转换成迭代器返回
      iterator.toList.sortBy(_._2).reverse.take(3).iterator
    })
    sorted.saveAsTextFile(resultPath + "007")


    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
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
