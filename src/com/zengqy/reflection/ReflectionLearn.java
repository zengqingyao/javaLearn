package com.zengqy.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @包名: com.zengqy.reflection
 * @author: zengqy
 * @DATE: 2023/8/25 14:14
 * @描述:
 */
public class ReflectionLearn {

    public static void main(String[] args) throws Exception {

        Class<Person> clz = Person.class;
        Class clz2 = Class.forName("com.zengqy.reflection.Person");
        Class<? extends Person> clz3 = new Person().getClass();
        Class<?> clz4 = new Person().getClass().getClassLoader().loadClass("com.zengqy.reflection.Person");


        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName() + " 类型：" + field.getType());
        }


        Person person = clz.newInstance();
        Constructor<Person> constructor = clz.getDeclaredConstructor(int.class, String.class);
        Person lijiayao = constructor.newInstance(100, "lijiayao");
        System.out.println(lijiayao);


        Field age = clz.getDeclaredField("age");
        age.setAccessible(true);
        System.out.println("反射获取到值：" + age.get(lijiayao));
        age.set(lijiayao, 20);
        System.out.println(lijiayao);

        Field name1 = clz.getDeclaredField("name");
        name1.setAccessible(true);
        name1.set(lijiayao, "zengqy");
        System.out.println(lijiayao);

        Field money = clz.getDeclaredField("money");
        money.set(null, 999);
        System.out.println(Person.money);


        Method show = clz.getDeclaredMethod("show", String.class, int.class);
        show.invoke(null, "打印", 20000);

        Method setName = clz.getDeclaredMethod("setName", String.class);
        setName.invoke(lijiayao, "设置");

        Method getName = clz.getDeclaredMethod("getName");
        System.out.println(getName.invoke(lijiayao));


        System.out.println("=============注解的值=============");
        ClassPerson annotation = clz.getAnnotation(ClassPerson.class);
        System.out.println(annotation.value());
        System.out.println(annotation.getClass());
        System.out.println(annotation.annotationType().getName());

    }


}


@ClassPerson("需要反射的类")
class Person {

    @FieldPerson(name = "年龄", age = 20)
    private int age;

    public String name;

    public static int money = 100;

    public static String show(String name, int age) {
        System.out.println("name: " + name + "age: " + age);
        return "zengqy";
    }


    public Person() {
    }

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
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

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface FieldPerson {
    String name();

    int age();

    String type() default "可以不写";
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface ClassPerson {
    String value();
}