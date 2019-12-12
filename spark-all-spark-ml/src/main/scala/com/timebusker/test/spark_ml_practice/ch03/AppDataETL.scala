package com.timebusker.test.spark_ml_practice.ch03

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * @Description: AppDataETL 数据加载转换
 * @Author: Administrator
 * @Date: 2019/11/30 17:45
 **/
object AppDataETL {

  val inputPath = "D:\\test-data\\book2-master\\2rd_data\\ch03\\appdata.txt"

  val outputPath = "D:\\test-data\\test\\ch02\\" + this.getClass.getSimpleName + "_"

  val APP_CLASSES = Seq(
    "购物优惠",
    "地图旅游",
    "教育学习",
    "金融理财",
    "游戏娱乐"
  )

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local[8]")
    conf.setAppName(this.getClass.getSimpleName)
    val spark = SparkSession.builder().config(conf).getOrCreate()
    spark.sparkContext.setCheckpointDir(outputPath + "_____" + System.currentTimeMillis())
    // 【包名】~【应用名称】~【类别】~【标签】~【应用介绍】
    val files = spark.sparkContext.textFile(inputPath).map(line => {
      val array = line.split("~", -1)
      // 从应用信息简介中提取（动词和名词）作简介基础信息
      val introductions = array(4).split(" ").map(_.split("/")).filter(line => {
        line(0).length > 1 && (line(1).equals("v") || line(1).contains("n"))
      }).map(x => x(0))
      (array(0), array(1), array(2), array(3), introductions)
    });

    // 编码格式转换--将词义量化
    // 给没有简介标有的词义做下标排序，实现Map(词语-->标号)
    // 根据简介词语出现的次数来对App算法分类
    val minDF = files.map(line => (line._3, line._5)).flatMap(_._2.distinct).distinct()
    val indexes = minDF.collect().zipWithIndex.toMap

    files.map(line => (line._3, line._5)).saveAsTextFile(outputPath + System.currentTimeMillis())

    val training = files.map(line => (line._3, line._5)).map { case (label, terms) => {
      val svm = terms.map(key => (key, 1)).groupBy(_._1).map(line => (line._1, line._2.length)).map(line => {
        (indexes.get(line._1).getOrElse(-1) + 1, line._2)
      }).filter(_._2 > 0).toSeq.sortBy(_._1)
        // 将集合拼接为 svm 数据格式
        .map(line => line._1 + ":" + line._2)
        // 将集合元素按照空格转换为字符串
        .mkString(" ")
      // 保存数据格式
      (APP_CLASSES.indexOf(label), svm)
    }
    }.filter(_._2.length > 0).map(line => {
      line._1 + " " + line._2
    })
    training.saveAsTextFile(outputPath + System.currentTimeMillis())
    spark.stop()
  }

}
