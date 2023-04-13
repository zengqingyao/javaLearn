package com.zengqy.genericSecond;

/**
 * @包名: com.zengqy.genericSecond
 * @author: zengqy
 * @DATE: 2022/12/1 11:33
 * @描述:
 */
public interface Generator<T> {
    T getKey();
    void setKey(T key);
}

