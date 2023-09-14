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
 * @DATE: 2023/8/25 19:23
 * @描述: 手写一个线程池
 */

public class DiyThreadPool implements Executor {

    private AtomicInteger ctl = new AtomicInteger(0);
    private AtomicInteger rs = new AtomicInteger(RUNNING);

    private static final int RUNNING = -1;
    private static final int SHUTDOWN = 0;
    private static final int STOP = 1;

    private volatile int coolPoolSize;

    private volatile int maxiumPoolSize;

    private volatile int keepAliveTime;
    private TimeUnit timeUnit;

    BlockingQueue<Runnable> workQueue;

    List<Worker> workList = new CopyOnWriteArrayList<>();

    ReentrantLock mainLock = new ReentrantLock();


    public DiyThreadPool(int coolPoolSize, int maxiumPoolSize, int keepAliveTime, TimeUnit timeUnit, BlockingQueue<Runnable> workQueue) {
        this.coolPoolSize = coolPoolSize;
        this.maxiumPoolSize = maxiumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.workQueue = workQueue;
    }

    @Override
    public void execute(Runnable command) {

        if (rs.get() == SHUTDOWN || rs.get() == STOP) {
            throw new RuntimeException("线程池已经关闭，不能在提交任务");
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
        worker.thread.start();
        ctl.incrementAndGet();
        workList.add(worker);
        return true;
    }


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
                processWorkerExit(this);
            }

        }

        private Runnable getTask() {
            boolean timedOut = false;
            for (; ; ) {

                int state = rs.get();
                if (state == STOP || (state == SHUTDOWN && workQueue.isEmpty())) {
                    ctl.decrementAndGet();
                    return null;
                }

                int nums = ctl.get();
                boolean timed = nums > coolPoolSize;

                if ((nums > maxiumPoolSize) || (timed && timedOut)
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

    /**
     * 子线程退出了
     * 1. 需要在List里面移除掉
     * 2. 中断另外一个子线程，可以防止线程池退出后，一直卡在take()里面
     *
     * @param w
     */
    private void processWorkerExit(Worker w) {
        System.out.println(time() + " 子线程已经退出, " + w);
        workList.remove(w);


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

    public void shutDown() {
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


    public List<Runnable> shutdownNow() {
        mainLock.lock();
        try {
            rs.getAndSet(STOP);

            for (Worker worker : workList) {
                worker.thread.interrupt();
            }

            List<Runnable> list = new ArrayList<>();
            workQueue.drainTo(list);
            return list;

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
        DiyThreadPool pool = new DiyThreadPool(2, 4, 5, TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(2));


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

        //        List<Runnable> runnables = pool.shutdownNow();
        //        for (Runnable runnable : runnables) {
        //            System.out.println(time() + " " + runnable);
        //        }

        try {
            Thread.sleep(9000);
            pool.showState();
            pool.shutDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
