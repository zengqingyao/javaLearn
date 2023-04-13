package com.zengqy.javatest;

/**
 * @包名: com.zengqy.javatest
 * @author: zengqy
 * @DATE: 2023/3/10 21:45
 * @描述:
 *  当类有abstract抽象方法的时候，该类必须是抽象类，抽象类不能被实例化，等于c++里面的抽象类，因为包含纯虚函数
 *  abstract跟private final static明显冲突，无法一起用
 */
public class StudyClass {

    private int age;
    private String name;

    public StudyClass() {

    }

    public StudyClass(int age)
    {
        this.age = age;
    }

    public StudyClass(int age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    public String toString() {
        return "StudyClass{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * 可变参数，本质上是数组 int[] nums，可变参数只有一个，而且必须放在最右边
     * @param nums
     * @return
     */
    public static int sum(String name,int ... nums)
    {
        int total = 0 ;
        for (int num : nums) {
            System.out.print(num+" ");
            total+=num;
        }
        System.out.println();
        System.out.println("name："+name+" ，总和："+total);
        return total;
    }


    public static void main(String[] args) {

        StudyClass.sum("zengqy",1, 2, 3, 4);
        StudyClass.sum("my",new int[]{1,2,3,5});


        StudyClass studyClass = new StudyClass(19, "曾庆耀");
        System.out.println(studyClass);

        new AbstractTestChild(20).eat();



        LambaTest lambaTest = new LambaTest() {
            @Override
            public void test(int age, String name) {
                System.out.println("匿名内部类: age:"+age+" name:"+name);
            }
        };
        lambaTest.test(30,"zengqy");
        System.out.println(lambaTest.getClass());

        LambaTest lambaTest1 = (age,name)->{
            System.out.println("lamba表达式: age:"+age+" name:"+name);
            return;
        };
        lambaTest1.test(22,"zengqy2");
        System.out.println(lambaTest1.getClass());


    }
}

class AbstractTestChild extends AbstractTestBase{

    public AbstractTestChild(int age) {
        super(age);
    }

    /**
     * 必须重写抽象类的方法
     */
    @Override
    public void eat() {
        System.out.println("AbstractTestChild 的eat方法被调用");
    }
}


/**
 * 当类有abstract抽象方法的时候，该类必须是抽象类，抽象类不能被实例化，等于c++里面的抽象类，因为包含纯虚函数
 * abstract跟private final static明显冲突，无法一起用
 */
abstract class AbstractTestBase{
    public int age;

    abstract public void eat();

    public AbstractTestBase(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}


interface LambaTest{

    // public static final
    public static final String name = "zengqy";

    void test(int age,String name);



}