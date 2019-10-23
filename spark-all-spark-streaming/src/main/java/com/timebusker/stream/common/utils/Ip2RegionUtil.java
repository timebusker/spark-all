package com.timebusker.stream.common.utils;

import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;

/**
 * @Description:Ip2RegionUtil
 * @Author:Administrator
 * @Date2019/10/20 23:39
 **/
public class Ip2RegionUtil {



    public static void main(String[] args) throws Exception {
        //ip
        String ip = "20.19.153.27";

        // 判断是否为IP地址 (可用)
        //boolean isIpAddress = Util.isIpAddress(ip);

        //ip和long互转 (可用)
        //long ipLong = Util.ip2long(ip);
        //String strIp = Util.long2ip(ipLong);

        //根据ip进行位置信息搜索
        DbConfig config = new DbConfig();

        //获取ip库的位置（放在src下）（直接通过测试类获取文件Ip2RegionTest为测试类）
        String dbfile = Ip2RegionUtil.class.getResource("/ip2region.db").getPath();

        DbSearcher searcher = new DbSearcher(config, dbfile);

        //采用Btree搜索
        DataBlock block = searcher.btreeSearch(ip);

        //打印位置信息（格式：国家|大区|省份|城市|运营商）
        System.out.println(block.getRegion());
    }
}
