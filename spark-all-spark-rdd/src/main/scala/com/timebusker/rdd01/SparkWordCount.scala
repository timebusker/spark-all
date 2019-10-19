package com.timebusker.rdd01

import java.util.UUID

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @DESC:SparkWordCount:Spark编写Word Count
 * @author:timebusker
 * @date:2019 /7/9
 */
object SparkWordCount {


  // spark保存文件时，请指明文件地址协议：
  // file:/// 表示本地文件系统路径
  // hdfs:// 表示hdfs文件系统
  // 本地模式测试的读取本地目录
  //  val input = "D:\\WorkSpaces\\ideaProjectes\\spark-all\\spark-all-spark-rdd\\src\\main\\resources\\01"

  // 集群测试
  val input = "hdfs://hdpcentos:9000/hdfs-test/01"

  // 集群单机均可写到hdfs
  val output = "hdfs://hdpcentos:9000/output/01/"

  val timestamps = System.currentTimeMillis();

  def main(args: Array[String]): Unit = {
    // 设置Spark APP名称和master地址：本地模式，并启动四个线程运算
    val conf = new SparkConf().setAppName("SparkWordCount").setMaster("spark://12.12.12.6:7077")
    conf.set("spark.executor.memory", "1G")
    conf.set("spark.total.executor.cores", "1")

    // 创建spark执行的入口
    val sc = new SparkContext(conf)

    // 编写RDD执行逻辑
    // 读取文件，设置设置RDD分区数为1
    val linesRDD = sc.textFile(input, 2)
    // 输出执行结果
    linesRDD.saveAsTextFile(output + timestamps + "_001")

    // 先替换标点符号为空格
    val matcerRDD = linesRDD.map(_.replaceAll("[.,\"?~!;<>\\[\\]\\{\\}\\(\\)]", " "))
    matcerRDD.saveAsTextFile(output + timestamps + "_002")

    // 打平处理
    val wordsRDD = matcerRDD.flatMap(_.split(" "))
    wordsRDD.saveAsTextFile(output + timestamps + "_003")

    // 过滤空字符
    val filterRDD = wordsRDD.filter(x => x.trim.length > 0)
    filterRDD.saveAsTextFile(output + timestamps + "_004")

    // 给每一个词计数1进行组合
    val wordAsOneRDD = filterRDD.map((_, 1))
    wordAsOneRDD.saveAsTextFile(output + timestamps + "_005")

    //聚合：累计计算每个单词数量
    // 根据map集合的Key聚合运算，将RDD中元素两两传递给输入函数，同时产生一个新值，新值与RDD中下一个元素再被传递给输入函数，直到最后只有一个值为止。
    val reduceRDD = wordAsOneRDD.reduceByKey(_ + _) // 其等价于 wordAsOne.reduceByKey((x,y)=>{x+y})
    reduceRDD.saveAsTextFile(output + timestamps + "_006")

    // 最后结果数据排序,FALSE表示降序
    val sortRDD = reduceRDD.sortBy(_._2, false)
    sortRDD.saveAsTextFile(output + timestamps + "_007")

    // 释放资源
    sc.stop()
  }
}