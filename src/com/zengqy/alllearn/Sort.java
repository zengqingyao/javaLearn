package com.zengqy.alllearn;

import java.util.Random;

/**
 * @包名: com.zengqy.alllearn
 * @author: zengqy
 * @DATE: 2023/9/12 19:04
 * @描述:
 */
public class Sort {

    private static Random mRandom = new Random();

    private static final int SIZE = 100;

    private static int[] getData(int size) {
        int[] data = new int[size];
        for (int i = 0; i < data.length; i++) {
            data[i] = mRandom.nextInt(size * 3);
        }
        return data;
    }


    static abstract class CalTime {

        abstract void startSort();

        void calTime() {
            long start = System.currentTimeMillis();
            startSort();
            long end = System.currentTimeMillis() - start;
            System.out.println("排序耗费：" + end + "ms");
        }
    }

    private static void showData(int[] data) {
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " ");
            if (i % 30 == 0 && i != 0) {
                System.out.println();
            }
        }
        System.out.println();
        System.out.println();
    }


    public static void main(String[] args) {


        new CalTime() {

            @Override
            void startSort() {
                int[] data = getData(SIZE);
                showData(data);

                int i, j;
                for (i = 1; i < data.length; i++) {
                    int tmp = data[i];
                    for (j = i - 1; j >= 0; j--) {

                        if (tmp >= data[j]) {
                            break;
                        }
                        data[j + 1] = data[j];
                    }
                    data[j + 1] = tmp;
                }

                showData(data);
            }
        }.calTime();


    }
}
