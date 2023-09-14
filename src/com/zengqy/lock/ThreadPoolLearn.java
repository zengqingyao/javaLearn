package com.zengqy.lock;

import java.util.concurrent.*;

/**
 * @包名: com.zengqy.lock
 * @author: zengqy
 * @DATE: 2023/8/25 19:01
 * @描述:
 */
public class ThreadPoolLearn {
    public static void main(String[] args) throws Exception {


        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                int num = 0;
                return new Thread(r, "zengqy" + num++);
            }
        }, new ThreadPoolExecutor.AbortPolicy());


        for (int i = 0; i < 10; i++) {
            executor.execute(()->{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }




        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("子线程开始执行...");
                Thread.sleep(3000);
                System.out.println("子线程执行完成...");
                return "子线程执行完后的返回值";
            }
        };

        Future<String> res = executor.submit(callable);

        Thread.sleep(1000);

        System.out.println("主线程取消子线程");
//        res.cancel(true);
        System.out.println(res.get());

        executor.shutdown();

        System.out.println("================================");
        ScheduledThreadPoolExecutor executor1 = new ScheduledThreadPoolExecutor(2);
        executor1.schedule(callable,2,TimeUnit.SECONDS);


    }
}
