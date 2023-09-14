package com.zengqy.jvm;

/**
 * @包名: com.zengqy.jvm
 * @author: zengqy
 * @DATE: 2023/5/22 12:41
 * @描述:
 */
public class Javap {
    public static void main(String[] args) {



//        String s0 = new String("abc");
//        String s1 = new String("abc");
        String s2 = new String("de")+new String("f");
        String s3 = new String("de")+new String("f");

        // jdk1.7后，调用intern后，运行时常量池里面的对象就会指向s2堆中的对象
        // 所以s3.intern指向的对象就跟s2是同一个
        
        System.out.println(s2==s2.intern());
        System.out.println(s3.intern()==s2);

//        System.out.println(s0==s1);
//        System.out.println(s0.equals(s1));
//        System.out.println(s0.intern()==s1.intern());
//        System.out.println(s0.intern()=="abc");
//        System.out.println(s0.intern());

        int res = a();
        System.out.println(res);
    }

    public static int a(){
        return b();
    }

    public static int b(){
        int a = 10;
        int b = 20;
        return a+b;
    }
}
