package com.timebusker.generate.conf;

import com.timebusker.generate.utils.ZookeeperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @DESC:ZookeeperConfig
 * @author:timebusker
 * @date:2019/8/30
 */

@Configuration
public class ZookeeperConfig {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfig.class);

    @Value("${zookeeper.servers}")
    private String servers;

    private String ROOT = "/SPARK-ALL-GENERATE";

    @Bean
    public ZookeeperUtil zookeeperUtils() {
        ZookeeperUtil zookeeperUtil = new ZookeeperUtil(servers, ROOT);
        logger.info("完成初始化zkClient客户端工具类！");
        return zookeeperUtil;
    }
}
