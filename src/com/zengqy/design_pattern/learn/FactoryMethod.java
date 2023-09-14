package com.zengqy.design_pattern.learn;

/**
 * @包名: com.zengqy.design_pattern.learn
 * @author: zengqy
 * @DATE: 2023/8/28 14:30
 * @描述: //1. 工厂方法模式
 */
public class FactoryMethod {
    public static void main(String[] args) {
        System.out.println(new AppleFactory().getFruit());

    }
}

abstract class Fruit {
    private String name;

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

class Apple extends Fruit {
    public Apple() {
        super("苹果");
    }
}

interface IFruitFactiry {
    Fruit getFruit();
}

class AppleFactory implements IFruitFactiry {

    @Override
    public Apple getFruit() {
        return new Apple();
    }
}


