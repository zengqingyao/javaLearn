package com.zengqy.sort;

import java.util.Random;


public class Sort {
    public static final int SIZE = 100;
    public static int compValue;
    public static int swapValue;

    //sdasd十大
    public static int[] getData() {
        int[] data = new int[SIZE];
        int i;
        Random rand = new Random();
        for (i = 0; i < data.length; i++) {
            data[i] = rand.nextInt(1000);
        }
        return data;
    }

    public static void showData(int[] data) {
        int i = 0;
        for (int tmp : data) {
            i++;
            System.out.printf("%d\t", tmp);
            if (i == 40) {
                System.out.printf("\n");
                i = 0;
            }
        }
        System.out.printf("\n");
    }

    public static void swapDataValue(int[] data, int i, int j) {
        swapValue++;
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }


    public static void insertionSort() {
        System.out.printf("\n==================================插入排序==============================\n");
        compValue = 0;
        swapValue = 0;

        System.out.printf("随机序列: \n");
        int[] data = getData();
        showData(data);

        int i, j, tmp;
        for (i = 1; i < data.length; i++) {
            tmp = data[i];
            for (j = i - 1; j >= 0; j--) {
                compValue++;
                if (tmp > data[j]) {
                    break;
                } else {
                    swapValue++;
                    data[j + 1] = data[j];
                }
            }
            swapValue++;
            data[j + 1] = tmp;
        }


        System.out.printf("\n");
        showData(data);
        System.out.printf("总共比较次数: %d\n", compValue);
        System.out.printf("总共交换次数: %d\n\n", swapValue);

    }

    public static void selectionSort() {
        System.out.printf("\n==================================选择排序==============================\n");
        compValue = 0;
        swapValue = 0;

        System.out.printf("随机序列: \n");
        int[] data = getData();
        showData(data);

        int i, j;
        for (i = 0; i < data.length; i++) {
            int min = i;
            for (j = i + 1; j < data.length; j++) {
                compValue++;
                if (data[j] <= data[min]) {
                    min = j;
                }
            }
            swapDataValue(data, i, min);
        }

        System.out.printf("\n");
        showData(data);
        System.out.printf("总共比较次数: %d\n", compValue);
        System.out.printf("总共交换次数: %d\n\n", swapValue);

    }


    public static void buddleSort() {
        System.out.printf("\n==================================冒泡排序==============================\n");
        compValue = 0;
        swapValue = 0;

        System.out.printf("随机序列: \n");
        int[] data = getData();
        showData(data);

        int i, k = 0;
        while (true) {
            boolean done = true;
            for (i = 0; i < data.length - 1 - k; i++) {
                compValue++;
                if (data[i] < data[i + 1])
                    continue;
                swapDataValue(data, i, i + 1);
                done = false;
            }
            if (done == true)
                break;
            k++;
        }


        System.out.printf("\n");
        showData(data);
        System.out.printf("总共比较次数: %d\n", compValue);
        System.out.printf("总共交换次数: %d\n\n", swapValue);

    }


    public static void m_quickSort(int[] data, int low, int high) {
        if (low > high)
            return;

        int i = low;
        int j = high;
        int pivot = data[low];
        while (i < j) {

            while (data[j] >= pivot && i < j) {
                compValue++;
                j--;
            }
            swapDataValue(data, i, j);

            while (data[i] <= pivot && i < j) {
                compValue++;
                i++;
            }
            swapDataValue(data, i, j);
        }
        data[i] = pivot;

        m_quickSort(data, low, i - 1);
        m_quickSort(data, i + 1, high);
    }


    public static void quickSort() {
        System.out.printf("\n==================================快速排序==============================\n");
        compValue = 0;
        swapValue = 0;

        System.out.printf("随机序列: \n");
        int[] data = getData();
        showData(data);

        m_quickSort(data, 0, data.length - 1);

        System.out.printf("\n");
        showData(data);
        System.out.printf("总共比较次数: %d\n", compValue);
        System.out.printf("总共交换次数: %d\n\n", swapValue);

    }

    public static void shellSort() {
        System.out.printf("\n==================================希尔排序==============================\n");
        compValue = 0;
        swapValue = 0;

        System.out.printf("随机序列: \n");
        int[] data = getData();
        showData(data);

        int i, j, dlta;
        for (dlta = data.length / 2; dlta >= 1; dlta /= 2)  //50 25 12 6 3 1 不断递减间隔
        {
            for (i = dlta; i < data.length; i++)  //从间隔dlta开始进行直接插入法
            {
                int tmp = data[i];
                for (j = i - dlta; j >= 0; j -= dlta) {
                    compValue++;
                    if (tmp >= data[j])  //找到需要插入的位置，注意需要需要插入的数据tmp存放的位置为j+data
                        break;

                    swapValue++;
                    data[j + dlta] = data[j]; //不断的往后移动
                }
                swapValue++;
                data[j + dlta] = tmp;
            }
        }

        System.out.printf("\n");
        showData(data);
        System.out.printf("总共比较次数: %d\n", compValue);
        System.out.printf("总共交换次数: %d\n\n", swapValue);

    }

    //二分法加上左边界 如 2 3 4 4 4 6
    public static int binarySearchLeftBound(int[] data, int target) {
        int left = 0;
        int right = data.length - 1;

        while (left <= right) {

            int mid = left + (right - left) / 2;
//            System.out.println("left:"+left+" right:"+right+" mid:"+mid);
            if (data[mid] == target) {
                right = mid - 1;
            } else if (data[mid] < target) {
                left = mid + 1;
            } else if (data[mid] > target) {
                right = mid - 1;
            }
        }

        if (left >= data.length || data[left] != target)
            return -1;

        return left;
    }

    public static int binarySearchRightBound(int[] data, int target) {
        int left = 0;
        int right = data.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (data[mid] == target) {
                left = mid + 1;
            } else if (data[mid] < target) {
                left = mid + 1;
            } else if (data[mid] > target) {
                right = mid - 1;
            }
        }

        if (data[right] != target)
            return -1;

        return right;
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
            throw new RuntimeException("参数不正确");
        }

        char[] chars = str.toCharArray();
        char tmp = ' ';
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

        int[] nums = {2, 4, 4, 4, 4, 4, 4, 5};
        System.out.println(binarySearchLeftBound(nums, 4));
        System.out.println(binarySearchRightBound(nums, 4));

        String str = "abcdef";
        System.out.println(stringReverse(str, 1, 4));

    }
}


