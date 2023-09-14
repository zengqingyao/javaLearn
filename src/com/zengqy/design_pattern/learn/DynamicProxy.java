package com.zengqy.design_pattern.learn;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @包名: com.zengqy.design_pattern.learn
 * @author: zengqy
 * @DATE: 2023/8/28 15:14
 * @描述: 动态代理的使用
 */
public class DynamicProxy {
    public static void main(String[] args) {

        UsbSell king = new UsbKingSell();

        // 动态代理，对目标进行增强
        UsbSell proxy = (UsbSell) Proxy.newProxyInstance(DynamicProxy.class.getClassLoader()
                , new Class[]{UsbSell.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        if (method.getName().equals("sell")) {
                            Float price = (Float) method.invoke(king, args);
                            price += 10000; //加价
                            return price;
                        } else if (method.getName().equals("getMsg")) {
                            return "Xiaomi";
                        }


                        return method.invoke(king, args);
                    }
                });


        System.out.println(proxy.sell(10));
        proxy.showMsg("zengqy", 30);
        System.out.println(proxy.getMsg());

    }
}

interface UsbSell {
    float sell(int count);

    void showMsg(String name, int age);

    String getMsg();
}

class UsbKingSell implements UsbSell {

    @Override
    public float sell(int count) {
        return (float) (count * 99.9);
    }

    @Override
    public void showMsg(String name, int age) {
        System.out.println("showMsg: " + name + " age:" + age);
    }

    @Override
    public String getMsg() {
        return "King";
    }
}