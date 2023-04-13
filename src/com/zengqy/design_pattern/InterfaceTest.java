package com.zengqy.design_pattern;

/**
 * @包名: com.zengqy.design_pattern
 * @author: zengqy
 * @DATE: 2023/3/13 16:26
 * @描述:
 *  面向接口编程，接口为UsbInterface，然后电脑Computer通过接口来调用不同的类，Phone和Camera
 *
 */
public class InterfaceTest {

    public static void main(String[] args) {

        Computer.work(new CameraImpl());
        System.out.println("\n==============================");
        Computer.work(new PhoneImpl());

        System.out.println("\n=============================");
        UsbInterface.callStatic();
        System.out.println(UsbInterface.AGE);
        System.out.println(UsbInterface.NAME);
    }
}


/**
 * 多态方法
 */
class Computer{

    public static void work(UsbInterface usbInterface)
    {
        usbInterface.start();
        usbInterface.call();
        usbInterface.stop();

        if(usbInterface instanceof CameraImpl)
        {
            System.out.println(((CameraImpl) usbInterface).NAME);
        }
        else if(usbInterface instanceof PhoneImpl)
        {
            System.out.println(((PhoneImpl) usbInterface).NAME);
        }
    }
}



/**
 * 实现接口必须，实现接口的抽象方法
 */
class CameraImpl implements UsbInterface
{

    public String NAME = "CameraImpl的名字";

    @Override
    public void start() {
        System.out.println("CameraImpl 实现 UsbInterface 的 start() 方法");
    }

    @Override
    public void stop() {
        System.out.println("CameraImpl 实现 UsbInterface 的 stop() 方法");
    }

    @Override
    public void call() {
        System.out.println("CameraImpl 重写 UsbInterface 的 call() 方法");
    }
}


class PhoneImpl implements UsbInterface{

    public String NAME = "PhoneImpl的名字";

    @Override
    public void start() {
        System.out.println("Phone 实现 UsbInterface 的 start() 方法");
    }

    @Override
    public void stop() {
        System.out.println("Phone 实现 UsbInterface 的 stop() 方法");
    }

    @Override
    public void call() {
        System.out.println("PhoneImpl 调用 UsbInterface 的 call() 方法");
        UsbInterface.super.call();
    }
}



interface UsbInterface
{

    /**
     *  接口定义属性的时候，默认会自动加上：public static final, 就是静态常量属性
     */
    int AGE = 20;
    public static final String NAME = "UsbInterface的名字";

    /**
     * 接口定义的时候，默认会加上 public abstract，所以写不写都一样
     */
    void start();
    public abstract void stop();

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