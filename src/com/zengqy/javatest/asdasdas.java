package com.zengqy.javatest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @包名: com.zengqy.javatest
 * @author: zengqy
 * @DATE: 2023/9/7 19:33
 * @描述:
 */
public class asdasdas {

    private static String time(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return "["+format.format(new Date()) + "] ";
    }


    public static void main(String[] args) throws InterruptedException {

        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(time()+"start");
                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        pool.scheduleAtFixedRate(r,0,2000, TimeUnit.MILLISECONDS);

        pool.shutdown();


        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        queue.put("asd");
        queue.put("asdsa");
        queue.put("asdsa");


        Iterator<String> iterator = queue.iterator();
        while (iterator.hasNext())
        {
            System.out.println(iterator.next());
        }


    }
}
