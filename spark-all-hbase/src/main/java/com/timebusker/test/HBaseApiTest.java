package com.timebusker.test;

import com.timebusker.utils.HBaseUtil;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @Description:HBaseApiTest
 * @Author:Administrator
 * @Date2019/11/7 18:14
 **/
public class HBaseApiTest {

    private HBaseUtil util = new HBaseUtil();

    private String tableName = "tb_user_info";

    private String base_info = "base_info";

    private String extend_info = "extend_info";

    @Test
    public void test1() throws Exception {
        Set<String> family = new HashSet<>();
        family.add(base_info);
        family.add(extend_info);
        util.createTable(tableName, family);
    }

    @Test
    public void test2() throws Exception {
        util.descTable(tableName);
    }

    @Test
    public void test3() throws Exception {
        for (int i = 0; i < 100000; i++) {
            // String rowKey = UUID.randomUUID().toString().replace("-", "");
            String rowKey = System.currentTimeMillis() + "";
            util.putData(tableName, rowKey, base_info, "name", "Marry" + i);
            util.putData(tableName, rowKey, base_info, "sex", "男" + i);
            util.putData(tableName, rowKey, base_info, "age", "22岁" + i);
            util.putData(tableName, rowKey, extend_info, "address", "昆明市盘龙区昆明广场" + i + "号");
        }
    }

    @Test
    public void test4() throws Exception {
        util.getResult(tableName, "5176ad76b2ee40328bd4d7309d907146");
    }
}
