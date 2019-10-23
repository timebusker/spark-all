package com.timebusker.generate.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:MobileCallGenerateService:模拟随机产生通话记录
 * @Author:Administrator
 * @Date2019/10/22 14:52
 **/
@Service
public class MobileCallGenerateService {

    private static final Map<String, String> calls = new HashMap<String, String>();
    private static final List<String> phoneNumber = new ArrayList<>();
    private static final Random random = new Random();

    static {
        calls.put("17078388295", "李雁");
        calls.put("13980337439", "卫艺");
        calls.put("14575535933", "仰莉");
        calls.put("19902496992", "陶欣悦");
        calls.put("18549641558", "施梅梅");
        calls.put("17005930322", "金虹霖");
        calls.put("18468618874", "魏明艳");
        calls.put("18576581848", "华贞");
        calls.put("15978226424", "华啟倩");
        calls.put("15542823911", "仲采绿");
        calls.put("17526304161", "卫丹");
        calls.put("15422018558", "戚丽红");
        calls.put("17269452013", "何翠柔");
        calls.put("17764278604", "钱溶艳");
        calls.put("15711910344", "钱琳");
        calls.put("15714728273", "缪静欣");
        calls.put("16061028454", "焦秋菊");
        calls.put("16264433631", "吕访琴");
        calls.put("17601615878", "沈丹");
        calls.put("15897468949", "褚美丽");
        calls.put("15810092493", "萧邦主");
        calls.put("18000696806", "赵贺彪");
        calls.put("15151889601", "张倩");
        calls.put("13269361119", "王世昌");
        calls.put("15032293356", "张涛");
        calls.put("17731088562", "张阳");
        calls.put("15338595369", "李进全");
        calls.put("15733218050", "杜泽文");
        calls.put("15614201525", "任宗阳");
        calls.put("15778423030", "梁鹏");
        calls.put("18641241020", "郭美彤");
        calls.put("15732648446", "刘飞飞");
        calls.put("13341109505", "段光星");
        calls.put("13560190665", "唐会华");
        calls.put("18301589432", "杨力谋");
        calls.put("13520404983", "温海英");
        calls.put("18332562075", "朱尚宽");
        calls.put("15902136987", "张翔");
        calls.put("13801358247", "杨超凡");
        calls.put("15975500987", "何潮辉");
        calls.put("13013685036", "庄银泳");
        calls.put("15019933667", "萧金辉");
        calls.put("18301930136", "黄海锋");
        phoneNumber.addAll(calls.keySet());
        System.err.println("-------------------------------------------------------------------------------------------");
        System.err.println("-------------------------------------------------------------------------------------------");
        System.err.println(calls.size());
        System.err.println(calls.values().size());
        System.err.println(phoneNumber.size());
        System.err.println("-------------------------------------------------------------------------------------------");
        System.err.println("-------------------------------------------------------------------------------------------");
    }

    private static final int MAX_CALL_NUMBER = phoneNumber.size();

    public String mockLog() {
        //产生主叫/被叫
        String initiativeNumber = phoneNumber.get(random.nextInt(MAX_CALL_NUMBER));
        String initiativeName = calls.get(initiativeNumber);
        Map<String, String> map = randomLonLat();
        String initiativeLon = map.get("LON");
        String initiativeLat = map.get("LAT");

        String passiveNumber = phoneNumber.get(random.nextInt(MAX_CALL_NUMBER));
        String passiveName = calls.get(passiveNumber);
        map = randomLonLat();
        String passiveLon = map.get("LON");
        String passiveLat = map.get("LAT");

        //通话时间生成
        int year = random.nextInt(2) == 0 ? 2018 : 2019;
        int month = random.nextInt(12);
        int day = random.nextInt(getDay(month)) + 1;
        int hour = random.nextInt(24);
        int min = random.nextInt(60);
        int sec = random.nextInt(60);
        //使用日期类格式化时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        Date timeDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd hh:mm:ss");
        String datetime = sdf.format(timeDate);
        //通话时长
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.applyPattern("000");
        // 默认通话时间范围[0,1800)秒
        int dur = random.nextInt(60 * 30);
        String duration = decimalFormat.format(dur);
        //通话记录
        String log = initiativeNumber + "," + initiativeName + "," + initiativeLon + "," + initiativeLat + ","
                + passiveNumber + "," + passiveName + "," + passiveLon + "," + passiveLat + "," + duration + "," + datetime;
        return log;
    }

    private static int getDay(int month) {
        if (month == 2)
            return 28;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
            return 31;
        return 30;
    }

    private static final double MIN_LON = 102.168663108349;
    private static final double MIN_LAT = 26.5426939317143;
    private static final double MAX_LON = 103.673591874361;
    private static final double MAX_LAT = 24.3852049170515;

    private static Map<String, String> randomLonLat() {
        BigDecimal db = new BigDecimal(Math.random() * (MAX_LON - MIN_LON) + MIN_LON);
        String lon = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        db = new BigDecimal(Math.random() * (MAX_LAT - MIN_LAT) + MIN_LAT);
        String lat = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        Map<String, String> map = new HashMap<String, String>();
        map.put("LON", lon);
        map.put("LAT", lat);
        return map;
    }

    public static void main(String[] args) {
        System.out.println(new MobileCallGenerateService().mockLog());
    }
}
