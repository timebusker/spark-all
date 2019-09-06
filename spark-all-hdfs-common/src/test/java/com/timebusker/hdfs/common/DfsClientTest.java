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
        client.copyToLocalFile("/BDS4/yarn/staging/root/.staging/job_1561413675073_0017/job.jar","/tmp/");
    }
}
