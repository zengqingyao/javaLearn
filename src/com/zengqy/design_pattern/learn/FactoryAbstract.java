package com.zengqy.design_pattern.learn;

/**
 * @包名: com.zengqy.design_pattern.learn
 * @author: zengqy
 * @DATE: 2023/8/28 14:40
 * @描述: 抽象工厂方法，一个工厂生产多个类
 */
public class FactoryAbstract {


    public static void main(String[] args) {

        XiaomiFactory xiaomiFactory = new XiaomiFactory();
        xiaomiFactory.createPhone().call();
        xiaomiFactory.createRouter().connect();

    }

}

interface Phone {
    void call();
}

interface Router {
    void connect();
}

interface Factory {
    Phone createPhone();
    Router createRouter();
}

class XiaomiPhone implements Phone{

    @Override
    public void call() {
        System.out.println("小米手机");
    }
}

class XiaomiRouter implements Router{

    @Override
    public void connect() {
        System.out.println("小米路由");
    }
}

class XiaomiFactory implements Factory{

    @Override
    public Phone createPhone() {
        return new XiaomiPhone();
    }

    @Override
    public Router createRouter() {
        return new XiaomiRouter();
    }
}

