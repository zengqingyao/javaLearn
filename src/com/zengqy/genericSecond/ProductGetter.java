package com.zengqy.genericSecond;

import java.util.ArrayList;
import java.util.Random;

/**
 * @包名: com.zengqy.genericSecond
 * @author: zengqy
 * @DATE: 2022/12/1 12:00
 * @描述: 一个抽奖池
 */
public class ProductGetter<T> {

    static Random random = new Random();

    // 奖品
    private T product;


    // 泛型通配符测试
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    // 奖品池
    ArrayList<T> mList = new ArrayList<>();

    /**
     * 添加奖品
     * @param t 奖品
     */
    public void addProduct(T t)
    {
        mList.add(t);
    }


    /**
     * 获取奖品
     * @return
     */
    public T getProduct()
    {
        return mList.get(random.nextInt(mList.size()));
    }


    /**
     * 定义泛型方法
     * @param list
     * @param <E>
     * @return
     */
    public <E> E getProduct(ArrayList<E> list)
    {
        return list.get(random.nextInt(list.size()));
    }

    /**
     * 静态的泛型方法
     * @param t
     * @param e
     * @param k
     * @param <T>
     * @param <E>
     * @param <K>
     */
    public static <T,E,K> void printType(T t,E e,K k)
    {
        System.out.println(t+"\t"+t.getClass().getSimpleName());
        System.out.println(e+"\t"+e.getClass().getSimpleName());
        System.out.println(k+"\t"+k.getClass().getSimpleName());
    }


    /**
     * 泛型可变参数的定义
     * @param e
     * @param <E>
     */
    public static <E> void print(E... e)
    {
        for (int i = 0; i < e.length; i++) {
            System.out.println(e[i]);
        }
    }

}
