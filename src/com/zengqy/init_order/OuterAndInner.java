package com.zengqy.init_order;



/**
 * @包名: com.zengqy.init_order
 * @author: zengqy
 * @DATE: 2023/3/22 23:06
 * @描述:  局部内部类，匿名内部类，成员内部类，静态成员内部类，还有初始化顺序
 */
public class OuterAndInner {

    // 运行局部内部类
    public static void runLocalInnerClass()
    {

        //1. 局部内部类是定义在外部类的局部变量，通常在方法
        //2. 不能添加访问修饰符，但是可以使用final修饰
        //3. 作用域：仅仅在定义它的方法或者代码块中
        //4. 局部内部类可以直接访问外部类的所有属性方法
        class LocalInnerClass implements UsbInterface{

            String name = "局部内部类的值";

            @Override
            public void strat() {
                System.out.println("测试运行局部内部类");
            }

        }

        LocalInnerClass localInnerClass = new LocalInnerClass();
        localInnerClass.strat();
        localInnerClass.call();
        System.out.println(localInnerClass.name);
        System.out.println(UsbInterface.name);
        System.out.println("局部内部类的class名字："+localInnerClass.getClass());
    }



    public static void main(String[] args) {
        
        // 1. 测试局部内部类
        System.out.println("==================局部内部类===================");
        runLocalInnerClass();


        // 2. 匿名内部类
        System.out.println("==================匿名内部类===================");
        UsbInterface usbInterface = new UsbInterface() {
            @Override
            public void strat() {
                System.out.println("测试运行匿名内部类");
            }
        };
        usbInterface.strat();

        // class com.zengqy.init_order.OuterAndInner$1  会顺延 $2 $3
        System.out.println("匿名内部类的class名字："+usbInterface.getClass());



        // 3. 成员内部类
        System.out.println("==================匿名内部类===================");

        // 1. get返回内部类的句柄
        Outer outer = new Outer();
        Outer.Inner innerInstance = outer.getInnerInstance();
        innerInstance.say();

        // 2. 主动去new 内部类
        Outer.Inner inner = new Outer().new Inner();
        inner.say();


        // 4. 静态内部类
        System.out.println("==================静态内部类===================");
//        Outer.InnerStatic innerStatic = new Outer.InnerStatic();
        Outer.InnerStatic.say();

    }
}


class Outer{

    public int age;
    public static String name = "外部类的name默认值";

//
//    private static User name = new User();

    static {
        System.out.println("==> 这是外部类的静态代码块");
    }

    {
        System.out.println("==> 这是外部类的普通代码块");
    }



    //========================成员内部类============================
    class Inner{
        public int age = 99;

        {
            System.out.println("==> 这是成员内部类的普通代码块");
        }

        public Inner() {
            System.out.println("==> Inner成员内部类的无参构造器被执行");
        }

        public Inner(int age) {
            this.age = age;
            System.out.println("==> Inner成员内部类的有参构造器被执行");
        }

        public void say()
        {
            System.out.println("成员内部类的方法 say 内部类的name: "+this.age);
            System.out.println("成员内部类的方法 say 外部类的name: "+ Outer.this.age);
        }
    }


    public Inner getInnerInstance()
    {
        return new Inner();
    }

    //========================成员内部类============================


    //========================静态内部类============================
    // 初始化 外部类的时候，静态内部类也不会被初始化
    public static class InnerStatic {
        int age;
        public static String name = "静态内部类的默认值";

        static {
            System.out.println("==> 这是静态内部类的静态代码块");
        }

        {
            System.out.println("==> 这是静态内部类的普通代码块");
        }

        public static void say() {
            System.out.println("is 静态内部类的方法 访问外部类的所有静态成员："+name);
            System.out.println("is 静态内部类的方法 访问外部类的所有静态成员："+ Outer.name);
        }

    }
    //========================静态内部类============================


    public Outer() {
        System.out.println("==> Outer外部类的无参构造器被执行");
    }

    public Outer(int age) {
        this.age = age;
        System.out.println("==> Outer外部类的有参构造器被执行");
    }

    @Override
    public String toString() {
        return "Outer{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}








interface UsbInterface{

    // 接口默认是 public final static
    String name = "接口的值";

    // 接口默认 public abstract
    public abstract void strat();

    /**
     * JDK8以后，接口支持定义默认方法，要加上defult关键字
     */
    default public void call(){
        System.out.println("JDK8以后，接口支持定义默认方法，要加上defult关键字");
    }

    /**
     * JDK8以后，接口支持定义静态方法，要加上static关键字
     */
    public static void callStatic()
    {
        System.out.println("JDK8以后，接口支持定义静态方法，要加上static关键字");
    }

}