package com.zengqy.net;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @包名: com.zengqy.net
 * @author: zengqy
 * @DATE: 2023/4/1 0:36
 * @描述:
 */
public class UrlTest {
    public static void main(String[] args) {

        try {
            URL url = new URL("http://loaclhost:8080/helloworld/index.jsp?username=xxx&password=123");

            System.out.println("getProtocol: "+url.getProtocol());
            System.out.println("getHost: "+url.getHost());
            System.out.println("getPort: "+url.getPort());
            System.out.println("getPath: "+url.getPath());
            System.out.println("getFile: "+url.getFile());
            System.out.println("getQuery: "+url.getQuery());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}
