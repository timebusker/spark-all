package com.timebusker.task;

import com.timebusker.kafka.PushMessageService;
import com.timebusker.model.MessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @DESC:KafkaTestTask:kafka消息定时推送
 * @author:timebusker
 * @date:2019/9/17
 */
@Component
public class KafkaTestTask {

    @Autowired
    private PushMessageService service;

    @Scheduled(fixedRate = 10000)
    public void execute() {
        service.sendMessage("timebusker", new MessageEntity("这是测试消息啊!").toString());
    }
}
