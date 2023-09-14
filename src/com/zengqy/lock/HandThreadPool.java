package com.zengqy.lock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @包名: com.zengqy.lock
 * @author: zengqy
 * @DATE: 2023/8/26 11:33
 * @描述: 手写线程池，复习
 */
public class HandThreadPool implements Executor {

    // 当前线程数量
    private AtomicInteger ctl = new AtomicInteger(0);

    // 线程池状态
    private AtomicInteger rs = new AtomicInteger(RUNNING);

    private static final int RUNNING = -1;
    private static final int SHUTDOWN = 0;
    private static final int STOP = 1;


    // 核心线程数
    private volatile int coolPoolSize;

    // 非核心线程数
    private volatile int maxiumPoolSize;

    // 非核心线程存活时间
    private volatile int keepAliveTime;
    private TimeUnit timeUnit;

    // 存放 任务的 阻塞队列
    private BlockingQueue<Runnable> workQueue;

    // 主锁
    private ReentrantLock mainLock = new ReentrantLock();

    private List<Worker> workList = new CopyOnWriteArrayList<>();

    public HandThreadPool(int coolPoolSize, int maxiumPoolSize, int keepAliveTime,
                          TimeUnit timeUnit, BlockingQueue<Runnable> workQueue) {

        if (maxiumPoolSize < coolPoolSize || keepAliveTime < 0)
            throw new IllegalArgumentException("线程池的最大线程数不能小于核心线程数");

        this.coolPoolSize = coolPoolSize;
        this.maxiumPoolSize = maxiumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.workQueue = workQueue;
    }


    /**
     * 提交任务
     *
     * @param command
     */
    @Override
    public void execute(Runnable command) {

        if (rs.get() == SHUTDOWN || rs.get() == STOP) {
            throw new RuntimeException("Error, msg: 线程池已经关闭，不能提交任务了");
        }

        if (ctl.get() < coolPoolSize) {
            if (!addWorker(command, true)) {
                reject();
            }
            return;
        }

        if (!workQueue.offer(command)) {
            if (!addWorker(command, false)) {
                reject();
            }
        }

    }


    private boolean addWorker(Runnable command, boolean isCore) {

        if (ctl.get() >= (isCore ? coolPoolSize : maxiumPoolSize)) {
            return false;
        }

        Worker worker = new Worker(command);
        ctl.incrementAndGet();
        worker.thread.start();
        workList.add(worker);
        return true;
    }


    /**
     * 启动线程，然后去阻塞队列里面获取任务并且运行
     */
    private class Worker implements Runnable {

        Thread thread;
        Runnable firstTask;

        public Worker(Runnable firstTask) {
            this.firstTask = firstTask;
            thread = new Thread(this);
        }

        @Override
        public void run() {
            Runnable task = firstTask;
            try {
                while (task != null || (task = getTask()) != null) {
                    task.run();
                    task = null;
                }
            } finally {
//                System.out.println(time() + " 子线程已经退出, " + this);
                workList.remove(this);

                if (rs.get() == SHUTDOWN) {
                    if (workQueue.isEmpty() && ctl.get() != 0) {
                        for (Worker worker : workList) {
                            if (!worker.thread.isInterrupted()) {
                                worker.thread.interrupt();
                                break;
                            }
                        }
                    }
                }
            }

        }

        private Runnable getTask() {

            boolean timedOut = false;

            for (; ; ) {
                int state = rs.get();

                // 当SHUTDOWN，且阻塞队列为空的时候，就可以返回null从而退出线程了
                if (state == STOP || (state == SHUTDOWN && workQueue.isEmpty())) {
                    ctl.decrementAndGet();
                    return null;
                }


                // 如果大于核心线程数，则需要超时控制
                int nums = ctl.get();
                boolean timed = nums > coolPoolSize;

//                System.out.println(time() + "线程数:" + ctl.get() + " timed:" + timed + " timeout:"
//                        + timedOut + " 队列数:" + workQueue.size());
                if (((nums > maxiumPoolSize) || (timed && timedOut))
                        && (nums > 1 || workQueue.isEmpty())) {
                    if (ctl.compareAndSet(nums, nums - 1)) {
                        return null;
                    }
                    continue;
                }

                try {
                    Runnable r = timed ? workQueue.poll(keepAliveTime, timeUnit) : workQueue.take();
                    if (r != null) {
                        return r;
                    }
                    timedOut = true;
                } catch (InterruptedException e) {
                    System.out.println(time() + " 被中断了，再去查看能否获取到task " + rs.get());
                    timedOut = false;
                }
            }
        }

    }


    // 停止接收新任务，原来的任务继续执行
    //    1. 停止接收新的submit的任务；
    //    2. 已经提交的任务（包括正在跑的和队列中等待的）,会继续执行完成；
    private void shutdown() {
        mainLock.lock();
        try {
            rs.getAndSet(SHUTDOWN);

            for (Worker worker : workList) {
                worker.thread.interrupt();
            }

        } finally {
            mainLock.unlock();
        }
    }

//  1、根据JDK文档描述，大致意思是：执行该方法，线程池的状态立刻变成STOP状态，
//    并试图停止所有正在执行的线程，不再处理还在池队列中等待的任务，当然，它会返回那些未执行的任务。
//  2、它试图终止线程的方法是通过调用Thread.interrupt()方法来实现的，但是大家知道，这种方法的作用有限，
//  如果线程中没有sleep 、wait、Condition、定时锁等应用, interrupt()方法是无法中断当前的线程的。
//  所以，ShutdownNow()并不代表线程池就一定立即就能退出，它可能必须要等待所有正在执行的任务都执行完成了才能退出。

    //    跟 shutdown() 一样，先停止接收新submit的任务；
//    忽略队列里等待的任务；
//    尝试将正在执行的任务interrupt中断；
//    返回未执行的任务列表；
    private List<Runnable> shutDownNow() {
        mainLock.lock();
        try {
            rs.getAndSet(STOP);

            for (Worker worker : workList) {
                worker.thread.interrupt();
            }

            List<Runnable> res = new ArrayList<>();
            workQueue.drainTo(res);
            return res;

        } finally {
            mainLock.unlock();
        }
    }


    private void reject() {
        throw new RuntimeException(time() + "阻塞队列已满, 线程数:" + ctl.get() + " 队列:" + workQueue.size());
    }

    private static String time() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return "[" + format.format(new Date()) + "] " + Thread.currentThread().getName() + " ";

    }

    public int showState() {
        System.out.println(time() + "========= 线程数:" + ctl.get() + " 队列数:" + workQueue.size()
                + " 任务数:" + workList.size());
        return ctl.get();
    }

    public static void main(String[] args) {


        HandThreadPool pool = new HandThreadPool(2, 4, 5, TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(4));


        for (int i = 0; i < 8; i++) {
            int finalI = i;

            try {
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(time() + "子线程执行任务ing：" + finalI);

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
//                            System.out.println(time() + "任务" + finalI + " 被中断 " + this);
                        }
                        System.out.println(time() + "子线程结束任务：" + finalI);
                    }
                });
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }

        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Runnable> runnables = pool.shutDownNow();
        for (Runnable runnable : runnables) {
            runnable.run();
            System.out.println(time() + " " + runnable);
        }

        try {
            Thread.sleep(6000);
            pool.showState();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


}
