package com.zengqy.javatest;

import java.util.Arrays;
import java.util.Scanner;


/**
 * @包名: com.zengqy.javatest
 * @author: zengqy
 * @DATE: 2023/3/10 16:12
 * @描述:
 */
public class ScannerInput {


    public static void main(String[] args) {

//        ~-2 计算机运算都是以补码（反码+1）形式进行运算，但是输出结果要看 原码
//        原码：1000000 00000000 00000000 00000010
//        反码：1111111 11111111 11111111 11111101
//        补码: 1111111 11111111 11111111 11111110
//        ~操作对补码取反,就是运算后的补码
//             00000000 00000000 00000000 00000001
//        因为最高位为0,为正数，所以补码跟原码一样，故值为1
        System.out.println("~-2=" + (~-2));

//        ~2的 正数的补码和反码都是一样的
//        原码：00000000 00000000 00000000 00000010
//        补码: 00000000 00000000 00000000 00000010
//        ~取反得到补码: 11111111 11111111 11111111 11111101
//        负数补码的反码（补码-1）是: 11111111 11111111 11111111 11111100
//        负数补码的原码是：         10000000 00000000 00000000 00000011 最终结果为-3
        System.out.println("~2=" + ~2);

        // 0.0~1.0之间的数
        double random = Math.random();
        System.out.println(random);
        lable:
        for (int j = 0; j < 10; j++) {
            lable2:
            for (int i = 0; ; i++) {
                int randomInt = (int) (Math.random() * 100);
//                System.out.println("随机值："+randomInt); // [0-100) 的值
                if (randomInt == 0) {
                    System.out.println("使用了" + i + "次才能随机到0");
                    break lable; //之间跳出最开始的for循环，如果是 break; 则等于 break lable2;
                }
            }
        }

        System.out.println("\n==================数组的遍历================");
        String[] arrys = {"曾庆耀", "sda", "ccc", "Sadas"};
        for (String arry : arrys) {
            System.out.println(arry);
        }

        for (int i = 0; i < arrys.length; i++) {
            System.out.println(arrys[i]);
        }
        System.out.println("==================数组的遍历================");

        String[] arrays = new String[5];
        arrays[0] = "0";
        arrays[2] = "2";
        arrays[4] = "4";
        for (int i = 0; i < arrays.length; i++) {
            System.out.println(arrays[i]);
        }
        System.out.println("==================================");


        char[] char26s = new char[26];
        for (int i = 0; i < char26s.length; i++) {
            char26s[i] = (char) ('A' + i);
        }
        for (char char26 : char26s) {
            System.out.print(char26);
        }
        System.out.println();


        int[] arragsInt = {1, 2, 3, 4, 5, 6, 7, 11};

        for (int i = 0, j = arragsInt.length - 1; i < j; i++, j--) {
            int tmp = arragsInt[j];
            arragsInt[j] = arragsInt[i];
            arragsInt[i] = tmp;
        }
        for (int i : arragsInt) {
            System.out.print(i + " ");
        }
        System.out.println();

        int[] arragsInt2 = {1, 2, 3, 4, 5, 6, 7, 11};
        int[] copyOf = Arrays.copyOf(arragsInt2, 3);
        for (int i : copyOf) {
            System.out.print(i + " ");
        }
        System.out.println();
        copyOf = Arrays.copyOfRange(arragsInt2, 1, 3);
        for (int i : copyOf) {
            System.out.print(i + " ");
        }


        System.out.println("\n===============二维数组===================");
        //二维数组里面
        int[][] arraysTest = {{1, 2, 3}, {1, 2}, {1}};
        int[][] twoArrays = new int[10][];
        for (int i = 0; i < twoArrays.length; i++) {
            twoArrays[i] = new int[i + 1];

            for (int i1 = 0; i1 < twoArrays[i].length; i1++) {
                twoArrays[i][i1] = i;
            }
        }

        for (int i = 0; i < twoArrays.length; i++) {
            for (int i1 = 0; i1 < twoArrays[i].length; i1++) {
                System.out.print(twoArrays[i][i1] + " ");
            }
            System.out.println();
        }


        Scanner scanner = new Scanner(System.in);

        // 只能获取不到空格的字符串
        System.out.print("\n请输入next：");
        System.out.println(scanner.next());

        // 获取int型的值，如果是其他的值会报错
        System.out.print("请输入nextInt：");
        System.out.println(scanner.nextInt());

        while (scanner.hasNextLine()) {
            System.out.println("你输入了：" + scanner.nextLine());
        }


    }
}

