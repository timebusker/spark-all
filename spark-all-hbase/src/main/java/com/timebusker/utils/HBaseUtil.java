package com.timebusker.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Set;

/**
 * @Description:HBaseUtil
 * @Author:Administrator
 * @Date2019/11/6 14:01
 **/
public class HBaseUtil {

    private static Configuration conf = null;

    public HBaseUtil() {
        conf = HBaseConfiguration.create();
        conf.addResource("hdfs-site.xml");
        conf.addResource("core-site.xml");
        conf.addResource("hbase-site.xml");
    }

    /**
     * 创建表
     *
     * @param tableName
     * @param family
     * @throws Exception
     */
    public void createTable(String tableName, Set<String> family) throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        HTableDescriptor desc = new HTableDescriptor(tableName);
        for (String column : family) {
            desc.addFamily(new HColumnDescriptor(column));
        }
        if (admin.tableExists(tableName)) {
            System.out.println("表已经存在，别瞎输行吗！");
            System.exit(0);
        } else {
            admin.createTable(desc);
            System.out.println("表创建成功");
        }
    }

    /**
     * 创建表
     *
     * @param tableName
     * @param descriptor
     * @throws Exception
     */
    public void createTable(String tableName, HTableDescriptor descriptor) throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        boolean exists = admin.tableExists(Bytes.toBytes(tableName));
        System.out.println(exists ? "表已存在" : "表不存在");
        admin.createTable(descriptor);
        exists = admin.tableExists(Bytes.toBytes(tableName));
        System.out.println(exists ? "创建表成功" : "创建失败");
    }

    /**
     * 获取表的基本信息
     *
     * @param tableName
     * @throws Exception
     */
    public void descTable(String tableName) throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        HTable table = new HTable(conf, tableName);
        HTableDescriptor desc = table.getTableDescriptor();
        HColumnDescriptor[] columnFamilies = desc.getColumnFamilies();
        for (HColumnDescriptor t : columnFamilies) {
            System.out.println(Bytes.toString(t.getName()));
        }

    }

    /**
     * 替换该表tableName的所有列簇
     *
     * @param tableName
     * @throws Exception
     */
    public void modifyTable(String tableName) throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        HTableDescriptor htd = new HTableDescriptor(TableName.valueOf(tableName));
        htd.addFamily(new HColumnDescriptor(Bytes.toBytes("cf3")));
        htd.addFamily(new HColumnDescriptor(Bytes.toBytes("cf2")));
        admin.modifyTable(tableName, htd);
        System.out.println("修改成功");
    }

    /**
     * 获取所有的表信息
     *
     * @throws Exception
     */
    public void getAllTables() throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        String[] tableNames = admin.getTableNames();
        for (int i = 0; i < tableNames.length; i++) {
            System.out.println(tableNames[i]);
        }
    }

    /**
     * 更新数据 && 插入数据
     *
     * @param tableName
     * @param rowKey
     * @param familyName
     * @param columnName
     * @param value
     * @throws Exception
     */
    public void putData(String tableName, String rowKey, String familyName, String columnName, String value) throws Exception {
        HTable htable = new HTable(conf, Bytes.toBytes(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.add(Bytes.toBytes(familyName), Bytes.toBytes(columnName), Bytes.toBytes(value));
        htable.put(put);
    }

    /**
     * 为表添加数据
     *
     * @param tableName
     * @param rowKey
     * @param column1
     * @param value1
     * @param column2
     * @param value2
     * @throws Exception
     */
    public void addData(String tableName, String rowKey, String[] column1, String[] value1, String[] column2, String[] value2) throws Exception {
        Put put = new Put(Bytes.toBytes(rowKey));
        HTable htable = new HTable(conf, Bytes.toBytes(tableName));
        HColumnDescriptor[] columnFamilies = htable.getTableDescriptor().getColumnFamilies();
        for (int i = 0; i <= columnFamilies.length; i++) {
            String nameAsString = columnFamilies[i].getNameAsString();
            if (nameAsString.equals("lie01")) {
                for (int j = 0; j < column1.length; j++) {
                    put.add(Bytes.toBytes(nameAsString), Bytes.toBytes(column1[j]), Bytes.toBytes(value1[j]));
                }
            }
            if (nameAsString.equals("lie02")) {
                for (int j = 0; j < column2.length; j++) {
                    put.add(Bytes.toBytes(nameAsString), Bytes.toBytes(column2[j]), Bytes.toBytes(value2[j]));
                }
            }
        }
        htable.put(put);
        System.out.println("addData ok!");
    }

    /**
     * 根据rowKey查询
     *
     * @param tableName
     * @param rowKey
     * @return
     * @throws Exception
     */
    public Result getResult(String tableName, String rowKey) throws Exception {
        Get get = new Get(Bytes.toBytes(rowKey));
        HTable htable = new HTable(conf, Bytes.toBytes(tableName));
        Result result = htable.get(get);
        for (KeyValue k : result.list()) {
            System.out.println(Bytes.toString(k.getFamily()) + "\t" + Bytes.toString(k.getQualifier()) + "\t" + Bytes.toString(k.getValue()) + "\t" + k.getTimestamp());
        }
        return result;
    }

    /**
     * 查询指定的某列
     *
     * @param tableName
     * @param rowKey
     * @param familyName
     * @param columnName
     * @return
     * @throws Exception
     */
    public Result getResult(String tableName, String rowKey, String familyName, String columnName) throws Exception {
        Get get = new Get(Bytes.toBytes(rowKey));
        HTable htable = new HTable(conf, Bytes.toBytes(tableName));
        get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
        Result result = htable.get(get);
        for (KeyValue k : result.list()) {
            System.out.println(Bytes.toString(k.getFamily()));
            System.out.println(Bytes.toString(k.getQualifier()));
            System.out.println(Bytes.toString(k.getValue()));
            System.out.println(k.getTimestamp());
        }
        return result;
    }


    /**
     * 遍历查询表
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    public ResultScanner getResultScann(String tableName) throws Exception {
        Scan scan = new Scan();
        ResultScanner rs = null;
        HTable htable = new HTable(conf, tableName);
        try {
            rs = htable.getScanner(scan);
            for (Result r : rs) {
                for (KeyValue kv : r.list()) {
                    System.out.println(Bytes.toString(kv.getRow()));
                    System.out.println(Bytes.toString(kv.getFamily()));
                    System.out.println(Bytes.toString(kv.getQualifier()));
                    System.out.println(Bytes.toString(kv.getValue()));
                    System.out.println(kv.getTimestamp());
                }
            }
        } finally {
            rs.close();
        }
        return rs;
    }

    /**
     * @param tableName
     * @param scan
     * @return
     * @throws Exception
     */
    public ResultScanner getResultScann(String tableName, Scan scan) throws Exception {
        ResultScanner rs = null;
        HTable htable = new HTable(conf, tableName);
        try {
            rs = htable.getScanner(scan);
            for (Result r : rs) {
                for (KeyValue kv : r.list()) {
                    System.out.println(Bytes.toString(kv.getRow()));
                    System.out.println(Bytes.toString(kv.getFamily()));
                    System.out.println(Bytes.toString(kv.getQualifier()));
                    System.out.println(Bytes.toString(kv.getValue()));
                    System.out.println(kv.getTimestamp());
                }
            }
        } finally {
            rs.close();
        }
        return rs;
    }

    /**
     * 查询表中的某一列
     *
     * @param tableName
     * @param rowKey
     * @param familyName
     * @param columnName
     * @return
     * @throws Exception
     */
    public Result getResultByColumn(String tableName, String rowKey, String familyName, String columnName) throws Exception {
        HTable htable = new HTable(conf, tableName);
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
        Result result = htable.get(get);
        for (KeyValue kv : result.list()) {
            System.out.println(Bytes.toString(kv.getFamily()));
            System.out.println(Bytes.toString(kv.getQualifier()));
            System.out.println(Bytes.toString(kv.getValue()));
            System.out.println(kv.getTimestamp());
        }
        return result;
    }


    /**
     * 查询某列数据的某个版本
     *
     * @param tableName
     * @param rowKey
     * @param familyName
     * @param columnName
     * @param versions
     * @return
     * @throws Exception
     */
    public Result getResultByVersion(String tableName, String rowKey, String familyName, String columnName, int versions) throws Exception {
        HTable htable = new HTable(conf, tableName);
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
        get.setMaxVersions(versions);
        Result result = htable.get(get);
        for (KeyValue kv : result.list()) {
            System.out.println(Bytes.toString(kv.getFamily()));
            System.out.println(Bytes.toString(kv.getQualifier()));
            System.out.println(Bytes.toString(kv.getValue()));
            System.out.println(kv.getTimestamp());
        }
        return result;
    }

    /**
     * 删除指定某列
     *
     * @param tableName
     * @param rowKey
     * @param falilyName
     * @param columnName
     * @throws Exception
     */
    public void deleteColumn(String tableName, String rowKey, String falilyName, String columnName) throws Exception {
        HTable htable = new HTable(conf, tableName);
        Delete de = new Delete(Bytes.toBytes(rowKey));
        de.deleteColumn(Bytes.toBytes(falilyName), Bytes.toBytes(columnName));
        htable.delete(de);
    }


    /**
     * 删除指定的某个rowKey
     *
     * @param tableName
     * @param rowKey
     * @throws Exception
     */
    public void deleteColumn(String tableName, String rowKey) throws Exception {
        HTable htable = new HTable(conf, tableName);
        Delete de = new Delete(Bytes.toBytes(rowKey));
        htable.delete(de);

    }

    /**
     * 让该表失效
     *
     * @param tableName
     * @throws Exception
     */
    public void disableTable(String tableName) throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        admin.disableTable(tableName);
    }

    /**
     * 删除表
     *
     * @param tableName
     * @throws Exception
     */
    public void dropTable(String tableName) throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        admin.disableTable(tableName);
        admin.deleteTable(tableName);
    }
}
