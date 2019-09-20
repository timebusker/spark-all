package com.timebusker.streaming

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @DESC:SparkStreamingKafkaETL
 * @author:timebusker
 * @date:2019/9/20
 */
object SparkStreamingKafkaETL {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[8]").setAppName("SparkStreamingKafkaETL")
    val context = new StreamingContext(conf, Seconds(30))

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "spark-etl",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (true: java.lang.Boolean)
    )

    val topics = Array("timebusker")
    val stream = KafkaUtils.createDirectStream[String, String](
      context,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )
    stream.map(_.value()).flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).print()
    context.start()
    context.awaitTerminationOrTimeout(1000000)
  }
}
