package com.zengqy.design_pattern.learn;

/**
 * @包名: com.zengqy.design_pattern.learn
 * @author: zengqy
 * @DATE: 2023/8/28 14:51
 * @描述: 建造者模式，简单类型
 */
public class Build {


    public static void main(String[] args) {

        Student build = Student.builder()
                .setAge(100)
                .setName("231")
                .build();

        System.out.println(build);


    }
}

class Student {
    private int age;
    private String name;

    public Student(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public static BuildStudent builder() {
        return new BuildStudent();
    }


    public static class BuildStudent {
        private int age;
        private String name;


        public BuildStudent setName(String name) {
            this.name = name;
            return this;
        }

        public BuildStudent setAge(int age) {
            this.age = age;
            return this;
        }

        public Student build() {
            return new Student(age, name);
        }
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
        return "Student{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}

