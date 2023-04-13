package com.zengqy.generic;

public class Cat extends Animal{

    public Cat() {
    }

    public Cat(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                '}';
    }
}
