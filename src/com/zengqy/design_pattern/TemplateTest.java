package com.zengqy.design_pattern;

/**
 * @包名: com.zengqy.design_pattern
 * @author: zengqy
 * @DATE: 2023/3/12 23:56
 * @描述: 抽象类的模板设计模式
 */
public class TemplateTest {
    public static void main(String[] args) {

        Template template = new TemplateChildA();
        template.caleTimes();

        template = new TemplateChildB();
        template.caleTimes();
    }
}


/**
 * 必须实现抽象类的抽象方法
 */
class TemplateChildA extends Template
{

    /**
     * 继承抽象类必须重写抽象方法、
     */
    @Override
    public void doSomething() {

        int num = 0;
        for (int i = 0; i < 10000000; i++) {
            num+=i;
        }
        System.out.println("执行了TemplateChildA类的doSomething方法: "+num);
    }
}

class TemplateChildB extends Template
{

    /**
     * 继承抽象类必须重写抽象方法、
     */
    @Override
    public void doSomething() {

        int num = 0;
        for (int i = 0; i < 10000000; i++) {
            num*=i;
        }
        System.out.println("执行了TemplateChildB类的doSomething方法: "+num);
    }
}


/**
 * 模板基类，无法被实例化
 */
abstract class Template{

    public abstract void doSomething();

    public void caleTimes()
    {
        long start = System.currentTimeMillis();

        // 因为有动态绑定机制，所以运行的时候调用子类重写的方法。
        doSomething();

        long time = System.currentTimeMillis()-start;
        System.out.println("执行任务花费了多少时间："+time+"ms");
    }

}
