package com.zengqy.javatest;

import java.util.Arrays;


public class CatTest extends AnimalTest{


    {
        System.out.println("CatTest 的普通代码块被执行，先调用代码块，再调用构造器");
    }


    //因为先定义，所以先调用getStaticValue()，在调用下面的静态代码块
    private static int staticTest = getStaticValue();

    /**
     * 什么时候进行类加载？
     * 1. new 对象实例的时候，父类也会进行加载
     * 2. 调用类的 静态属性或者静态方法、
     *
     * 静态代码块和静态属性根据定义顺序来执行。
     */
    static {
        System.out.println("CatTest 的静态代码块被执行，类加载的时候执行，只执行一次");
    }

    private static int getStaticValue() {
        System.out.println("CatTest的静态方法被调用了，返回100");
        return 100;
    }

    //==========================动态绑定测试===============================
    public int dynamicValue = 20;

    @Override
    public int getDynamicValue() {
        return dynamicValue;
    }

//    @Override
//    public int dynamicTest1()
//    {
//        return dynamicValue+50;
//    }

    @Override
    public int dynamicTest2()
    {
        return dynamicValue+30;
    }
    //==========================动态绑定测试===============================



    public CatTest() {
    }

    public CatTest(int age, String name, char[] sex) {
        super(age, name, sex);
    }


    /**
     * 向上转型就是，父类的引用 = 子类的对象
     * 重写父类的方法，返回值是Object类的的子类就可以，例如 String char[]（数组是对象）
     * @param a
     * @return
     */
    @Override
    public char[] overrideTest(int a) {
        System.out.println("调用 CatTest 的 overrideTest方法");
        return null;
    }



    public static void main(String[] args) {
        CatTest catTest = new CatTest(19, "毛毛", new char[]{'公'});
        System.out.println(catTest);
        catTest.overrideTest(100);


        System.out.println("\n============多态============");
        AnimalTest animalTest = new AnimalTest(20,"试试","公".toCharArray());
        //调用父类的 overrideTest
        animalTest.overrideTest(20);

        //调用子类的 overrideTest
        System.out.println();
        animalTest = catTest; // 向上转型
        animalTest.overrideTest(30);
        System.out.println("判断是否是AnimalTest的子类？"+ (animalTest instanceof AnimalTest));

        System.out.println();
        animalTest = new DogTest(19, "很好", new char[]{'母'});
        animalTest.overrideTest(30);
        System.out.println("判断是否是AnimalTest的子类？"+ (animalTest instanceof AnimalTest));


        System.out.println("====================动态绑定=======================");

        AnimalTest animalTest1 = new CatTest();

        // 假如子类没有重写，调用父类的方法，父类的方法里面调用子类复写的方法，对应的属性也会变为子类的实绩值
        // 例如CatTest的dynamicTest1方法不重写，所以，
        // 会调用AnimalTest.dynamicTest1 -----> 子类的 CatTest.getDynamicValue() -->返回值为20
        System.out.println(animalTest1.dynamicTest1());
        System.out.println(animalTest1.dynamicTest2());

    }
}


class DogTest extends AnimalTest{

    {
        System.out.println("DogTest 的普通代码块被执行，先调用代码块，再调用构造器");
    }

    static {
        System.out.println("DogTest 的静态代码块被执行，类加载的时候执行，只执行一次");
    }

    public DogTest() {
    }

    public DogTest(int age, String name, char[] sex) {
        super(age, name, sex);
    }


    /**
     * 重写父类的方法，返回值是Object类的的子类就可以，例如 String char[]（数组是对象）
     * @param a
     * @return
     */
    @Override
    public String overrideTest(int a) {
        System.out.println("调用 DogTest 的 overrideTest方法");
        return null;
    }
}



class AnimalTest {
    private int age;
    private String name;
    private char[] sex;


    {
        System.out.println("AnimalTest 的普通代码块被执行，先调用代码块，再调用构造器");
    }

    static {
        System.out.println("AnimalTest 的静态代码块被执行，类加载的时候执行，只执行一次");
    }

    /**
     * 重写，方法名和参数列表类型必须一致，返回值是重写前的类Object的子类就可以
     * 子类不能缩小父类的访问权限
     * @param a
     * @return
     */
    public Object overrideTest(int a)
    {
        System.out.println("调用 AnimalTest 的 overrideTest方法");
        return null;
    }



    //==========================动态绑定测试===============================
    public int dynamicValue = 10;

    public int getDynamicValue() {
        return dynamicValue;
    }

    public int dynamicTest1()
    {
        return getDynamicValue()+20;
    }

    public int dynamicTest2()
    {
        return dynamicValue+30;
    }
    //==========================动态绑定测试===============================








    public AnimalTest() {
    }

    public AnimalTest(int age, String name, char[] sex) {
        this.age = age;
        this.name = name;
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char[] getSex() {
        return sex;
    }

    public void setSex(char[] sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "AnimalTest{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", sex=" + Arrays.toString(sex) +
                '}';
    }

}

