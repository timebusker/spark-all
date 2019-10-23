package com.timebusker.streaming.etl

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}

/**
 * @Description:DC_MOCK_CALL_LOG_ETL
 * @Author:Administrator
 * @Date2019/10/22 19:32
 **/
object DC_MOCK_CALL_LOG_ETL_HIVE {

  //  create table tb_dc_call_log (
  //    mobile string,
  //    name string,
  //    lon double,
  //    lat double,
  //    peer_mobile string,
  //    peer_name string,
  //    peer_lon double,
  //    peer_lat double,
  //    duration bigint,
  //    datetime string
  //  )partitioned by(ld bigint);

  case class CallLog(mobile: String, name: String, lon: Double, lat: Double, peer_mobile: String, peer_name: String, peer_lon: Double, peer_lat: Double, duration: Long, datetime: String, ld: Long)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("DC_MOCK_CALL_LOG_ETL_HIVE")
    conf.setMaster("local[*]")

    val spark = SparkSession.builder.config(conf).enableHiveSupport().getOrCreate()
    val ssc = new StreamingContext(spark.sparkContext, Minutes(60))

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "12.12.12.6:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "dw_mock",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (true: java.lang.Boolean)
    )
    val topics = Array("DC_MOCK_CALL_LOG")

    val streaming = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )
    val streamingValues = streaming.map(line => line.value())

    val format: Broadcast[SimpleDateFormat] = {
      ssc.sparkContext.broadcast(new SimpleDateFormat("YYYYmmddHH"))
    }

    spark.sql("set hive.exec.dynamic.partition.mode=nonstrict")
    streamingValues.foreachRDD(rdd => {
      val callLog = rdd.map(line => {
        val splits = line.split(",")
        val ld = format.value.format(new Date()).toInt
        CallLog(splits(0), splits(1), splits(2).toDouble, splits(3).toDouble, splits(4), splits(5), splits(6).toDouble, splits(7).toDouble, splits(8).toLong, splits(9), ld)
      })

      // 开启隐式转换
      import spark.implicits._
      val df = callLog.toDF()
      // 无论hive建表的时候，使用的fileformat使用的是哪一种，都是没有关系的
      // df.write.format("orc").partitionBy("ld").mode(SaveMode.Append).saveAsTable("tb_dc_call_log")
      df.write.format("hive").partitionBy("ld").mode(SaveMode.Append).saveAsTable("tb_dc_call_log")
      spark.sql("select count(*) from tb_dc_call_log").show()
    })

    ssc.start
    ssc.awaitTermination
  }
}
