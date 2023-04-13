package com.zengqy.design_pattern;

//懒加载方式，只在需要的时候才new对象，保证懒加载的情况保证线程安全，因为使用了双检索机制
public class Singleton {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //防止被new,构造器设置为private
    private Singleton(){}

    //使用volatile防止指令重排
    private volatile static Singleton singleton;


    public static Singleton getSingleton()
    {
        //双检锁
        if(singleton==null)
        {
            //不为null的时候加锁，然后里面再次判断是否为null
            synchronized (Singleton.class)
            {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }

        return singleton;
    }

    public static void main(String[] args) {
        Singleton singleton = getSingleton();
        singleton.setName("Singleton");
        System.out.println(singleton.getName());

        HungrySingleton hungrySingleton = HungrySingleton.getHungrySingleton();
        hungrySingleton.setName("HungrySingleton");
        System.out.println(hungrySingleton.getName());


        InnerStaticSingleton instance = InnerStaticSingleton.getInstance();
        instance.setName("InnerStaticSingleton");
        System.out.println(instance.getName());


        EnumSingleton enumSingleton = EnumSingleton.getInstance();
        enumSingleton.setName("EnumSingleton");
        System.out.println(enumSingleton.getName());


    }
}


//饿汉式单例模式，加载类的时候就已经初始化
class HungrySingleton{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static HungrySingleton hungrySingleton = new HungrySingleton();

    private HungrySingleton(){}

    public static HungrySingleton getHungrySingleton()
    {
        return hungrySingleton;
    }

}




//  静态内部类单例模式也称单例持有者模式，实例由内部类创建，
//  由于 JVM 在加载外部类的过程中, 是不会加载静态内部类的, 只有内部类的属性/方法被调用时才会被加载,
//  并初始化其静态属性。静态属性由static修饰，保证只被实例化一次，并且严格保证实例化顺序。
//  是开源项目中比较常用的一种单例模式。在没有加任何锁的情况下，保证了多线程下的安全，并且没有任何性能影响和空间的浪费。
class InnerStaticSingleton {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private InnerStaticSingleton(){}

    private static class Inner{
        private final static InnerStaticSingleton instance = new InnerStaticSingleton();
    }

    public static InnerStaticSingleton getInstance(){
        return Inner.instance;
    }

}

// 因为枚举类型是线程安全的，并且只会装载一次
class EnumSingleton{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    private EnumSingleton(){}

    private enum Singleton{
        INSTANCE;

        private final EnumSingleton instance;

        Singleton(){
            instance = new EnumSingleton();
        }

        private EnumSingleton getInstance(){
            return instance;
        }
    }

    public static EnumSingleton getInstance(){
        return Singleton.INSTANCE.getInstance();
    }
}