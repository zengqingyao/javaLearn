package com.zengqy.genericSecond;

import java.util.ArrayList;

/**
 * @包名: com.zengqy.genericSecond
 * @author: zengqy
 * @DATE: 2022/12/1 11:34
 * @描述:
 */
public class Generic<T,E> implements Generator<T> {

    private T key;
    private E value;


    public Generic(T key, E value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public T getKey() {
        return key;
    }

    @Override
    public void setKey(T key) {
        this.key = key;
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }


    /**
     * 泛型通配符
     * @param productGetter
     */
    public static void showProductValueAll(ProductGetter<?> productGetter)
    {
        System.out.println(productGetter.getValue());
    }

    /**
     * 通配符上限，extends 继承实参类型，只能是Numebr 或者其 子类
     * @param productGetter
     */
    public static void showProductValueUpperLimit(ProductGetter<? extends Number> productGetter)
    {
        System.out.println(productGetter.getValue());
    }

    public static void main(String[] args) {

        // 泛型接口的实现
        Generic<String,Integer> generic = new Generic<>("zengqy",100);
        String key = generic.getKey();
        Integer value = generic.getValue();
        System.out.println(key);
        System.out.println(value);

        //调用泛型类的成员方法
        System.out.println("=====================");
        ProductGetter<String> stringProductGetter = new ProductGetter<>();
        String[] str = {"苹果手机","华为手机","小米手机","机器人"};
        for (int i = 0; i < str.length; i++) {
            stringProductGetter.addProduct(str[i]);
        }
        System.out.println(stringProductGetter.getProduct());


        // 调用泛型方法
        System.out.println("=====================");
        ArrayList<String> list = new ArrayList<>();
        list.add("苹果手机");
        list.add("华为手机");
        list.add("小米手机");
        list.add("机器人");
        String product = stringProductGetter.getProduct(list);
        System.out.println(product);

        // 调用泛型的静态方法
        System.out.println("=====================");
        ProductGetter.printType(100,"事实上",true);

        // 泛型方法可变参数的调用
        System.out.println("=====================");
        ProductGetter.print(1,2,3,4,5);
        ProductGetter.print("abc","da","cc");

        //泛型通配符 ? 的使用
        System.out.println("=====================");
        stringProductGetter.setValue("测试");
        showProductValueAll(stringProductGetter);
        ProductGetter<Integer> integerProductGetter = new ProductGetter<>();
        integerProductGetter.setValue(200);
        showProductValueAll(integerProductGetter);

    }
}
