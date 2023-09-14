package com.zengqy.io;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @包名: com.zengqy.io
 * @author: zengqy
 * @DATE: 2023/8/25 15:39
 * @描述:
 */
public class IoLearn {
    public static File srcFile = new File("src\\com\\zengqy\\io\\dolby_test.ts");


    static abstract class CalTimes {

        public abstract String start();

        public void calCpTime() {
            long start = System.currentTimeMillis();

            String res = start();

            long times = System.currentTimeMillis() - start;
            System.out.println(res + " 花费了" + times + " ms");
        }

    }


    public static void main(String[] args) {

        new CalTimes() {
            @Override
            public String start() {
                try (FileInputStream fis = new FileInputStream(srcFile);
                     FileOutputStream fos = new FileOutputStream(srcFile + ".fis")) {

                    byte[] bytes = new byte[1024];
                    int rlen;
                    while ((rlen = fis.read(bytes)) != -1) {
                        fos.write(bytes, 0, rlen);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "拷贝" + srcFile.length() / 1024 / 1024 + "M";
            }
        }.calCpTime();


        new CalTimes() {
            @Override
            public String start() {
                try (FileInputStream fis = new FileInputStream(srcFile);
                     FileOutputStream fos = new FileOutputStream(srcFile + ".bis");
                     BufferedInputStream bis = new BufferedInputStream(fis);
                     BufferedOutputStream bos = new BufferedOutputStream(fos)) {

                    byte[] bytes = new byte[1024];
                    int rlen;
                    while ((rlen = bis.read(bytes)) != -1) {
                        bos.write(bytes, 0, rlen);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "拷贝" + srcFile.length() / 1024 / 1024 + "M";
            }
        }.calCpTime();


        new CalTimes() {

            @Override
            public String start() {

                try (FileInputStream fis = new FileInputStream(srcFile);
                     FileOutputStream fos = new FileOutputStream(srcFile + ".niocp");
                     FileChannel src = fis.getChannel();
                     FileChannel dest = fos.getChannel();
                ) {

                    // pos 是 src的pos ，然后拷贝多少字节到dest上
//                    src.transferTo(0, src.size(), dest);

                    // pos 是 dest 的pos ，然后拷贝多少count放在pos上
                    dest.transferFrom(src, 0, src.size());


                } catch (Exception e) {
                    e.printStackTrace();
                }


                return "拷贝" + srcFile.length() / 1024 / 1024 + "M";
            }
        }.calCpTime();


    }
}
