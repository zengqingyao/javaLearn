package com.zengqy.javatest;

import java.util.*;

/**
 * @包名: com.zengqy.javatest
 * @author: zengqy
 * @DATE: 2023/3/23 23:40
 * @描述: 集合相关类的使用
 */
public class SetAndList {

    /**
     * ArrayList的使用，底层是通过object数组，所以改查速度快，增删速度慢
     * 优点: 底层数据结构是数组，查询快，增删慢。
     * 缺点: 因为没有使用synchronized,，所以线程不安全，但是效率高
     * 底层实现：
     * ArrayList 底层是基于数组来实现容量大小动态变化的，默认初始容量为10，以1.5倍进行扩容
     * 当需要扩容的时候，调用Arrays.copyOf()来进行复制一个新的数组。
     *
     * @return
     */
    public static void runArrayListTest() {
        System.out.println("===============ArrayList的使用===============");
        ArrayList<String> list = new ArrayList<>();

        list.add("Item0");
        list.add("Item1");
        list.add("Item3");
        // 此条语句将会把“Item2”字符串增加到list的第3个位置。
        list.add(2, "Item2");
        System.out.println(list);


        int pos = list.indexOf("Item2");  // 检查元素的位置
        System.out.println("Item2的索引位置：" + pos + " size：" + list.size());


        boolean element = list.contains("Item5"); // 检查数组链表中是否包含某元素
        System.out.println("是否包含Item5某个参数？ " + element);

        list.remove(1);     // 移除第1个位置上的元素
        list.remove("Item3");  //移除第一次找到的 "Item3"元素

        System.out.println(list);


        //1. 第一种遍历方式
        System.out.print("第一种遍历方式使用size循环:                         ");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();

        //2. 第二种遍历方式 通过迭代器方式
        System.out.print("第二种遍历方式iterator迭代器:                       ");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.print(next + " ");
        }
        System.out.println();

        System.out.print("第三种遍历方式foreach方式（底层就是通过iterator迭代器）：");
        for (String string : list) {
            System.out.print(string + " ");
        }
        System.out.println();


        // 转换 ArrayList 为 Array
        // 1. 第一个重载方法，是将list直接转为Object[] 数组；
        System.out.println("第一种转化为Object[] 数组：");
        Object[] objects = list.toArray();
        for (Object object : objects) {
            String s = (String) object;
            System.out.println(s);
        }

        // 2. list转化为你所需要类型的数组
        System.out.println("第二种转化为对应的数组");
        String[] simpleArray = list.toArray(new String[list.size()]);
        for (String s : simpleArray) {
            System.out.println(s);
        }

        //Arrays.asList(strArray)返回值是java.util.Arrays类中一个私有静态内部类java.util.Arrays.ArrayList，
        // 它并非java.util.ArrayList类。java.util.Arrays.ArrayList类具有 set()，get()，contains()等方法，
        // 但是不具有添加add()或删除remove()方法,所以调用add()方法会报错。
        //使用场景：Arrays.asList(strArray)方式仅能用在将数组转换为List后，不需要增删其中的值，仅作为数据源读取使用。
        List<String> strings = Arrays.asList(new String[]{"abc", "abc1", "abc3"});


