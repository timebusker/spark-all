package com.timebusker.common.test;

import com.timebusker.stream.common.MySQLExecuteUtil;
import com.zaxxer.hikari.HikariDataSource;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @DESC:MySQLExcetueUtilTest
 * @author:timebusker
 * @date:2019/9/23
 */
public class MySQLExecuteUtilTest {

    private static final Set<String> set = new HashSet<>();

    private static final int TEST_THREAD_COUNT = 100;

    private static final CountDownLatch latch = new CountDownLatch(TEST_THREAD_COUNT);

    public static void main(String[] args) {
        for (int i = 0; i < TEST_THREAD_COUNT; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    HikariDataSource dataSource = new MySQLExecuteUtil().getDataSource();
                    set.add(dataSource.getPoolName());
                    latch.countDown();
                }
            });
            thread.start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (String name : set) {
            System.out.println(name);
        }
    }
}
