package com.zengqy.generic;

public class MiniCat extends Cat{

    public MiniCat() {
    }

    public MiniCat(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "MiniCat{" +
                "name='" + name + '\'' +
                '}';
    }
}
