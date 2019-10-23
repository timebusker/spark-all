package com.timebusker.generate.task;

import com.timebusker.generate.service.MobileCallGenerateService;
import com.timebusker.generate.utils.KafkaPushMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description:MobileCallGenerateTask
 * @Author:Administrator
 * @Date2019/10/22 15:52
 **/
@Component
public class MobileCallGenerateTask {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TOPIC = "DC_MOCK_CALL_LOG";

    @Autowired
    private KafkaPushMessageUtil kafkaUtil;

    @Autowired
    private MobileCallGenerateService service;

    @Scheduled(fixedRate = 100)
    public void execute() throws Exception {
        String log = service.mockLog();
        logger.info("##########" + log);
        kafkaUtil.sendMessage(TOPIC, log);
    }
}
