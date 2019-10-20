package com.timebusker.streaming.etl

import com.timebusker.streaming.utils.KafkaUtil
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @Description:ImoocAccessLogETL
 * @Author:Administrator
 * @Date2019/10/20 15:49
 **/
object ImoocAccessLogETL {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("ImoocAccessLogETL").setMaster("local[3]")
    val ssc = new StreamingContext(conf, Seconds(10))

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "12.12.12.6:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "spark-etl",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (true: java.lang.Boolean)
    )
    val topics = Array("imooc-access")

    // 数据消费
    val streaming = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )
    // 消费到的record包含很多元数据信息，时间传输数据只有 record.value()
    streaming.map(record => {
      println(record.key())
      println(record.value())
    })

    // 数据清洗
    val transformRDD = streaming.map(record => parseLog(record.value()))

    // 数据转入kafka做统一数据流处理
    // 广播变量
    val kafkaProducer: Broadcast[KafkaUtil[String, String]] = {
      ssc.sparkContext.broadcast(KafkaUtil[String, String](KafkaUtil.kafkaProducerConfig))
    }
    transformRDD.foreachRDD(rdd => {
      rdd.foreachPartition(partition => {
        partition.foreach(record => {
          kafkaProducer.value.send("imooc-access-etl", record.toString())
        })
      })
    })

    ssc.start()
    ssc.awaitTerminationOrTimeout(3000000)
  }

  /**
   * 解析每行日志记录
   *
   * @param line
   */
  def parseLog(line: String): String = {
    try {
      val splits = line.split("\t")

      val datetime = splits(0)
      val url = splits(1)
      val traffic = splits(2)
      val ip = splits(3)

      val cmsTypeId = url.replace("http://www.imooc.com/", "").split("/")
      var cmsType = ""
      var cmsId = ""
      if (cmsTypeId.length > 1) {
        cmsType = cmsTypeId(0)
        cmsId = cmsTypeId(1)
      }

      val area = "华东区域"
      val province = "浙江省"
      val city = "杭州市"
      url + "\t" + cmsType + "\t" + cmsId + "\t" + traffic + "\t" + ip + "\t" + area + "\t" + province + "\t" + city + "\t" + datetime
    } catch {
      case e: Exception => {
        println(line)
        ""
      }
    }
  }
}
