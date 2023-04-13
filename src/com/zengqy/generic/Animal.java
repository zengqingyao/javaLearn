package com.zengqy.generic;

public class Animal {
    public String name;

    public Animal() {
    }

    public Animal(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                '}';
    }
}
