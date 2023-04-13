package com.zengqy.generic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Generic<T> {

    private T ID;
    private T ids;

    public T getID() {
        return ID;
    }

    public void setID(T ID) {
        this.ID = ID;
    }

    //泛型方法
    public static <T extends List> T getList(T t)
    {
        return t;
    }


    public static void main(String[] args) throws Exception{


        System.out.println("==============通配符的上下限制==============");
        // 通配符的上下限制
        ArrayList<Animal> animals = new ArrayList<>();
        ArrayList<Cat> cats = new ArrayList<>();
        ArrayList<MiniCat> miniCats = new ArrayList<>();
        ArrayList<MiniCat2> miniCats2 = new ArrayList<>();

        //addAll因为本来就有泛型的通配符上限，所以只能加入传Cat及其子类
        miniCats.add(new MiniCat("MiniCat1"));
        miniCats.add(new MiniCat("MiniCat2"));

        cats.addAll(miniCats);

//        showObject(animals);
        showUpperLimit(cats);
        showUpperLimit(miniCats);
        showUpperLimit(miniCats2);

        showLowerLimit(animals);
        showLowerLimit(cats);
        showLowerLimit(miniCats);
//        showLowerLimit(miniCats2);
        System.out.println("==============通配符的上下限制==============");



        Generic<String> stringGeneric = new Generic<>();
        stringGeneric.setID("zengqy");
        System.out.println(stringGeneric.getID());

//        Class<? extends Generic> clz = stringGeneric.getClass();

        Class<Generic> clz = Generic.class;
        Field[] declaredFields = clz.getDeclaredFields();

        for (Field declaredField : declaredFields) {
            System.out.println(declaredField.getName()+":"+declaredField.getType().getSimpleName());
        }
        System.out.println("============================");
        Method[] declaredMethods = clz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            System.out.println(declaredMethod.getName()+":"+declaredMethod.getReturnType().getSimpleName());
        }
        System.out.println("============================");

        Class<InfoImpl> infoClass = InfoImpl.class;
        for (Method declaredMethod : infoClass.getDeclaredMethods()) {
            System.out.println(declaredMethod.getName()+":"+declaredMethod.getReturnType().getSimpleName());
        }
        for (Field declaredField : infoClass.getDeclaredFields()) {
            System.out.println(declaredField.getName()+":"+declaredField.getType().getSimpleName());
        }

        System.out.println("============================");
        //泛型数组 泛型数组不可以new， 错误：ArrayList<String>[] strlist = new ArrayList<String>[5];
        ArrayList<String>[] strlist = new ArrayList[5];

        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("zengqy");

        strlist[0] = stringArrayList;
        System.out.println(strlist[0].get(0));
        System.out.println("============================");

        Class<Cat> catClass = Cat.class;
        Constructor<Cat> constructor = catClass.getConstructor();
        Cat cat = constructor.newInstance();


    }


    /**
     * 因为设置了泛型的通配符上限，所以只能传Cat及其子类，不能调用list.add添加
     * 类型擦除，extends，代码编译为字节码后，会把通配符T改为上限 Cat，如果没有extends则为object
     * @param list
     */
    public static void showUpperLimit(ArrayList<? extends Cat > list)
    {
        System.out.println("\n================showUpperLimit start==================");
        for (int i = 0; i < list.size(); i++) {
            System.out.println("showUpperLimit："+list.size()+" "+list.get(i));
        }
        System.out.println("================showUpperLimit end==================\n");
    }

    /**
     * 因为设置了泛型的通配符下限，所以只能传MiniCat及其父类，可以list.add 添加 MiniCat及其子类
     * 类型擦除，代码编译为字节码后，会把通配符T改为object
     * @param list
     */
    public static void showLowerLimit(ArrayList<? super MiniCat> list)
    {
        System.out.println("\n================showLowerLimit start==================");
//        list.add(new MiniCat());
//        list.add(new MiniCat2());
        for (int i = 0; i < list.size(); i++) {
            System.out.println("showLowerLimit："+list.size()+" "+list.get(i));
        }
        System.out.println("================showLowerLimit start==================\n");
    }

}


class InfoImpl implements Info<String>
{
    private int ID;
    private int ID1;

    public void show()
    {
        return ;
    }

    @Override
    public String info(String s) {
        return s;
    }
}