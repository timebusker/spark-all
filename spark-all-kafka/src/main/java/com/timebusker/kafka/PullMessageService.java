package com.timebusker.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.internals.Topic;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @DESC:PullMessageService
 * @author:timebusker
 * @date:2019/9/17
 */
@Component
public class PullMessageService {

    // 简单消费者
    @KafkaListener(topics = {"timebusker"})
    public void consumer(ConsumerRecord<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, Consumer consumer) {
        System.out.println("消费者收到消息:" + record.value() + "; topic:" + topic);
        // 如果需要手工提交异步
        consumer.commitSync();
        // 交手工同步提
        consumer.commitAsync();
        System.out.println();
    }

    // 分组1 中的消费者1
    @KafkaListener(id = "consumer1-1", groupId = "group1",
            topicPartitions = {@TopicPartition(topic = "timebusker", partitions = {"0", "1"})
            })
    public void consumer1_1(ConsumerRecord<String, Object> record) {
        System.out.println("consumer1-1 收到消息:" + record.value());
    }

    // 分组1 中的消费者2
    @KafkaListener(id = "consumer1-2", groupId = "group1",
            topicPartitions = {@TopicPartition(topic = "timebusker", partitions = {"2", "3"})
            })
    public void consumer1_2(ConsumerRecord<String, Object> record) {
        System.out.println("consumer1-2 收到消息:" + record.value());
    }

    // 分组1 中的消费者3
    @KafkaListener(id = "consumer1-3", groupId = "group1",
            topicPartitions = {@TopicPartition(topic = "timebusker", partitions = {"4", "5"})
            })
    public void consumer1_3(ConsumerRecord<String, Object> record) {
        System.out.println("consumer1-3 收到消息:" + record.value());
    }

    // 分组2 中的消费者
    @KafkaListener(id = "consumer2-1", groupId = "group1", topics = "timebusker")
    public void consumer2_1(ConsumerRecord<String, Object> record) {
        System.err.println("consumer2-1 收到消息:" + record.value());
    }
}
