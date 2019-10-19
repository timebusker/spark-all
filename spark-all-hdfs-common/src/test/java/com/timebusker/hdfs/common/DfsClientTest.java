package com.timebusker.hdfs.common;

import com.timebusker.hdfs.common.utils.DfsClientUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @DESC:DfsClientTest
 * @author:timebusker
 * @date:2019/9/3
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class DfsClientTest {

    @Autowired
    private DfsClientUtil client;

    @Test
    public void test1() {
        System.err.println(client.getFsURL());
    }

    @Test
    public void test2() {
        client.copyFromLocalFile("D:\\WorkSpaces\\ideaProjectes\\spark-all\\spark-all-spark-rdd\\src\\\\main\\resources\\03", "/hdfs-test");
    }

    @Test
    public void test3() {
        client.mkdir("/output");
    }
}
