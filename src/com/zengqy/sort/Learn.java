package com.zengqy.sort;


import java.util.Random;

/**
 * @包名: com.zengqy.sort
 * @author: zengqy
 * @DATE: 2023/8/28 15:51
 * @描述:
 */
public class Learn {

    private static Random mRandom = new Random();
    private static int SIZE = 100;

    private static int[] getData(int size) {
        int[] nums = new int[size];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = mRandom.nextInt(size * 20);
        }
        return nums;
    }

    private static void show(int[] nums) {

        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + " ");
            if (i % 25 == 0 && i != 0) {
                System.out.println();
            }
        }
        System.out.println();
        System.out.println();
    }


    static abstract class TemplateSort {
        abstract void startSort();

        public void caleTime() {
            long l = System.currentTimeMillis();
            startSort();
            System.out.println("排序耗时：" + (System.currentTimeMillis() - l) + "ms");
        }
    }

    private static void swapData(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }


    private static void insertionSort() {

        new TemplateSort() {

            @Override
            void startSort() {

                int[] data = getData(SIZE);
                show(data);

                // 2 5 6 23   9
                int j;
                for (int i = 1; i < data.length; i++) {
                    int tmp = data[i];
                    for (j = i - 1; j >= 0; j--) {
                        if (tmp >= data[j]) {
                            break;
                        }
                        data[j + 1] = data[j];
                    }
                    data[j + 1] = tmp;
                }

                show(data);
            }
        }.caleTime();

    }

    private static void selectionSort() {

        new TemplateSort() {

            @Override
            void startSort() {
                int[] data = getData(SIZE);
                show(data);

                for (int i = 0; i < data.length; i++) {
                    int min = i;
                    for (int j = i; j < data.length; j++) {
                        if (data[min] > data[j]) {
                            min = j;
                        }
                    }
                    int tmp = data[i];
                    data[i] = data[min];
                    data[min] = tmp;
                }


                show(data);
            }
        }.caleTime();

    }

    private static void buddleSort() {
        new TemplateSort() {

            @Override
            void startSort() {
                int[] data = getData(SIZE);
                show(data);


                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < data.length - 1 - i; j++) {
                        if (data[j] > data[j + 1]) {
                            int tmp = data[j];
                            data[j] = data[j + 1];
                            data[j + 1] = tmp;
                        }
                    }
                }


                show(data);
            }
        }.caleTime();
    }

    private static void shellSort() {
        new TemplateSort() {

            @Override
            void startSort() {
                int[] data = getData(SIZE);
                show(data);

                int i, j, delta;

                for (delta = data.length / 2; delta >= 1; delta /= 2) {
                    for (i = delta; i < data.length; i++) {
                        int tmp = data[i];
                        for (j = i - delta; j >= 0; j -= delta) {
                            if (tmp >= data[j]) {
                                break;
                            }
                            data[j + delta] = data[j];
                        }
                        data[j + delta] = tmp;
                    }

                }
                show(data);
            }
        }.caleTime();
    }

    private static void m_quickSort(int[] nums, int low, int high) {
        if (low > high)
            return;

        int i = low;
        int j = high;
        int pivot = nums[low];

        while (i < j) {
            while (nums[j] >= pivot && i < j) j--;
            swapData(nums, i, j);

            while (nums[i] <= pivot && i < j) i++;
            swapData(nums, i, j);

        }
        nums[i] = pivot;

        m_quickSort(nums, low, i - 1);
        m_quickSort(nums, i + 1, high);

    }

    private static void quickSort() {
        new TemplateSort() {

            @Override
            void startSort() {
                int[] data = getData(SIZE);
                show(data);

                m_quickSort(data, 0, data.length - 1);

                show(data);
            }
        }.caleTime();
    }


    private static int binarySearchLeftBound(int[] data, int target) {

        int left = 0;
        int right = data.length - 1;
        int leftRange = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (target == data[mid]) {
                leftRange = mid;
                right = mid - 1;
            } else if (target < data[mid]) {
                right = mid - 1;
            } else if (target > data[mid]) {
                left = mid + 1;
            }
        }

        return leftRange;
    }

    private static int binarySearchRightBound(int[] data, int target) {

        int left = 0;
        int right = data.length - 1;
        int rightRange = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (data[mid] == target) {
                rightRange = mid;
                left = mid + 1;
            } else if (target < data[mid]) {
                right = mid - 1;
            } else if (target > data[mid]) {
                left = mid + 1;
            }
        }

        return rightRange;
    }

    private static String stringReverse(String str, int start, int end) {
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
