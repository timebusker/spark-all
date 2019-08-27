package com.timebusker.rdd01

import java.util.UUID

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @DESC:SparkWordCount:Spark编写Word Count
  * @author:timebusker
  * @date:2019 /7/9
  */
object SparkWordCount {

  val inputPath = "D:\\WorkSpaces\\ideaProjectes\\spark-all\\spark-all-sparkrdd\\src\\main\\resources\\01\\"

  val timestamps = System.currentTimeMillis();

  def main(args: Array[String]): Unit = {
    // 设置Spark APP名称和master地址：本地模式，并启动四个线程运算
    val conf = new SparkConf().setAppName("SparkWordCount").setMaster("local[4]")

    // 创建spark执行的入口
    val sc = new SparkContext(conf)

    // 编写RDD执行逻辑
    // 读取文件，设置设置RDD分区数为1
    val linesRDD = sc.textFile(inputPath, 4)
    // 输出执行结果
    linesRDD.saveAsTextFile("D:\\result\\" + timestamps + "\\001")

    // 先替换标点符号为空格
    val matcerRDD = linesRDD.map(_.replaceAll("[.,\"?~!;<>\\[\\]\\{\\}\\(\\)]", " "))
    matcerRDD.saveAsTextFile("D:\\result\\" + timestamps + "\\002")

    // 打平处理
    val wordsRDD = matcerRDD.flatMap(_.split(" "))
    wordsRDD.saveAsTextFile("D:\\result\\" + timestamps + "\\003")

    // 过滤空字符
    val filterRDD = wordsRDD.filter(x => x.trim.length > 0)
    filterRDD.saveAsTextFile("D:\\result\\" + timestamps + "\\004")

    // 给每一个词计数1进行组合
    val wordAsOneRDD = filterRDD.map((_, 1))
    wordAsOneRDD.saveAsTextFile("D:\\result\\" + timestamps + "\\005")

    //聚合：累计计算每个单词数量
    // 根据map集合的Key聚合运算，将RDD中元素两两传递给输入函数，同时产生一个新值，新值与RDD中下一个元素再被传递给输入函数，直到最后只有一个值为止。
    val reduceRDD = wordAsOneRDD.reduceByKey(_ + _) // 其等价于 wordAsOne.reduceByKey((x,y)=>{x+y})
    reduceRDD.saveAsTextFile("D:\\result\\" + timestamps + "\\006")

    // 最后结果数据排序,FALSE表示降序
    val sortRDD = reduceRDD.sortBy(_._2, false)
    sortRDD.saveAsTextFile("D:\\result\\" + timestamps + "\\007")

    // 释放资源
    sc.stop()
  }
}