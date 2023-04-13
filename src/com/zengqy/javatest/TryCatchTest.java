package com.zengqy.javatest;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @包名: com.zengqy.javatest
 * @author: zengqy
 * @DATE: 2023/3/14 23:17
 * @描述: 学习try catch finally的测试类
 */
public class TryCatchTest {

    public static void testThrows () throws RuntimeException
    {
        throw new CustomRuntimeException("自定义的运行时异常");
    }


    public static void main(String[] args) {

        try {
            testThrows();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }


        System.out.println("=======================1. nextInt方法=======================");
        Scanner scanner = new Scanner(System.in);


        int num = 0;
        while (true)
        {
            System.out.print("请输入一个整数：");
            try {
                num = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("错误，你输入的不是整数！！");
                scanner.nextLine(); // 当输入的不是int型，数据会留在标准IO缓冲区里面，所以需要取出来
            }finally {
                // 每次都会执行
            }
        }
        System.out.println("你成功输入整数："+num);

        System.out.println("=======================2. nextLine方法=======================");
        while (true)
        {
            System.out.print("请输入一个整数：");
            String next = scanner.next();

            try {
                num = Integer.parseInt(next);
                break;
            } catch (NumberFormatException e) {
                System.out.println("错误，你输入的不是整数！！");
            }
        }
        System.out.println("你成功输入整数："+num);


        scanner.close();

    }

}



class CustomRuntimeException extends RuntimeException{
    public CustomRuntimeException(String message) {
        super(message);
    }
}

