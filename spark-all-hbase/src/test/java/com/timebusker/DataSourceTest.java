package com.timebusker;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.timebusker.mysql.entity.MySQLUserEntity;
import com.timebusker.mysql.repository.MySQLUserRepository;
import com.timebusker.phoenix.entity.PhoenixUserEntity;
import com.timebusker.phoenix.mapper.PhoenixUserMapper;
import com.timebusker.phoenix.repository.PhoenixUserRepository;
import com.timebusker.postgres.entity.PostgresUserEntity;
import com.timebusker.postgres.repository.PostgresUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * @Description:DataSourceTest
 * @Author:Administrator
 * @Date2019/11/13 22:58
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataSourceTest {

    @Autowired
    private MySQLUserRepository mysqlRepository;

    @Autowired
    private PostgresUserRepository postgresRepository;

    @Autowired
    private PhoenixUserMapper phoenixUserMapper;

    @Autowired
    private PhoenixUserRepository phoenixUserRepository;

    @Test
    public void test1() {
        for (int i = 0; i < 30; i++) {
            PostgresUserEntity postgres = new PostgresUserEntity();
            postgres.setIdx(UUID.randomUUID().toString().replace("-", ""));
            postgres.setName("Tom");
            postgres.setSex("男");
            postgres.setAge(32);
            postgres.setAddress("北京市");
            postgresRepository.save(postgres);

            MySQLUserEntity phoenix = new MySQLUserEntity();
            phoenix.setIdx(UUID.randomUUID().toString().replace("-", ""));
            phoenix.setName("Jetty");
            phoenix.setSex("女");
            phoenix.setAge(23);
            phoenix.setAddress("广州");
            mysqlRepository.save(phoenix);
        }
    }

    @Test
    public void test21() {
        for (int i = 0; i < 20; i++) {
            PhoenixUserEntity phoenix = new PhoenixUserEntity();
            phoenix.setIdx(UUID.randomUUID().toString().replace("-", ""));
            phoenix.setName("Tom");
            phoenix.setSex("男");
            phoenix.setAge("32");
            phoenix.setAddress("北京市");
            phoenixUserMapper.save(phoenix);
        }
    }

    @Test
    public void test22() {
        System.out.println("------------" + JSON.toJSONString(phoenixUserMapper.getById("1380f88442444bacb5d3b35fd3211473")));
        System.out.println("------------" + JSON.toJSONString(phoenixUserMapper.getByName("Tom")));
    }

    @Test
    public void test23() {
        PageHelper.startPage(1, 15);
        System.out.println("------------" + JSON.toJSONString(phoenixUserMapper.getAll()));
    }

    @Test
    public void test31() {
        PhoenixUserEntity phoenix = new PhoenixUserEntity();
        phoenix.setIdx(UUID.randomUUID().toString().replace("-", ""));
        phoenix.setName("Tomcat");
        phoenix.setSex("男");
        phoenix.setAge("100");
        phoenix.setAddress("北京市");
        phoenixUserRepository.save(phoenix);
    }
}
