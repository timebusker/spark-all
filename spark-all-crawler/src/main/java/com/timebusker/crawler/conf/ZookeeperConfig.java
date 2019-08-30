package com.timebusker.crawler.conf;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

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

    private ZkClient zkClient;

    private static final String IMOOC = "/IMOOC";

    public static final String EMPLOYMENT_CLASSES = IMOOC + "/EMPLOYMENT_CLASSES";

    public static final String PRACTICAL_COURSES = IMOOC + "/PRACTICAL_COURSES";

    public static final String FREE_COURSES = IMOOC + "/FREE_COURSES";

    @PostConstruct
    private void initZkClient() {
        if (zkClient == null) {
            zkClient = new ZkClient(servers);
        }
        /**
         * 需要先创建父节点
         */
        if (!zkClient.exists(IMOOC)) {
            zkClient.createPersistent(IMOOC, 1);
        }
    }

    public int getNodeData(String path) {
        if (!zkClient.exists(path)) {
            zkClient.createPersistent(path, 1);
            return 1;
        }
        int exists = zkClient.readData(path);
        zkClient.writeData(path, exists + 1);
        logger.info(path + "当前节点值是：\t" + exists);
        return exists;
    }
}
