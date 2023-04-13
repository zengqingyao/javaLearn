package com.zengqy.javatest;

import java.util.ArrayList;
import java.util.Random;

public class Generic<T> {
    private T a;
    Random random = new Random();

    public T getA() {
        return a;
    }

    public void setA(T a) {
        this.a = a;
    }

    public Generic(T a) {
        this.a = a;
    }

    public Generic() {

    }

    /**
     *
     * @param list
     * @param <E>
     * @return
     */
    public <E> E getProduct(ArrayList<E> list)
    {
        return list.get(random.nextInt(list.size()));
    }

    public static void showGeneric(Generic<?> generic)
    {
        Object a = generic.getA();
        System.out.println(a);
    }

    /**
     * 泛型的上限通配符，传递的集合类型上限只能为String或其子类
     * @param generic
     */
    public static void showGenericExtends(Generic<? extends String> generic)
    {
        String a = generic.getA();
        System.out.println(a);
    }

    public static void main(String[] args) {

        Generic<String> stringGeneric = new Generic<String>();
        stringGeneric.setA("zengqy");
        System.out.println(stringGeneric.getA());


        ArrayList<String> list = new ArrayList<>();
        list.add("QWE");
        list.add("WER");
        list.add("CXD");

        String product = stringGeneric.getProduct(list);
        System.out.println(product);

        System.out.println("==============================");
        Generic<Integer> integerGeneric = new Generic<>();
        integerGeneric.setA(999);

        showGenericExtends(stringGeneric);
        showGeneric(integerGeneric);
        showGeneric(stringGeneric);


    }
}
