package com.zengqy.javatest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @包名: com.zengqy.javatest
 * @author: zengqy
 * @DATE: 2023/3/16 23:43
 * @描述:  关于时间的相关使用
 */
public class TimeTest {

    public static void main(String[] args) {

        //第一代时间类
        System.out.println("======================第一代时间类==========================");
        Date date = new Date();
        System.out.println(date);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss E");
        String format = simpleDateFormat.format(date);
        System.out.println(format);

        try {
            Date parse = simpleDateFormat.parse("2022年03月16日 11:48:44 星期四");
            System.out.println(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // 不是线程安全的
        System.out.println("======================第二代时间类==========================");
        Calendar instance = Calendar.getInstance();
        System.out.println("年："+instance.get(Calendar.YEAR));
        System.out.println("月："+(instance.get(Calendar.MONTH)+1));
        System.out.println("日："+instance.get(Calendar.DATE));
        System.out.println("小时："+instance.get(Calendar.HOUR));
        System.out.println("24小时："+instance.get(Calendar.HOUR_OF_DAY));


        System.out.println("======================第三代时间类==========================");

        // LocalDate LocalTime
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        System.out.println("年："+now.getYear());
        System.out.println("月："+now.getMonth());
        System.out.println("月："+now.getMonthValue());
        System.out.println("日："+now.getDayOfMonth());
        System.out.println("小时："+now.getHour());

        // 格式化时间
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 hh:mm:ss E");
        String format1 = dateTimeFormatter.format(now);
        System.out.println(format1);

        // instant 时间戳
        Instant instant = Instant.now();
        System.out.println("时间戳："+instant);

        Date date1 = Date.from(instant);

        Instant instant1 = date1.toInstant();

    }
}
