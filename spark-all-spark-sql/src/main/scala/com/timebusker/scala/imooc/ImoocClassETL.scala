package com.timebusker.scala.imooc

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
 * @Description:ImoocClassETL
 * @Author:Administrator
 * @Date2019/10/20 20:09
 **/
object ImoocClassETL {

  val input = "hdfs://hdpcentos:9000/data/imooc-class"

  val output = "hdfs://hdpcentos:9000/output/"

  val timestamps = System.currentTimeMillis();

  case class Course(cmsid: String, cmsname: String, cmstype: String, cmsurl: String)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("ImoocClassETL")
    conf.setMaster("local[*]")

    val spark = SparkSession.builder.config(conf).enableHiveSupport().getOrCreate()

    val courses = spark.sparkContext.textFile(input)
    val filters = courses.filter(line => line.contains(".imooc.com/"))
    filters.saveAsTextFile(output + timestamps + "_001")

    val urlAndName = filters.map(line => {
      try {
        val splits = line.split("\\|")(2)
        val url = splits.split("\t")(0)
        val name = splits.split("\t")(1)
        (url, name)
      } catch {
        case e: Exception => {
          println(line)
          ("", "")
        }
      }
    })
    urlAndName.saveAsTextFile(output + timestamps + "_002")

    val courseRDD = urlAndName.map(record => {
      val cmsUrl = record._1
      val cmsName = record._2
      var cmsId = ""
      var cmsType = ""
      if (cmsUrl.contains("https://www.imooc.com/learn/")) {
        cmsId = cmsUrl.replace("https://www.imooc.com/learn/", "")
        cmsType = "免费课程"
      } else if (cmsUrl.contains("https://coding.imooc.com/class/")) {
        cmsId = cmsUrl.replace("https://coding.imooc.com/class/", "").replace(".html", "")
        cmsType = "实战课程"
      } else if (cmsUrl.contains("https://class.imooc.com/sc/")) {
        cmsId = cmsUrl.replace("https://class.imooc.com/sc/", "")
        cmsType = "就业办系列课程"
      }
      Course(cmsId, cmsName, cmsType, cmsUrl)
    })
    courseRDD.saveAsTextFile(output + timestamps + "_003")

    import spark.implicits._
    val courseDF = courseRDD.toDF()
    courseDF.createOrReplaceTempView("tb_course_info_tmp")
    // spark默认采用parquet格式存储数据，此处使用orc存储
    // spark.sql("select * from tb_course_info_tmp").write.mode(SaveMode.Overwrite).saveAsTable("tb_course_info")

    // 如果直接使用表面保存数据，区分字段名称大小写，（hive中不区分但scala语法区分，需要统一）
    spark.table("tb_course_info_tmp").write.format("orc").mode(SaveMode.Overwrite).saveAsTable("tb_course_info")
    courseDF.write.format("orc").mode(SaveMode.Overwrite).saveAsTable("tb_course_info1")
  }
}
