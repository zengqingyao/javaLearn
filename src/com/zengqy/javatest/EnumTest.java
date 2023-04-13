package com.zengqy.javatest;

/**
 * @包名: com.zengqy.javatest
 * @author: zengqy
 * @DATE: 2022/12/2 16:00
 * @描述:
 */
public class EnumTest {

    public static void main(String[] args) {

        System.out.println("===========自定义Enum类============");
        System.out.println(EnumCustomSample.SPRING);
        System.out.println(EnumCustomSample.NOVALUE);

        System.out.println("===========Enum类============");
        System.out.println(EnumSample.SPRING);

        // 遍历枚举对象
        for (EnumSample value : EnumSample.values()) {
            System.out.println(value+" name:"+value.name());
        }

        System.out.println("====================");
        EnumSample enumSample = EnumSample.AUTUMN;

        // 通过字符串获取枚举对象，字符串要有
        EnumSample enumSample1 = EnumSample.valueOf("AUTUMN");

        System.out.println("是同一个对象吗？"+ (enumSample==enumSample1));

        // AUTUMN - WINTER = -1    3-4 = -1
        System.out.println(enumSample.compareTo(EnumSample.WINTER));

        System.out.println(enumSample.name()); //输出枚举对象的名字
        System.out.println(enumSample.ordinal()); //输出枚举对象的编号 0 1 2 3


        switch (enumSample)
        {
            case SPRING:
                System.out.println(EnumSample.SPRING);
                break;
            case AUTUMN:
                System.out.println(EnumSample.AUTUMN);
                break;
            case WINTER:
                System.out.println(EnumSample.WINTER);
                break;
            default:
                System.out.println("default");
        }
    }

}


/**
 * 自定义模仿Eunm类的实现
 */
class EnumCustomSample {

    private String name;
    private String desc;

    public static final EnumCustomSample SPRING = new EnumCustomSample("春天", "温暖");
    public static final EnumCustomSample SUMMER = new EnumCustomSample("春天", "温暖");
    public static final EnumCustomSample AUTUMN = new EnumCustomSample("春天", "温暖");
    public static final EnumCustomSample WINTER = new EnumCustomSample("春天", "温暖");
    public static final EnumCustomSample NOVALUE = new EnumCustomSample();



    private EnumCustomSample() {
    }

    private EnumCustomSample(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "EnumCustomSample{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}

    /**
 * 枚举类的构造器只能使用private修饰，即使省略也默认是private的。
 */
enum EnumSample
{
    SPRING("春天","温暖"),SUMMER("夏天","炎热")
    ,AUTUMN("秋天","温暖"),WINTER("冬天","寒冷"),NOVALUE;
    private String name;
    private String desc;

    // 无参构造器可以省略参数输入 NOVALUE,
    EnumSample()
    {

    }

    EnumSample(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "EnumSample{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}