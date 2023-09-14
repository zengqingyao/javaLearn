package com.zengqy.design_pattern;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @包名: com.zengqy.design_pattern
 * @author: zengqy
 * @DATE: 2023/8/20 22:29
 * @描述: 设计模式
 * 1. 单一职责原则，就是每个类只干某一件事，不要把其他不相干的实现也添加进来
 * 2. 开闭原则，类、函数应该对外扩展开发，不如使用abstract抽象类，然后让不同的其他人实现方法从而实现不同的方法。
 */
public class DesignPattern {
    public static void main(String[] args) {
        //1. 工厂方法模式
        System.out.println(new AppleFactory().getFruit());

        System.out.println("=============抽象工厂模式================");
        // 2.抽象工厂模式 创建型
        new XiaomiFactory().createPhone().call();
        new XiaomiFactory().createRouter().connect();

        System.out.println("=============构建者模式================");
        // 3. 构建者模式 ，适合用于对象太多参数的情况
        Student build = Student.builder()
                .setName("zengqy")
                .setAge(100)
                .build();

        System.out.println(build);


        System.out.println("==============适配器模式===============");
        // 3. 适配器模式 ，新的接口兼容以前的
        AudioPlayerAdapter adapter = new AudioPlayerAdapter(new OldAudioPlayerImpl());
        adapter.play("mp3", "/data/app");
        adapter.play("mp4", "/data/app");


        System.out.println("==============装饰器模式===============");

//        对装饰器模式来说，装饰者和被装饰者都实现同一个接口/抽象类。对代理模式来说，
//        代理类和被代理的类都实现同一个接口/抽象类，在结构上确实没有啥区别。但是他们的作用不同，
//        装饰器模式强调的是增强自身，在被装饰之后你能够在被增强的类上使用增强后的功能，增强后你还是你，
//        只不过被强化了而已；代理模式强调要让别人帮你去做事情，以及添加一些本身与你业务没有太多关系的事情（
//        记录日志、设置缓存等）重点在于让别人帮你做。
//        装饰模式和代理模式的不同之处在于思想。

        new ContextWrapper(new ContextImpl()).test();


        System.out.println("==============动态代理模式===============");

        final UsbKingFactory proxyObject = new UsbKingFactory(); //被代理的对象

        UsbSell proxy = (UsbSell) Proxy.newProxyInstance(proxyObject.getClass().getClassLoader(),
                new Class[]{UsbSell.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Object res = null;

                        // 当调用show方法的时候
                        if (method.getName().equals("show")) {
                            res = method.invoke(proxyObject, args);
                        }
                        else if (method.getName().equals("sell")) { // 当调用sell方法的时候
                            res = method.invoke(proxyObject,args); //返回Float对象
                            Float price = (Float) res; // 代理商加价
                            price+=10000;
                            res = price;
                        }
                        return res;
                    }
                });

        System.out.println(proxy.sell(2));
        proxy.show("zengqy", 200);
    }
}


// 设计模式（创建型）
//=======================================================
//1. 工厂方法模式
abstract class Fruit {
    private final String name;

    public Fruit(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "name='" + name + '\'' +
                '}';
    }
}

class Apple extends Fruit {   //苹果，继承自水果

    public Apple() {
        super("苹果");
    }
}

interface FruitFactory {
    Fruit getFruit();
}

class AppleFactory implements FruitFactory {

    @Override
    public Apple getFruit() {
        return new Apple();
    }
}

//========================================================
//=======================================================
//2. 抽象工厂模式
interface Phone {
    void call();
}

interface Router {
    void connect();
}

interface ElectronicFactory {
    Phone createPhone();

    Router createRouter();
}

class XiaomiPhone implements Phone {

    @Override
    public void call() {
        System.out.println(getClass().getSimpleName() + " 生产的电话被调用了");
    }
}

class XiaomiRouter implements Router {

    @Override
    public void connect() {
        System.out.println(getClass().getSimpleName() + " 生产的的路由联网了");
    }
}

//
class XiaomiFactory implements ElectronicFactory {

    @Override
    public Phone createPhone() {
        return new XiaomiPhone();
    }

    @Override
    public Router createRouter() {
        return new XiaomiRouter();
    }
}


//=======================================================
//3. 建造者模式
class Student {
    int age;
    String name;

    private Student(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public static BuildStudent builder() {
        return new BuildStudent();
    }


    public static class BuildStudent {
        int age;
        String name;

        public BuildStudent setAge(int age) {
            this.age = age;
            return this;
        }

        public BuildStudent setName(String name) {
            this.name = name;
            return this;
        }

        public Student build() {
            return new Student(age, name);
        }
    }


    @Override
    public String toString() {
        return "Student{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}

//=======================================================
//4. 适配器模式
// 新的接口 类/对象适配器模式，这里使用对象适配器模式，因为类适配器模式需要继承旧的类，不好
interface NewAudioPlayer {
    void play(String audioType, String file);
}

// 新的音频播放器实现类
class NewAudioPlayerImpl implements NewAudioPlayer {

    @Override
    public void play(String audioType, String fileName) {
        System.out.println("新的实现类 Playing " + audioType + " file: " + fileName);
    }
}

// 旧的以前的接口
interface OldAudioPlayer {
    void playAudio(String file);
}

class OldAudioPlayerImpl implements OldAudioPlayer {

    @Override
    public void playAudio(String file) {
        System.out.println("旧的实现类 Playing audio file: " + file);
    }
}

class AudioPlayerAdapter implements NewAudioPlayer {

    OldAudioPlayer mOldAudioPlayer;

    public AudioPlayerAdapter(OldAudioPlayer oldAudioPlayer) {
        this.mOldAudioPlayer = oldAudioPlayer;
    }


    @Override
    public void play(String audioType, String file) {
        if ("mp3".equals(audioType)) {
            mOldAudioPlayer.playAudio(file);
        } else {
            new NewAudioPlayerImpl().play(audioType, file);
        }
    }
}

//=======================================================
//4. 装饰者模式
interface Context {
    void test();
}

class ContextImpl implements Context {

    @Override
    public void test() {
        System.out.println("ContextImpl 实现");
    }
}

class ContextWrapper implements Context {

    private Context mContext;

    public ContextWrapper(Context context) {
        mContext = context;
    }

    @Override
    public void test() {
        System.out.println("装饰器模式，添加额外的内容，增强对象");
        mContext.test();
    }
}

//=======================================================
//4. 静态代理跟装饰者模式基本一样，这里研究动态代理，jdk的只支持interface接口
interface UsbSell {
    String show(String name, int age);
    float sell(int count);
}

// 需要代理的对象
class UsbKingFactory implements UsbSell {

    @Override
    public String show(String name, int age) {
        System.out.println("实现了动态代理的对象.. " + name + " " + age);
        return null;
    }

    @Override
    public float sell(int count) {
        System.out.println("UsbKingFactory 中shell被调用, 你买了多少块："+count);
        return 90.1F*count;
    }
}