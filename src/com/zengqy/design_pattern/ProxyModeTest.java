package com.zengqy.design_pattern;

/**
 * @包名: com.zengqy.design_pattern
 * @author: zengqy
 * @DATE: 2023/3/24 23:02
 * @描述: 代理模式，静态代理和动态代理
 */
public class ProxyModeTest {
    public static void main(String[] args) {


        // 1. 静态代理模式
        StaticThreadProxy s = new StaticThreadProxy(new Runnable() {
            @Override
            public void run() {
                System.out.println("模拟线程被执行了");
            }
        });
        s.start();

    }

}


/**
 * 模拟Thread()类里面的启动线程，其实就是静态代理模式
 */
class StaticThreadProxy implements Runnable{

    private Runnable target = null;

    @Override
    public void run() {
        if (target != null) {
            // 动态绑定了，所以调用传进来的对象的run方法
            target.run();
        }
    }

    public StaticThreadProxy(Runnable target) {
        this.target = target;
    }

    public void start()
    {
        start0();
    }

    // 这里其实应该是JVM底层通过 pthread_create创建线程调用run方法，这里只做模拟
    private void start0() {
        run();
    }
}