        System.out.println("\n===============ArrayList的使用===============");
    }


    /**
     * linkedList的使用，底层是通过双向链表，所以增删速度快
     * 优点: 底层数据结构是链表，查询慢，增删快。
     * 缺点: 因为没有使用synchronized,，所以线程不安全，但是效率高
     * 底层实现： 底层是使用双向链表实现的
     */
    public static void runLinkedList() {
        System.out.println("\n===============LinkedList的使用===============");
        LinkedList<String> list = new LinkedList<>();

        list.add("Item0");
        list.add("Item1");
        list.addFirst("first");
        list.addLast("last");

        System.out.println(list);

        list.remove();
        list.removeLast();


        // 遍历跟ArrayList一模一样
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.print(next + " ");
        }
        System.out.println();


        System.out.println("list转化为数组：");
        String[] strings = list.toArray(new String[list.size()]);
        for (String string : strings) {
            System.out.println(string);
        }


        System.out.println("===============LinkedList的使用===============");
    }


    /**
     * 底层数据结构是hashmap。(无序,唯一)
     * 如何来保证元素唯一性? 依赖两个方法：hashCode()和equals()
     * HashSet 基于 HashMap 来实现的，是一个不允许有重复元素的集合，允许有 null 值，是无序的，即不会记录插入的顺序。
     * HashSet 不是线程安全的， 如果多个线程尝试同时修改 HashSet，则最终结果是不确定的。 您必须在多线程访问时显式同步对 HashSet 的并发访问。
     */
    public static void runHashSet() {
        System.out.println("\n===============runHashSet的使用===============");
        HashSet<Employee> hashSet = new HashSet<>();
        hashSet.add(new Employee("zengqy", 20));
        hashSet.add(new Employee("lijiayao", 23));
        hashSet.add(new Employee("zengqy", 99));

        // 这个会加不进去，因为重写了 equals 和 hashcode
        hashSet.add(new Employee("zengqy", 20));


        System.out.print("1. 迭代器遍历方式：");
        Iterator<Employee> iterator = hashSet.iterator();
        while (iterator.hasNext()) {
            Employee next = iterator.next();
            System.out.print(next + " ");
        }
        System.out.println();

        System.out.print("2. foreach遍历方式：");
        for (Employee s : hashSet) {
            System.out.print(s + " ");
        }
        System.out.println();


        // 把 set转化为list
        System.out.print("3. Set转化为ArrayList遍历方式：");
        ArrayList<Employee> employees = new ArrayList<>(hashSet);
        for (int i = 0; i < employees.size(); i++) {
            System.out.print(employees.get(i) + " ");
        }


        System.out.println("\n把set转化为数组类型，再遍历");
        Employee[] employees1 = hashSet.toArray(new Employee[hashSet.size()]);
        for (Employee employee : employees1) {
            System.out.println(employee);
        }

        System.out.println("===============runHashSet的使用===============");
    }


    /**
     * 是hashset的子类
     * 底层数据结构是LinkedHashMap双向链表和哈希表。(FIFO插入有序,唯一)
     * 1.由链表保证元素有序
     * 2.由哈希表保证元素唯一
     */
    public static void runLinkedHashSet() {
        System.out.println("\n===============runLinkedHashSet的使用===============");

        LinkedHashSet<Employee> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add(new Employee("item0", 19));
        linkedHashSet.add(new Employee("item1", 20));
        linkedHashSet.add(new Employee("item2", 21));
        linkedHashSet.add(new Employee("item2", 22));

        // 会添加失败，因为重写了 equals 和 hashcode
        linkedHashSet.add(new Employee("item2", 21));


        Iterator<Employee> iterator = linkedHashSet.iterator();
        while (iterator.hasNext()) {
            Employee next = iterator.next();
            System.out.println(next);
        }


        System.out.println("===============runLinkedHashSet的使用===============");
    }


    /**
     * 底层数据结构是红黑树。(唯一，有序）所以的主要功能用于排序
     * 1. 如何保证元素排序的呢?
     * 自然排序
     * 比较器排序
     * 2.如何保证元素唯一性的呢?
     * 根据比较的返回值是否是0来决定
     */
    public static void runTreeSet() {
        System.out.println("\n===============runTreeSet的使用===============");

        // 默认有个比较器，会安装字符串升序来排序
        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.add("aaa");
        treeSet.add("eeeeeee");
        treeSet.add("bbbb");
        treeSet.add("cccccc");
        treeSet.add("dddddd");

        System.out.println(treeSet);


        // 按照字符串降序排列
        treeSet = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });

        treeSet.add("aaa");
        treeSet.add("eeeeeee");
        treeSet.add("bbbb");
        treeSet.add("cccccc");
        treeSet.add("dddddd");
        System.out.println(treeSet);


        // 按照字符串长度来排序
        // 如果返回值为0，则代表key相同,hashset有唯一性，底层treemap只会设置value的值，所以加入会失败
        treeSet = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        });

        treeSet.add("aaa");
        treeSet.add("eeeeeee");
        treeSet.add("bbbb");
        treeSet.add("dddddd");

        // 因为cccccc的length 跟dddddd一样，所以只会在原来的key上设置value值，key不变，故加入失败
        // return t.setValue(value);
        treeSet.add("cccccc");

        System.out.println(treeSet);

        System.out.println("===============runTreeSet的使用===============");
    }


    /**
     * Map接口有三个比较重要的实现类，分别是HashMap、TreeMap和HashTable。
     * <p>
     * 有序的：TreeMap
     * 无序的：HashMap(效率较高，可以添加null值)、HashTable（加了synchronized，所以线程安全的，
     * 所以效率较低，key和value不允许null值）
     */
    private static void runHashMap() {

        System.out.println("\n===============runHashMap的使用===============");

        HashMap<String, Employee> hashMap = new HashMap<>();
        hashMap.put("Item0", new Employee("item0", 20));
        hashMap.put("Item1", new Employee("item1", 30));
        hashMap.put("Item2", new Employee("item2", 40));
        hashMap.put("Item3", new Employee("item3", 50));

        //=============================entrySet遍历=============================
        // entrySet的存在就是为了方便遍历的
        Set<Map.Entry<String, Employee>> entries = hashMap.entrySet();
        Iterator<Map.Entry<String, Employee>> iterator = entries.iterator();
        System.out.println("iterator迭代器方式：");
        while (iterator.hasNext()) {
            Map.Entry<String, Employee> next = iterator.next();
            System.out.println("获取到的值: " + next.getKey() + " = " + next.getValue());
        }

        System.out.println("\nforeach方式，底层也是迭代器：");
        for (Map.Entry<String, Employee> entry : entries) {
            System.out.println("获取到的值: " + entry.getKey() + " = " + entry.getValue());
        }


        //=============================keySet遍历key的值=============================
        System.out.println("\nkeySet遍历key的值");
        Set<String> strings = hashMap.keySet();
        Iterator<String> iterator1 = strings.iterator();
        while (iterator1.hasNext()) {
            String next = iterator1.next();
            System.out.println("遍历key值：" + next);
        }

        //=============================values遍历key的值=============================
        System.out.println("\nvalues遍历values的值");
        Collection<Employee> values = hashMap.values();
        Iterator<Employee> iterator2 = values.iterator();
        while (iterator2.hasNext()) {
            Employee next = iterator2.next();
            System.out.println("遍历values的值：" + next);
        }


        System.out.println("===============runHashMap的使用===============");
    }


    /**
     * 底层数据结构是红黑树。(唯一，有序）所以的主要功能用于排序
     * 1. 如何保证元素排序的呢?
     * 自然排序
     * 比较器排序
     * 2.如何保证元素唯一性的呢?
     * 根据比较的返回值是否是0来决定
     */
    private static void runTreeMap() {
        System.out.println("\n===============runTreeMap的使用===============");

        // key 为自定义的Employee类，必须传入比较器Comparator，或者Employee类实现比较器
        TreeMap<String, Employee> treeMap = new TreeMap<>(new Comparator<String>() {

            /**
             * 根据key的长度从小到大排序
             * @param o1 为新插入的key的值，会跟已经存在的key值的长度进行比较
             * @param o2 旧的已经存在的key的值
             * @return 返回 0 则只会替换values
             */
            @Override
            public int compare(String o1, String o2) {
                int res = o1.length() - o2.length();

                // 如果返回0，则相当于替换values
                if (res == 0) {
                    res = 1;
                }

                return res;
            }
        });


        treeMap.put("Item0", new Employee("item0", 20));
        treeMap.put("Item222", new Employee("item2", 40));
        treeMap.put("Item11", new Employee("item1", 30));
        treeMap.put("Item333", new Employee("item3", 40));
        treeMap.put("Item44", new Employee("item4", 30));


        System.out.println("iterator迭代器方式：");
        Set<Map.Entry<String, Employee>> entries = treeMap.entrySet();
        Iterator<Map.Entry<String, Employee>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Employee> next = iterator.next();
            System.out.println("获取到的值: " + next.getKey() + " = " + next.getValue());
        }

        System.out.println("\nforeach方式，底层也是迭代器：");
        for (Map.Entry<String, Employee> entry : entries) {
            System.out.println("获取到的值: " + entry.getKey() + " = " + entry.getValue());
        }


        System.out.println("===============runTreeMap的使用===============");

    }


    /**
     * Collections工具类
     */
    private static void runCollections() {
        System.out.println("\n===============runCollections的使用===============");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("item222");
        arrayList.add("item00000");
        arrayList.add("item11");
        arrayList.add("item222");
        arrayList.add("item3333");


        //1.
        System.out.println("ArrayList排序前：" + arrayList);
        Collections.reverse(arrayList);
        System.out.println("ArrayList反转后：" + arrayList);

        //2.
        Collections.shuffle(arrayList);
        System.out.println("ArrayList随机排序后：" + arrayList);

        //3.
        Collections.swap(arrayList, 0, arrayList.size() - 1);
        System.out.println("ArrayList调换0和最后的：" + arrayList);

        //4.
        Collections.sort(arrayList);
        System.out.println("ArrayList默认排序，字符串从小到大：" + arrayList);

        //5.
        Collections.sort(arrayList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println("ArrayList自定义比较器，字符串从大到小：" + arrayList);


        // 就是字符串排序最大的那个值
        System.out.println("自然排序的最大值：" + Collections.max(arrayList));


        String max = Collections.max(arrayList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        });
        System.out.println("字符串长度最大的；" + max);


        System.out.println("item222出现的次数：" + Collections.frequency(arrayList, "item222"));

        Collections.replaceAll(arrayList, "item222", "替换后item222的值");
        System.out.println(arrayList);


        //========================================
        System.out.println("============hashset============");
        HashSet<Employee> hashSet = new HashSet<>();
        hashSet.add(new Employee("item0", 20));

        Collections.addAll(hashSet, new Employee("item1", 30), new Employee("item2", 40));

        Employee[] employees = new Employee[]{new Employee("item3", 50),
                new Employee("item4", 60)};


        Collections.addAll(hashSet,employees);

        System.out.println(hashSet);


        System.out.println("===============runCollections的使用===============");
    }


    public static void main(String[] args) {

        // collection接口 有 2个继承接口 list 和 set
        // list接口的实现


        // 1.
        runArrayListTest();
        runLinkedList();

        // 2.
        runHashSet();
        runLinkedHashSet();
        runTreeSet();

        // 3.
        runHashMap();
        runTreeMap();

        runCollections();
    }


}


class Employee {
    private String name;
    private int age;


    //=================================
    // 重写equals , hashmap添加的时候会调用判断是不是同一个对象
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return age == employee.age && Objects.equals(name, employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
    //=================================


    public Employee(String name, int age) {
        this.name = name;
        this.age = age;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}