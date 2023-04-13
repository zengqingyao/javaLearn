package com.zengqy.javatest;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @包名: com.zengqy.javatest
 * @author: zengqy
 * @DATE: 2022/12/2 19:52
 * @描述: 基本数据类型和类的使用
 */
public class basedata {


    public static void main(String[] args) throws UnsupportedEncodingException {


        System.out.println("=================字符的编码和解码==================");
        System.out.println("1天= "+TimeUnit.DAYS.toSeconds(1)+"秒");
        System.out.println("1天= "+TimeUnit.DAYS.toHours(1)+"小时");
        System.out.println("1小时= "+TimeUnit.HOURS.toSeconds(1)+"秒");

        try {
            // 休眠2秒钟
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("=================字符的编码和解码==================");
        String ssss = "a曾b";

        //使用默认的utf-8来进行编码 中文3个字节
        byte[] bytes2 = ssss.getBytes();
        System.out.println("UTF-8编码:"+Arrays.toString(bytes2));

        // 按照GBK进行编码 中文2个字节
        byte[] bytes3 = ssss.getBytes("GBK");
        System.out.println("GBK编码:"+Arrays.toString(bytes3));


        String s2 = new String(bytes2);
        System.out.println("UTF-8解码: "+s2);

        String s3 = new String(bytes3,"GBK");
        System.out.println("GBK解码: "+s3);


        // char 字符 2个字节，默认使用UTF-16存储，java char类型2个字节，最多只能由6万多个字符

        System.out.println("=================字符的编码和解码==================\n");






        String abc1 = "hello";
        String abc2 = "abc";
        String abc3 = abc1+abc2;  //底层调用StringBuilder然后 new String
        String abc4 = "helloabc";
        System.out.println(abc3.intern() == abc4);


        // boolean 不参与类型的自动转换
        // byte->short->int->long->float->double
        // char->int->long->float->double
        // 1.1默认是double型，所以需要加上f才能显示声明为float     1默认是int型
        // byte short char 相互可以计算，计算的时候会先转为int类型
        System.out.println("\n==========基本数据类型自动类型转换为最高精度的==========");
        int n1 = 10;
        float ff = n1 + 1.1f;
        float ff1 = (float) (n1 + 1.1);

        // 100d 就是100被认为100.0 double ，下面三元运算符是一个整体，精度会被提高到doble型，所以为1.0
        Object object = true ? new Integer(1) : new Double(100d);
        System.out.println(object);


        // jdk1.5以前手动拆箱和装箱
        System.out.println("\n==========手动拆箱和装箱==========");
        int i = 60;
        Integer integer = Integer.valueOf(i);
        Integer integer1 = new Integer(i);

        //收到拆箱
        int n = integer.intValue();
        System.out.println(n);
        System.out.println("\n==========自动拆箱和装箱==========");

        //自动装箱
        Integer integer2 = 200; //底层使用的是 Integer.valueOf(200)
        //自动拆箱
        int n3 = integer2; // 底层使用 integer2.intValue()
        System.out.println(integer2);

        System.out.println("\n==========自动拆箱和装箱==========");


        System.out.println("\n==========String类型和基本数据类型的转换==========");
        // String类是final类型，不能被继承，底层是通过字符数组实现 private final char value[];
        // String保存的是字符串常量，用final修饰，所以不能修改，每次String类的更新实际上是改地址
        // 支持 比较字符串，查找字符串，替换字符串等操作
        int n4 = 11;
        String string = n4 + "";
        int n5 = Integer.parseInt(string);
        System.out.println(n5);

        String a = "hello";
        String b = "abc";


        // 会调用StringBuilder sb , 然后通过 sb.append("hello) sb.append("abc),最后 new String()返回
        String c = a + b;


        // 调用String的toCharArray()返回char[] ,String的底层是char[]
        String str1 = "ab12曾";
        char[] chars = str1.toCharArray();
        for (int j = 0; j < chars.length; j++) {
            System.out.println(chars[j]);
        }

        // char[]转换成String 调用String的构造器，字符类型默认utf-16编码，2个字节
        char[] arrs = new char[]{'h', 'e', 'l', 'l', 'o', '我'};
        String str2 = new String(arrs);
        System.out.println(str2);//hello


//        编码：字符串转换成字节(从看得懂的转化成看不懂的二进制数据）
//        解码：字节转化成字符串(从看不懂的二进制数据转换成看得懂的东西)
        String str3 = "abc123中国";
        byte[] bytes = str3.getBytes();//使用默认的字符集（UTF-8）进行编码
        String str4 = new String(bytes);//使用默认的字符集（UTF-8）进行解码
        System.out.println(str4);//abc123中国

        byte[] bytes1 = str3.getBytes("GBK");//使用GBK字符集进行编码
        String str5 = new String(bytes1);
        System.out.println(str5);//输出结果为abc123�й�，原因是bytes1编码的时候用的是GBK，解码的时候用的是默认的字符集，所以乱码

        //编码用的GBK,解码必须也用GBK才不会出问题
        String str6 = new String(bytes1, "GBK");//指定用UTF-8这种字符集进行解码
        System.out.println(str6);//abc123中国


        System.out.println("\n==========StringBuffer类型和基本数据类型的转换==========");
        // StringBuffer保存的是字符串常量，里面的值可以更改，每次修改是修改实际的内容，是线程安全的

        // String 转为 StringBuffer
        String name = "zengqy";
        StringBuffer stringBuffer = new StringBuffer(name);

        StringBuffer stringBuffer1 = new StringBuffer();
        StringBuffer append = stringBuffer1.append(name);

        // StringBuffer 转为 String
        String s = stringBuffer.toString();
        System.out.println(s);

        String str = null;
        StringBuffer sb1 = new StringBuffer();
        // 内部源码会 复制 null 这个数组
        sb1.append(str);
        System.out.println(sb1.length());
        System.out.println(sb1);

        System.out.println("\n==========StringBuilder类型和基本数据类型的转换==========");
        //StringBuilder 因为方法没用带synchronized，故不是线程安全的，单线程的情况下使用 StringBuilder，效率比StringBuffer快

        int[] as = new int[]{1, -2, -5, 5, 6, 7, 0};
        Arrays.sort(as);
        String s1 = Arrays.toString(as);
        System.out.println(s1);

        int[] ints = Arrays.copyOf(as, 4);
        System.out.println(Arrays.toString(ints));

        System.out.println("\n==========大数处理==========");

        // 需要处理很大的整数，long无法存放，则需要大数类
        BigInteger bigInteger = new BigInteger("999999999999999999999999999999999");
        System.out.println(bigInteger);

        bigInteger = bigInteger.add(new BigInteger("1")); // 加
        System.out.println(bigInteger);

        bigInteger = bigInteger.subtract(new BigInteger("2")); // 减
        System.out.println(bigInteger);

        bigInteger = bigInteger.multiply(new BigInteger("2")); //乘
        System.out.println(bigInteger);

        bigInteger = bigInteger.divide(new BigInteger("2")); //除
        System.out.println(bigInteger);

        // 需要处理很高精度的浮点数
        System.out.println();
        BigDecimal bigDecimal = new BigDecimal("1999.11199999999999999999999999999");
        System.out.println(bigDecimal);

        // BigDecimal.ROUND_CEILING 会保留跟原精度一样的长度
        bigDecimal = bigDecimal.divide(new BigDecimal("2"), BigDecimal.ROUND_CEILING);
        System.out.println(bigDecimal);


        System.out.println("\n==========ArraysList==========");

        // 可以添加多个null，底层是通过数组来存储的，是线程不安全的
        // 如果是无参构造器，则默认容量为0，调用add后为10，以后以1.5倍扩容
        ArrayList arrayList = new ArrayList();
        arrayList.add("zengqy");
        arrayList.add(false);
        arrayList.add("ccccc");
        arrayList.add(200);

        // 迭代器遍历
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            System.out.println(next);
        }

        System.out.println();
        //foreach
        for (Object o : arrayList) {
            System.out.println(o);
        }


        HashMap hashMap = new HashMap();
        hashMap.put("aaa", "sadas");
        hashMap.put("bbb", "sadas");
        hashMap.put("ccc", "sadas");
        hashMap.put("ddd", "sadas");

        System.out.println("hashMap有几个元素："+hashMap.size());

        Set entrySet = hashMap.entrySet();

        Iterator iterator1 = entrySet.iterator();
        while (iterator1.hasNext()) {
            Map.Entry next = (Map.Entry) iterator1.next();
            System.out.println("运行时类型:"+next.getClass()+" "+next.getKey() + "-" + next.getValue());

        }


        ArrayList<StringBuilder> src = new ArrayList<>();
        src.add(new StringBuilder("zengqy1"));
        src.add(new StringBuilder("zengqy2"));
        src.add(new StringBuilder("zengqy3"));

        ArrayList<StringBuilder> dest = new ArrayList<>();
        for (int i1 = 0; i1 < src.size(); i1++) {
            dest.add(new StringBuilder());
        }

        Collections.copy(dest,src);
        System.out.println(dest);
        dest.get(0).append("-123"); //修改dest里面的值，因为是引用类型，等于直接修改src
        System.out.println(dest);
        System.out.println(src);


    }
}


