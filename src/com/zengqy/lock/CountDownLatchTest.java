package com.zengqy.lock;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {

    private final static Random random = new Random();

    static class SearchTask implements Runnable
    {
        private Integer id;
        private CountDownLatch latch;

        public SearchTask(Integer id, CountDownLatch latch) {
            this.id = id;
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println("开始寻找"+id+"号龙珠");

            int seconds = random.nextInt(10);

            try {
                Thread.sleep(seconds*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("花了"+seconds+"s,找到了"+id+"号龙珠");
            latch.countDown();
        }
    }


    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(7);

        for (int i = 0; i < 7; i++) {
            Thread thread = new Thread(new SearchTask(i,countDownLatch));
            thread.start();
        }


        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("已经找到7龙珠，可以召唤神龙");
    }
}
