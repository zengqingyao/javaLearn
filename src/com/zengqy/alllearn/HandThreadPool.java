package com.zengqy.alllearn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @包名: com.zengqy.alllearn
 * @author: zengqy
 * @DATE: 2023/9/12 15:14
 * @描述: 手写线程池
 */
public class HandThreadPool implements Executor {

    private AtomicInteger ctl = new AtomicInteger(0);

    private AtomicInteger rs = new AtomicInteger(RUNNING);

    private static int RUNNING = -1;
    private static int SHUTDOWN = 0;
    private static int STOP = 1;

    private volatile int coolPoolsize;
    private volatile int maxiumPoolsize;
    private volatile int keepAliveTime;
    TimeUnit timeUnit;
    BlockingQueue<Runnable> mQueue;

    CopyOnWriteArrayList<Worker> mList = new CopyOnWriteArrayList<>();

    ReentrantLock mainLock = new ReentrantLock();

    public HandThreadPool(int coolPoolsize, int maxiumPoolsize, int keepAliveTime, TimeUnit timeUnit, BlockingQueue<Runnable> queue) {
        this.coolPoolsize = coolPoolsize;
        this.maxiumPoolsize = maxiumPoolsize;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        mQueue = queue;
    }


    @Override
    public void execute(Runnable command) {
        if (rs.get() == SHUTDOWN || rs.get() == STOP) {
            throw new RuntimeException("线程池已经关闭");
        }

        if (ctl.get() < coolPoolsize) {
            if (!addWork(command, true)) {
                reject();
            }
            return;
        }

        if (!mQueue.offer(command)) {
            if (!addWork(command, false)) {
                reject();
            }
        }

    }

    private boolean addWork(Runnable command, boolean isCore) {
        if (ctl.get() >= (isCore ? coolPoolsize : maxiumPoolsize)) {
            return false;
        }
        Worker worker = new Worker(command);
        worker.thread.start();
        ctl.incrementAndGet();
        mList.add(worker);
        return true;
    }


    private class Worker implements Runnable {

        Runnable firstTask;
        Thread thread;

        public Worker(Runnable firstTask) {
            this.firstTask = firstTask;
            thread = new Thread();
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

            boolean timedout = false;
            for (; ; ) {

                int state = rs.get();
                if (state == STOP || (state == SHUTDOWN && mQueue.isEmpty())) {
                    ctl.decrementAndGet();
                    return null;
                }


                int nums = ctl.get();

                boolean timed = nums >= coolPoolsize ? true : false;

                if ((nums > maxiumPoolsize || (timed && timedout))
                        && (nums > 1 || mQueue.isEmpty())) {
                    if (ctl.compareAndSet(nums, nums - 1)) {
                        return null;
                    }
                    continue;
                }

                try {
                    Runnable r = timed ? mQueue.poll(keepAliveTime, timeUnit) : mQueue.take();
                    if (r != null) {
                        return r;
                    }
                    timedout = true;
                } catch (InterruptedException e) {
                    timedout = false;
                }
            }
        }
    }

    private void processWorkerExit(Worker worker) {
        mList.remove(worker);

        if (rs.get() == SHUTDOWN) {
            if (mQueue.isEmpty() && ctl.get() > 0) {
                for (Worker worker1 : mList) {
                    if (!worker1.thread.isInterrupted()) {
                        worker1.thread.interrupt();
                        break;
                    }
                }
            }
        }
    }

    public void shutDown() {

        mainLock.lock();
        try {
            rs.set(SHUTDOWN);
            for (Worker worker : mList) {
                worker.thread.interrupt();
            }

        } finally {
            mainLock.unlock();
        }
    }

    public List<Runnable> shutDownNow() {
        mainLock.lock();
        try {
            rs.set(STOP);
            for (Worker worker : mList) {
                worker.thread.interrupt();
            }

            ArrayList<Runnable> list = new ArrayList<>();

            mQueue.drainTo(list);

            return list;
        } finally {
            mainLock.unlock();
        }
    }


    private void reject() {
    }

    public static void main(String[] args) {
        HandThreadPool threadPool = new HandThreadPool(2, 4, 5, TimeUnit.SECONDS
                , new LinkedBlockingDeque<>(2));

        threadPool.execute(new Runnable() {
            @Override
            public void run() {

            }
        });

    }
}
