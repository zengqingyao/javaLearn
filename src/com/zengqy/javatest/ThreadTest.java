package com.zengqy.javatest;

/**
 * @包名: com.zengqy.javatest
 * @author: zengqy
 * @DATE: 2022/12/4 18:05
 * @描述:
 */
public class ThreadTest {

    public static void main(String[] args) {


        System.out.println("main主线程：" + Thread.currentThread().getName());

        Runtime runtime = Runtime.getRuntime();
        int cpuNums = runtime.availableProcessors();
        System.out.println("当前CPU可以被使用个数：" + cpuNums + "个");

        //能从操作系统获取的最大的内存，返回值为字节 1M = 1024KB = 1024Bytes ;
        //虚拟机参数 -Xmx512m   设置JVM最大允许分配的堆内存为512M，默认是物理内存的1/4。
        //          -Xms512m： 设置JVM初始分配的堆内存为512M，默认是物理内存的1/64
        //          -XX:+PrintGCDetails 每次GC时打印详细信息
        //          -XX:+HeapDumpOnOutOfMemoryError
        long max = Runtime.getRuntime().maxMemory();

        //已经使用内存大小
        long total = Runtime.getRuntime().totalMemory();

        System.out.println("能获取最大内存= "+max+" bytes\t"+max/(double)1024/1024+"MB");
        System.out.println("已经使用的内存= "+total+" bytes\t"+total/(double)1024/1024+"MB");



        // 1.
        SampleThread thread = new SampleThread();
        thread.start();



        // 匿名方式启动线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    System.out.println(Thread.currentThread().getName() + " 线程被执行" + i++ + "次");

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i == 30) {
                        break;
                    }
                }
            }
        }).start();



        // 匿名内部类
        Lamda lamda = new Lamda() {
            @Override
            public boolean test(String name, int age) {
                System.out.println(name + " " + age);
                return false;
            }
        };
        lamda.test("曾庆耀", 30);

        // lamda表达式 , 使用条件是函数式接口，就是接口只有一个方法
        // 可以省略 形参类型，可以省略 new Lamda(),只有单行代码甚至可以省略 {}
        Lamda lamda1 = (name, age) -> {
            System.out.println(name + " " + age);
            return false;
        };
        lamda1.test("曾庆耀", 32);



    }
}


/**
 * 继承Thread类并且实现run方法
 */
class SampleThread extends Thread {
    @Override
    public void run() {
        int i = 0;
        while (true) {
            System.out.println(Thread.currentThread().getName() + " 线程被执行" + i++ + "次");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i == 30) {
                break;
            }
        }
    }
}

class SampleRunnable implements Runnable{

    @Override
    public void run() {
        System.out.println("SampleRunnable");
    }
}


interface Lamda {
    boolean test(String name, int age);
}