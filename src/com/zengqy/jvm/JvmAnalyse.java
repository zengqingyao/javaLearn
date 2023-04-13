package com.zengqy.jvm;


/*
* JVM分为：
* 类加载过程：加载->链接(验证->准备->解析)->初始化
* 类加载器由上往下有：
*   1. 根加载器器bootstrap class loader , C++实现,基类加载器
*   2. 扩展类加载器 extension class loader
*   3. 应用程序(系统)加载器 app class loader
*
* 使用双亲委派机制：https://www.cnblogs.com/enroute/p/13865807.html
*   1. 向上委托查找，会向上查询是否加载，如果已经加载了就不需要加载了，直到bootstrap加载器
*   2. 向下委托加载，从根加载器向下询问是否可以加载该类.
* 作用：
*   1.防止加载同一个.class，通过委托去询问上级是否已经加载过该.class，如果加载过了，则不需要重新加载。保证了数据安全。
*   2.保证核心.class不被篡改。通过委托的方式，保证核心.class不被篡改，即使被篡改也不会被加载，即使被加载也不会是同一个class对象，
*     因为不同的加载器加载同一个.class也不是同一个Class对象。这样则保证了Class的执行安全。
*
*
* JVM内存结构的细化：https://www.cnblogs.com/secbro/p/11718987.html?ivk_sa=1024320u
* 线程私有的：
*   程序计数器: 程序计数器是用于存放下一条指令所在单元的地址的地方
*   本地方法栈: JVM执行的native方法，用于管理本地方法的调用
*   虚拟机栈: 一个方法以栈帧存在，存放8大基本数据类型(byte-short-int-long-float-double-boolean-char)，对象的引用等等局部变量
*
* 线程共享的：
*   方法区(非堆): 方法区只是一个规范，JDK1.8之前方法区的实现叫永久代，JDK1.8后改为叫元空间，存储在本地内存（Native memory)中。
*          元空间：存储在本地内存（Native memory)中，存放常量池，类信息，还有class的static变量，当使用元空间时，
*                 可以加载多少类的元数据就不再由MaxPermSize控制,而由系统的实际可用空间来控制。
*
*   堆: 可以存放new出来的对象实例和数组，分为新生代和老年代，对不同的区域进行不同的GC算法，也就是分代收集算法。
*       新生代：
*           伊甸园区: 刚开始new出来的对象都存放在eden区
*           S0区：
*           S1区：
*       轻GC：(GC的复制算法)
*           如果eden区无法再创建对象，就会触发一次轻GC，此时会将S0区和eden区进行可达性分析，找出活跃对象使用复制算法复制到S1(to)区。
*           并且将S0区和eden区的对象清空，这样不可达的对象就被清除了，然后将S0区和S1区交换(from和to，谁空谁是to)
*
*       老年代：当一个对象经历了15次GC后,还没有被释放，则会进入老年代
*
*       重GC：(标记清除算法，标记压缩整理算法)
*
*
*
*  GC的算法：
*   引用计数法：
*   复制算法： 主要使用在新生代(伊甸园区,so,s1)，可以解决内存碎片的问题，弊端是浪费了一半的空间(to必须为空)，复制对象也是一种开销。
*             复制算法最佳使用情景：对象存活度较低的情况下
*   标记清除算法：扫描对象，进行标记，标记后清除，但是会导致存在内存碎片，
*           缺点是：两次扫描，浪费时间，会产生内存碎片。
*           优点是：不需要额外的内存。
*   标记压缩算法：可以防止内存碎片，但是多了移动对象的成本。
*
* */

import java.util.ArrayList;

public class JvmAnalyse {

    byte[] array = new byte[1*1024*1024];

    public static void main(String[] args) {

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

        ArrayList<JvmAnalyse> list = new ArrayList<>();
        int count = 0;



        try {
            while (true)
            {
                list.add(new JvmAnalyse());
                count++;
            }
        }catch (Throwable e){
            System.out.println(count);
            e.printStackTrace();
        }

    }
}
