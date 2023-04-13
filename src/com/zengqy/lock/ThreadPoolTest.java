package com.zengqy.lock;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class ThreadPoolTest {

    public static ConcurrentMap<String,String> concurrentMap = new ConcurrentHashMap<>();

    //创建一个100的线程池
    public static ExecutorService service = Executors.newFixedThreadPool(100);

    //必须执行完100个线程才继续主线程
    public static CountDownLatch countDownLatch = new CountDownLatch(100);


    //Callable执行线程，有返回值
    static FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
        @Override
        public String call() throws Exception {
            System.out.println("Callable 正在执行");
            return "is callable message";
        }
    });


    /**
     * 自定义线程池
     */
    public static void runThreadPool()
    {

        System.out.println("\n==============自定义实现线程池==================");
        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(2, 4,   //2个核心线程，最大线程数为4个
                        3, TimeUnit.SECONDS,        //最大空闲时间为3秒钟
                        new ArrayBlockingQueue<>(2));     //这里使用容量为2的ArrayBlockingQueue队列


        for (int i = 0; i < 6; i++) {   //开始6个任务
            int finalI = i;
            executor.execute(() -> {
                try {
                    System.out.println(Thread.currentThread().getName()+" 开始执行！（"+ finalI);
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName()+" 已结束！（"+finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        try {
            TimeUnit.SECONDS.sleep(1);    //看看当前线程池中的线程数量
            System.out.println("线程池中线程数量："+executor.getPoolSize());
            TimeUnit.SECONDS.sleep(5);     //等到超过空闲时间
            System.out.println("线程池中线程数量："+executor.getPoolSize());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        executor.shutdownNow();    //使用完线程池记得关闭，不然程序不会结束，它会取消所有等待中的任务以及试图中断正在执行的任务，关闭后，无法再提交任务，一律拒绝
        executor.shutdown();     //同样可以关闭，但是会执行完等待队列中的任务再关闭











        /**
         * corePoolSize：
         *  核心线程池大小，我们每向线程池提交一个多线程任务时，都会创建一个新的核心线程，
         *  无论是否存在其他空闲线程，直到到达核心线程池大小为止，之后会尝试复用线程资源。
         *  当然也可以在一开始就全部初始化好，调用prestartAllCoreThreads()即可。
         *
         * maximumPoolSize：
         *  最大线程池大小，当目前线程池中所有的线程都处于运行状态，并且等待队列已满，
         *  那么就会直接尝试继续创建新的非核心线程运行，但是不能超过最大线程池大小。
         *
         * keepAliveTime：
         *  线程最大空闲时间，当一个非核心线程空闲超过一定时间，会自动销毁。
         *
         * unit：
         *  线程最大空闲时间的时间单位
         *
         * workQueue：
         *  线程等待队列，当线程池中核心线程数已满时，就会将任务暂时存到等待队列中，
         *  直到有线程资源可用为止，这里可以使用我们上一章学到的阻塞队列。
         *
         * threadFactory：
         *  线程创建工厂，我们可以干涉线程池中线程的创建过程，进行自定义。
         *
         * handler：
         *  拒绝策略，当等待队列和线程池都没有空间了，真的不能再来新的任务时，
         *  来了个新的多线程任务，那么只能拒绝了，这时就会根据当前设定的拒绝策略进行处理。
         *

         * BlockingQueue阻塞队列
         *  ArrayBlockingQueue 里面的线程会排队等待，有容量限制
         *  SynchronousQueue 没有容量，就是无法把任务丢进等待队列里面，故会执行拒绝策略，默认是抛异常
         *  LinkedBlockingDeque 没有容量限制，也可以有容量限制，可以不断把任务丢进去，所以maximumPoolSize相当于不生效
         *
         *
         * 线程池的拒绝策略默认有以下几个：
         *
         * AbortPolicy(默认)：像上面一样，直接抛异常。
         * CallerRunsPolicy：直接让提交任务的线程运行这个任务，比如在主线程向线程池提交了任务，那么就直接由主线程执行。
         * DiscardOldestPolicy：丢弃队列中最近的一个任务，替换为当前任务。
         * DiscardPolicy：什么也不用做。
         */
        System.out.println("\n==============自定义实现线程池和返回值==================");
        ThreadPoolExecutor executor1 = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(2),
                new ThreadFactory() { // 创建线程的工程类

                    int count = 0;
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r,"自定义线程-"+ count++);
                    }
                },
                new ThreadPoolExecutor.AbortPolicy());


        Callable<String> cl = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread().getName()+" 子线程在运行...");
                Thread.sleep(1000);
                return "子线程的返回值，第一种方法";
            }
        };

        Future<String> res = executor1.submit(cl);

        try {
            System.out.println("1. 第一种方法获取线程的返回值："+executor1.submit(cl).get());
            System.out.println("2. 第一种方法获取线程的返回值："+res.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //FutureTask 只执行一次，多次无效
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread().getName()+" 子线程在运行...");
                Thread.sleep(2000);
                return "我是子线程的返回结果";
            }
        });

        executor1.submit(futureTask);

        try {
            System.out.println("2. 主线程获取到返回值："+futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executor1.shutdown();



        System.out.println("\n==============线程池定时执行任务==================");
        ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(2);

        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread().getName()+" 子线程在运行...");
                Thread.sleep(1000);
                return "这是子线程的返回值";
            }
        };

        // 2秒后才执行子线程
        ScheduledFuture<String> schedule = stpe.schedule(callable, 2, TimeUnit.SECONDS);

        try {
            System.out.println("主线程得到子线程的返回值："+schedule.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        stpe.shutdown();
    }







    public static void main(String[] args) {


        long start = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            service.execute(new Runnable() { //线程池执行，往map里面不断put值，完成后通知countDownLatch
                @Override
                public void run() {
                    for (int j = 0; j < 100; j++) {
                        concurrentMap.put(UUID.randomUUID().toString(),UUID.randomUUID().toString());
                    }
                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();

        System.out.println("Map的SIZE："+concurrentMap.size());
        Iterator<Map.Entry<String, String>> iterator = concurrentMap.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry<String, String> next =  iterator.next();
            System.out.println(i+++" KEY: "+next.getKey()+" Value:"+next.getValue());
        }


        //普通方式启动线程
        new Thread(futureTask).start();

        try {
            System.out.println("Thread: "+futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //线程池方式
        service.submit(futureTask);

        try {
            System.out.println("Submit: "+futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println(end-start+"ms");

        service.shutdown();

        runThreadPool();

    }
}
