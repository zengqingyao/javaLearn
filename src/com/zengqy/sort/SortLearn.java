package com.zengqy.sort;

import java.util.Random;

/**
 * @包名: com.zengqy.sort
 * @author: zengqy
 * @DATE: 2023/8/21 17:46
 * @描述:
 */
public class SortLearn {

    public static int SIZE = 100;

    /**
     * 模板基类，无法被实例化
     */
    static abstract class TemplateSort {

        public abstract void startSort();

        public void caleSortTimes() {
            long start = System.currentTimeMillis();

            // 因为有动态绑定机制，所以运行的时候调用子类重写的方法。
            startSort();

            long time = System.currentTimeMillis() - start;
            System.out.println("\n执行任务花费了多少时间：" + time + "ms");
        }

    }


    static int[] getData(int size) {
        Random random = new Random();
        int[] data = new int[size];

        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt(size * 30);
        }
        return data;
    }


    static void showData(int[] data) {
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " ");
        }
    }


    /*
     *  直接插入法排序 时间复杂度:O(n^2)
     *  数据分为有序和无序两部分，tmp为无序里面的值，然后不断向前遍历比较，
     *  如果比前值小，则有序部分的值不断往后移。
     *  如果比前值大，则找到对应的位置然后+1设置为tmp。
     *
     */
    public static void insertionSort() {
        System.out.println("\n===============插入法排序==============");

        new TemplateSort() {
            @Override
            public void startSort() {

                int[] data = getData(SIZE);
                showData(data);
                int j;
                for (int i = 0; i < data.length; i++) {
                    int tmp = data[i];
                    for (j = i - 1; j >= 0; j--) {
                        if (tmp > data[j])
                            break;
                        data[j + 1] = data[j];
                    }
                    data[j + 1] = tmp;
                }

                System.out.println();
                showData(data);
            }

        }.caleSortTimes();
    }

    public static void swapDataValue(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }


    // 选择排序 时间复杂度:O(n^2)
    public static void selectionSort() {

        System.out.println("\n===============选择排序==============");
        new TemplateSort() {

            @Override
            public void startSort() {
                int[] data = getData(SIZE);
                showData(data);

                for (int i = 0; i < data.length; i++) {
                    int min = i;
                    for (int j = i + 1; j < data.length; j++) {
                        if (data[j] < data[min]) {
                            min = j;
                        }
                    }
                    int tmp = data[i];
                    data[i] = data[min];
                    data[min] = tmp;
                }

                System.out.println();
                showData(data);
            }

        }.caleSortTimes();
    }

    // 冒泡排序 时间复杂度为 O(n^2)
    public static void buddleSort() {
        System.out.println("\n===============冒泡排序==============");
        new TemplateSort() {
            @Override
            public void startSort() {
                int[] data = getData(SIZE);
                showData(data);

                for (int i = 0; i < data.length - 1; i++) {
                    for (int j = 0; j < data.length - 1 - i; j++) {
                        if (data[j] > data[j + 1]) {
                            int tmp = data[j];
                            data[j] = data[j + 1];
                            data[j + 1] = tmp;
                        }
                    }
                }

                System.out.println();
                showData(data);
            }
        }.caleSortTimes();

    }


    public static void realQuickSort(int[] data, int low, int high) {

        if (low > high)
            return;

        int i = low;
        int j = high;
        int pivot = data[low];

        while (i < j) {
            while (data[j] >= pivot && i < j)
                j--;

            swapDataValue(data, i, j);

            while (data[i] <= pivot && i < j)
                i++;

            swapDataValue(data, i, j);
        }

        data[i] = pivot;
        realQuickSort(data, low, i - 1);
        realQuickSort(data, i + 1, high);
    }

    // 快速排序 快速排序的平均时间复杂度和最坏时间复杂度分别是O(nlgn)、O(n^2)。
    public static void quickSort() {

        System.out.println("\n===============快速排序==============");
        new TemplateSort() {
            @Override
            public void startSort() {
                int[] data = getData(SIZE);
                showData(data);

                realQuickSort(data, 0, data.length - 1);

                System.out.println();
                showData(data);
            }
        }.caleSortTimes();
    }

    // 希尔排序 希尔排序的最坏时间复杂度是O(n^s) , 1<s<2,
    public static void shellSort() {
        System.out.println("\n===============希尔排序==============");


        new TemplateSort() {
            @Override
            public void startSort() {
                int[] data = getData(SIZE);
                showData(data);

                // 3 6 22 33 44     11

                int i, j, delta;

                // 从插入法排序改进过来的
                for (delta = data.length / 2; delta >= 1; delta /= 2) {  //50 25 12 6 3 1 不断递减间隔
                    for (i = delta; i < data.length; i++) { //从间隔dlta开始进行直接插入法
                        int tmp = data[i];
                        for (j = i - delta; j >= 0; j -= delta) {
                            if (tmp > data[j]) //找到需要插入的位置，注意需要需要插入的数据tmp存放的位置为j+data
                                break;
                            data[j + delta] = data[j]; //不断的往后移动
                        }
                        data[j + delta] = tmp;
                    }

                }
                System.out.println();
                showData(data);

            }
        }.caleSortTimes();
    }

    //二分法加上左边界 如 2, 4, 4, 4, 4, 4, 4, 5 ,5
    // https://blog.csdn.net/qq_42500831/article/details/130505600
    public static int binarySearchLeftBound(int[] data, int target) {

        int left = 0;
        int right = data.length - 1;

        int leftRange = -1;

        while (left <= right) {

            // 可以防止数值溢出，相对于 mid = (left+right)/2
            int mid = left + (right - left) / 2;

            if (data[mid] == target) {
                leftRange = mid; // 记录当前找到的值
                right = mid - 1; // 再次从左边找，看看能不能找到
            } else if (target < data[mid]) {
                right = mid - 1;
            } else if (target > data[mid]) {
                left = mid + 1;
            }
        }
        return leftRange;
    }

    public static int binarySearchRightBound(int[] data, int target) {
        int left = 0;
        int right = data.length - 1;
        int rightRange = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (data[mid] == target) {
                rightRange = mid;
                left = mid + 1;
            } else if (target > data[mid]) {
                left = mid + 1;
            } else if (target < data[mid]) {
                right = mid - 1;
            }
        }

        return rightRange;
    }


    /**
     * 字符串的逆序
     *
     * @param str
     * @param start
     * @param end
     * @return
     */
    public static String stringReverse(String str, int start, int end) {
        if (str == null || start < 0 || end > str.length()) {
            throw new RuntimeException("参数错误");
        }

        char[] chars = str.toCharArray();
        char tmp;
        for (int i = start, j = end; i < j; i++, j--) {
            tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
        }

        return new String(chars);
    }


    public static void main(String[] args) {

        insertionSort();
        selectionSort();
        buddleSort();
        quickSort();
        shellSort();

        int[] nums = {2, 3, 4, 4, 4, 4, 5, 8, 9, 9, 10};
        System.out.println(binarySearchLeftBound(nums, 4));
        System.out.println(binarySearchRightBound(nums, 4));

        String str = "123456789";
        System.out.println(stringReverse(str, 0, str.length()-1));

    }
}
