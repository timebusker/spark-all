package com.timebusker.scala.imooc

import com.ggstar.util.ip.IpHelper
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SaveMode, SparkSession}
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}

/**
 * @Description:ImoocAccessETL
 * @Author:Administrator
 * @Date2019/10/19 14:11
 **/
object ImoocAccessETL {

  val input = "hdfs://hdpcentos:9000/data/imooc-access"

  val output = "/output/imooc-access/"

  def main(args: Array[String]): Unit = {

    var conf = new SparkConf()
    conf.setAppName("ImoocAccessETL")
    conf.setMaster("local[2]")
    conf.set("spark.sql.warehouse.dir", "hdfs://hdpcentos:9000/user/hive2/warehouse")

    // 启用sparkSQL支持hive操作
    // 当没有配置hive-site.xml时，Spark会自动在当前应用目录创建metastore_db和创建由spark.sql.warehouse.dir配置的目录
    // 如果没有配置，默认是当前应用目录下的spark-warehouse目录
    val spark = SparkSession.builder.config(conf).enableHiveSupport().getOrCreate()

    val files = spark.sparkContext.textFile(input)

    val rowRDD = files.map(x => parseLog(x))

    // 将RDD 转换成 DataFrames
    val df = spark.createDataFrame(rowRDD, logStruct)
    df.printSchema()
    df.createOrReplaceTempView("tb_sub_imooc_access_tmp")

    // 存入现有hive表中
    // spark.sql("select * from tb_sub_imooc_access_tmp").write.mode(SaveMode.Overwrite).orc("hdfs://hdpcentos:9000/user/hive2/warehouse/tb_sub_imooc_access")

    // 自建表存储
    spark.table("tb_sub_imooc_access_tmp").write.saveAsTable("tb_sub_imooc_access")

    spark.stop()
  }

  //定义的输出的字段
  val logStruct = StructType(
    Array(
      StructField("url", StringType),
      StructField("cmstype", StringType),
      StructField("cmsid", StringType),
      StructField("traffic", StringType),
      StructField("ip", StringType),
      StructField("province", StringType),
      StructField("city", StringType),
      StructField("datatime", StringType)
    )
  )

  //解析每行日志记录
  def parseLog(line: String) = {
    try {
      val splits = line.split("\t")

      val url = splits(1)
      val traffic = splits(2)
      val ip = splits(3)

      val domain = "http://www.imooc.com/"
      val cms = url.substring(url.indexOf(domain) + domain.length)
      val cmsTypeId = cms.split("/")

      var cmsType = ""
      var cmsId = ""
      if (cmsTypeId.length > 1) {
        cmsType = cmsTypeId(0)
        cmsId = cmsTypeId(1)
      }

      val city = "" // IpHelper.findRegionByIp(ip)
      val time = splits(0)

      //这个row里面的字段要和struct中的字段对应上
      Row(url, cmsType, cmsId, traffic, ip, "", city, time)
    } catch {
      case e: Exception => {
        println(line)
        Row(0)
      }
    }
  }
}
