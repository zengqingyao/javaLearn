package com.zengqy.reflection;

import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        User user = new User();
        Class c1 = user.getClass(); //通过对象获取Class类
        Class c2 = Class.forName("com.zengqy.reflection.User"); //通过forName获取
        Class c3 = User.class; //直接通过类名,不会自动地初始化该Class对象


        //基本数据类型包装类里面有个TYPE可以获取对应类的Class
        Class<Integer> type = Integer.TYPE;
        Class<Boolean> type1 = Boolean.TYPE;
        System.out.println(type+" "+type1);

        System.out.println(c1+" : "+c1.hashCode());
        System.out.println(c2+" : "+c2.hashCode());
        System.out.println(c3+" : "+c3.hashCode());



        System.out.println("===========================================");
        //获得父类类型
        Class superclass = c1.getSuperclass();
        System.out.println(superclass);


        System.out.println("===========================================");
        //各种数据的class, 类、接口、数组、枚举、注解、基本数据类型、void
        Class<Object> objectClass = Object.class; //类
        Class<Comparable> comparableClass = Comparable.class; //接口的class
        Class<String[]> aClass1 = String[].class; //一维数组的class
        Class<int[][]> aClass = int[][].class; //二维数组的class
        Class<Override> overrideClass = Override.class; //注解的class
        Class<ElementType> elementTypeClass = ElementType.class; //枚举类的class
        Class<Integer> integerClass1 = int.class;
        Class<Integer> integerClass = Integer.class; //基本数据类型的class
        Class<Void> voidClass = void.class; //void的class

        System.out.println("Object class:       "+ objectClass);
        System.out.println("Comparable class:   "+comparableClass);
        System.out.println("String[] class:     "+aClass1);
        System.out.println("int[][] class:      "+aClass);
        System.out.println("Override class:     "+overrideClass);
        System.out.println("ElementType class:  "+elementTypeClass);
        System.out.println("int class:          "+integerClass1);
        System.out.println("integerClass class: "+integerClass);
        System.out.println("void class:         "+voidClass);




        System.out.println("====================类的加载器=======================");
        //获取系统类的加载器，也就是sun.misc.Launcher$AppClassLoader
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader);

        //获取获取扩展类加载器 sun.misc.Launcher$ExtClassLoader
        ClassLoader parent = systemClassLoader.getParent();
        System.out.println(parent);

        //根加载器无法获取
        ClassLoader parent1 = parent.getParent();
        System.out.println(parent1);

        //测试Reflection类是由谁加载的，也就是sun.misc.Launcher$AppClassLoader
        Class<?> aClass2 = Class.forName("com.zengqy.reflection.Reflection");
        ClassLoader classLoader = aClass2.getClassLoader();
        System.out.println(classLoader);

        //系统类加载器的路径
        System.out.println(System.getProperty("java.class.path"));



        
        System.out.println("===================类的获取方法和属性========================");
        //获取包名和类名
        System.out.println(c1.getName());
        System.out.println(c1.getSimpleName());

        //获取类的属性
        Field[] fields = c1.getFields(); //只能找到public属性
        Field[] declaredFields = c1.getDeclaredFields(); //获取全部的属性
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField);
        }

        System.out.println(c1.getDeclaredField("name")); //直接获取某个属性

        //获取类的方法
        System.out.println();
        Method[] methods = c1.getMethods(); //获得本类及其父类的public方法
        Method[] declaredMethods = c1.getDeclaredMethods(); //获取本类的所有方法
        for (Method declaredMethod : declaredMethods) {
            System.out.println(declaredMethod);
//            System.out.println(declaredMethod.getName()+":"+declaredMethod.getReturnType().getSimpleName());
        }

        //直接获取某个方法
        System.out.println("===================直接获取某个方法========================");
        Method getName = c1.getMethod("getName", null);
        System.out.println(getName);

        Method setName = c1.getMethod("setName", String.class);
        System.out.println(setName);

        //获取指定的构造器
        System.out.println("===================获取指定的构造器========================");
        Constructor[] constructor = c1.getConstructors(); //public方法的构造器
        for (Constructor constructor1 : constructor) {
            System.out.println(constructor1);
        }

        Constructor[] declaredConstructors = c1.getDeclaredConstructors(); // 所有访问权限的构造函数
        for (Constructor declaredConstructor : declaredConstructors) {
            System.out.println(declaredConstructor);
        }

        //直接获取指定的构造器
        Constructor declaredConstructor = c1.getDeclaredConstructor(String.class,int.class,int.class);
        System.out.println(declaredConstructor);

        System.out.println("\n===========================================");
        //动态创建对象
        User u1 = (User)c1.newInstance(); //本质上调用了无参构造器
        u1.setName("zengqy");
        System.out.println(u1);

        //有参的构造器获得对象
        Constructor co1 = c1.getDeclaredConstructor(String.class,int.class,int.class);
        User lijiay = (User)co1.newInstance("lijiay", 200, 999);
        System.out.println(lijiay);

        //通过反射获取某个方法
        Method setName1 = c1.getDeclaredMethod("setName", String.class);
        setName1.invoke(lijiay,"kuangshen");
        System.out.println(lijiay);

        Field name = c1.getDeclaredField("name");

        //因为name是private的，但是可以直接关闭程序的安全检测，方法和属性都一样操作
        //如果一个方法或者属性频繁调用，可以提高反射效率，同时关闭安全检测
        name.setAccessible(true);
        name.set(lijiay,"woshiren");
        System.out.println(lijiay);

        System.out.println("\n====================通过反射获取注解=======================");
        //通过反射获取注解
        Annotation[] annotations = c1.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }

        //获取注解的value值
        TableZengqy tableZengqy = (TableZengqy) c1.getAnnotation(TableZengqy.class);
        System.out.println(tableZengqy.value());

        Field f = c1.getDeclaredField("name");
        FieldZengqy annotation = f.getAnnotation(FieldZengqy.class);
        System.out.println(annotation.columnName());
        System.out.println(annotation.type());
        System.out.println(annotation.len());

    }
}


@TableZengqy("db_user")
class User{
    @FieldZengqy(columnName = "db_name",type = "varchar",len = 10)
    private String name;

    @FieldZengqy(columnName = "db_id",len = 16)
    private int id;

    @Testtable(name = {"abc","cde","sda"},age = 20)
    private int age;

    public User() {
    }

    public User(String name, int id, int age) {
        this.name = name;
        this.id = id;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", age=" + age +
                '}';
    }
}

//类名和属性的注解
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface TableZengqy{
    String value();//value为默认值，所以可以 @TableZengqy("db_user") 不用@TableZengqy(value = "db_user")
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface FieldZengqy{
    String columnName();
    String type() default "可以不写";
    int len();
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Testtable{
    String[] name();
    int age();
}
