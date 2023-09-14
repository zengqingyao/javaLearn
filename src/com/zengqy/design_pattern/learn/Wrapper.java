package com.zengqy.design_pattern.learn;

/**
 * @包名: com.zengqy.design_pattern.learn
 * @author: zengqy
 * @DATE: 2023/8/28 15:10
 * @描述: 装饰者模式，例如context
 * //        对装饰器模式来说，装饰者和被装饰者都实现同一个接口/抽象类。对代理模式来说，
 * //        代理类和被代理的类都实现同一个接口/抽象类，在结构上确实没有啥区别。但是他们的作用不同，
 * //        装饰器模式强调的是增强自身，在被装饰之后你能够在被增强的类上使用增强后的功能，增强后你还是你，
 * //        只不过被强化了而已；代理模式强调要让别人帮你去做事情，以及添加一些本身与你业务没有太多关系的事情（
 * //        记录日志、设置缓存等）重点在于让别人帮你做。
 * //        装饰模式和代理模式的不同之处在于思想。
 */


public class Wrapper {

    public static void main(String[] args) {

        Context wrapper = new ContextWrapper(new ContextImpl());
        wrapper.test();

    }
}


interface Context{
    void test();
}

class ContextImpl implements Context{

    @Override
    public void test() {
        System.out.println("真正实现了方法");
    }
}

class ContextWrapper implements Context{

    Context mContext;

    public ContextWrapper(Context context) {
        mContext = context;
    }

    @Override
    public void test() {
        System.out.println("装饰者模式开始");
        mContext.test();
    }
}