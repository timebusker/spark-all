package com.timebusker.streaming.utils

import java.util.Properties
import java.util.concurrent.Future

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.StringSerializer

/**
 * @Description:KafkaUtil
 * @Author:Administrator
 * @Date2019/10/20 16:24
 **/
object KafkaUtil {

  val kafkaProducerConfig = {
    val properties = new Properties()
    properties.put(ProducerConfig.CLIENT_ID_CONFIG, "timebusker")
    properties.put(ProducerConfig.ACKS_CONFIG, "all")
    properties.put(ProducerConfig.RETRIES_CONFIG, "1")
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "12.12.12.6:9092")
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    properties
  }

  import scala.collection.JavaConversions._

  def apply[K, V](config: Map[String, Object]): KafkaUtil[K, V] = {
    val createProducerFunc = () => {
      val producer = new KafkaProducer[K, V](config)
      sys.addShutdownHook({
        producer.close()
      })
      producer
    }
    new KafkaUtil(createProducerFunc)
  }

  def apply[K, V](config: java.util.Properties): KafkaUtil[K, V] = apply(config.toMap)
}

class KafkaUtil[K, V](createProducer: () => KafkaProducer[K, V]) extends Serializable {

  lazy val producer = createProducer()

  def send(topic: String, key: K, value: V): Future[RecordMetadata] = producer.send(new ProducerRecord[K, V](topic, key, value))

  def send(topic: String, value: V): Future[RecordMetadata] = producer.send(new ProducerRecord[K, V](topic, value))
}
