package com.zengqy.lock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

public class Synchronized {

    int num;
    public static int staticNum = 0;

    //使用ReentrantLock锁取代synchronized
    private ReentrantLock lock = new ReentrantLock();

    //Condition对象来实现wait和notify的功能。
    // condition.await();
    // condition.signalAll();
    private Condition condition = lock.newCondition();


    public Synchronized(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    // 给静态方法加锁时，锁的是 Synchronized.class 对象;
    public synchronized static void isStaticSync() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("isStaticSync thread:" + Thread.currentThread().getId() + " ,num:" + staticNum++);
        }
    }

    //给普通方法加锁时，上锁的对象是 this
    public synchronized void isInstanceSync() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("isInstanceSync thread:" + Thread.currentThread().getId() + " ,num:" + num++);
        }
    }


    public void isInstanceSyncCode() {
        for (int i = 0; i < 1000; i++) {
            synchronized (this) {
                System.out.println("isInstanceSync thread:" + Thread.currentThread().getId() + " ,num:" + num++);
            }
        }
    }


    public void isReentranntLock() {
        for (int i = 0; i < 1000; i++) {
            lock.lock();
            System.out.println("isInstanceSync thread:" + Thread.currentThread().getId() + " ,num:" + num++);
            lock.unlock();
        }
    }


    //使用CAS来实现线程安全
    static AtomicInteger atomicInteger = new AtomicInteger(0);


    /**
     * 可重入锁和condition使用方法
     */
    public static void runReentrantLock() {


        System.out.println("\n================ReentrantLock使用方法====================");


        ReentrantLock lock2 = new ReentrantLock();
        lock2.lock();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread run:" + Thread.currentThread());
                lock2.lock();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread run:" + Thread.currentThread());
                lock2.lock();
            }
        });

        t1.start();
        t2.start();


        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("当前等待队列的线程数："+lock2.getQueueLength());
        System.out.println("是否有线程在等待队列中："+lock2.hasQueuedThreads());
        System.out.println("线程t1是否在等待队列中："+lock2.hasQueuedThread(t1));
        System.out.println("线程t2是否在等待队列中："+lock2.hasQueuedThread(t2));
        System.out.println("当前线程是否在等待队列中："+lock2.hasQueuedThread(Thread.currentThread()));


        //=============================================================
        System.out.println("\n=============Condition使用============");
        ReentrantLock lock1 = new ReentrantLock();
        Condition condition = lock1.newCondition();


        new Thread(()->{
            lock1.lock();
            try {
                System.out.println("子线程获取到锁，进入条件等待队列睡眠，等待被唤醒....");
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock1.unlock();
            }
        }).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lock1.lock();
        System.out.println("主线程，当前Condition的等待线程数："+lock1.getWaitQueueLength(condition));
        condition.signalAll();
        System.out.println("主线程，当前Condition的等待线程数："+lock1.getWaitQueueLength(condition));

        lock1.unlock();


    }


    /**
     * 读写锁使用方法
     */
    public static void runReentrantReadWriterLock()
    {
        Synchronized.staticNum = 0;

        System.out.println("\n===================ReentrantReadWriterLock===================");
        ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();

        // 锁降级，先申请写锁，再申请读锁后，如果释放写锁，则变为读锁，先后顺序不可以，就是锁升级不可以
        rwlock.writeLock().lock();
        rwlock.readLock().lock();
        System.out.println("主线程加读锁...");

        Thread t1 = new Thread(()->{

            // 主线程已经拿到了写锁，故这里会等待
            System.out.println("子线程等待获取到读锁..");
            rwlock.readLock().lock();
            Synchronized.staticNum++;
            System.out.println("子线程等待获取到读锁.."+Synchronized.staticNum);

            rwlock.readLock().unlock();
        });

        t1.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Synchronized.staticNum++;
        rwlock.writeLock().unlock();
        System.out.println("主线程写锁解锁.."+Synchronized.staticNum);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程读锁解锁.."+Synchronized.staticNum);
        rwlock.readLock().unlock();
    }


    /**
     * 原子类使用方法
     */
    public static void runAtomic()
    {
        System.out.println("\n=================AtomicInteger================");
        AtomicInteger atomicInteger = new AtomicInteger(0);
        System.out.println("先+1，然后返回值："+atomicInteger.incrementAndGet());
        System.out.println("先获取值，然后自增+1："+atomicInteger.getAndIncrement());
        System.out.println("先获取值，然后自减-1："+atomicInteger.getAndDecrement());
        System.out.println("先自减-1，然后获取值："+atomicInteger.decrementAndGet());



        System.out.println("直接+3，然后获取值："+atomicInteger.addAndGet(3));
        System.out.println("直接-1，然后获取值："+atomicInteger.addAndGet(-1));

        System.out.println("直接*2，然后获取值："+atomicInteger.updateAndGet(new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return operand*2;
            }
        }));

        // lambda表达式
        System.out.println("lambda表达式直接*10，然后获取值："+atomicInteger.updateAndGet(value -> value * 10));


        System.out.println("\n=================AtomicIntegerArray================");

        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[]{0,1,2,3});

        Runnable r = ()->{
            for (int i = 0; i < 10000; i++) {
                atomicIntegerArray.addAndGet(0,1);
            }
        };

        new Thread(r).start();
        new Thread(r).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("两个线程都对数组[0]的值进行+10000："+atomicIntegerArray.get(0));




        System.out.println("\n=================longAdder================");
        /**
         * JDK8.0后提供了DoubleAdder和LongAdder，在高并发的情况下相对于AtomicLong的性能更好
         * 因为LongAdder底层维护了一个数组，每个线程都只会对该数组的值修改，最后求和得到所有的值一次性加在value上
         */

        LongAdder longAdder = new LongAdder();
        Runnable rl = ()->{
            for (int i = 0; i < 100000; i++) {
                longAdder.add(1);
            }
        };

        for (int i = 0; i < 100; i++) {
            new Thread(rl).start();
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("longAdder 最后求和的值："+longAdder.sum());


        /**
         * 原子操作引用类型
         */
        System.out.println("\n=================AtomicReference================");
        String a = "hello";
        String b = "world";
        AtomicReference<String> atomicReference = new AtomicReference<>(a);
        atomicReference.compareAndSet(a,b);
        System.out.println("AtomicReference引用类型原子操作后的值："+atomicReference.get());


        /**
         * 原子操作某个类里面的int的属性的值，必须volatile表示
         */
        System.out.println("\n=================AtomicIntegerFieldUpdater================");
        class Student{
            volatile int age = 0;
        }

        Student student = new Student();
        AtomicIntegerFieldUpdater<Student> fieldUpdater =
                AtomicIntegerFieldUpdater.newUpdater(Student.class,"age");

        System.out.println("原子操作某个类的int的值+30："+fieldUpdater.addAndGet(student, 30));


    }






    /**
     * 使用阻塞队列进行生产-消费者模式
     */
    private static void runArrayBlockingQueue() {


        System.out.println("\n=================ArrayBlockingQueue================");


        BlockingQueue<Object> queue = new ArrayBlockingQueue<>(2);
        Runnable supplier = () -> {
            int i = 0;
            while (true){
                try {
                    String name = Thread.currentThread().getName();
                    System.err.println(time()+"生产者 "+name+" 正在准备餐品...");
                    TimeUnit.SECONDS.sleep(3);
                    System.err.println(time()+"生产者 "+name+" 已出餐！");
                    queue.put(new Object());
                    if(i++>1)
                        break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        };
        Runnable consumer = () -> {
            int i = 0;
            while (true){
                try {
                    String name = Thread.currentThread().getName();
                    System.out.println(time()+"消费者 "+name+" 正在等待出餐...");
                    queue.take();
                    System.out.println(time()+"消费者 "+name+" 取到了餐品。");
                    TimeUnit.SECONDS.sleep(4);
                    System.out.println(time()+"消费者 "+name+" 已经将饭菜吃完了！");

                    if(i++>1)
                        break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        };
        for (int i = 0; i < 3; i++) new Thread(supplier, "Supplier-"+i).start();
        for (int i = 0; i < 4; i++) new Thread(consumer, "Consumer-"+i).start();


        try {
            Thread.sleep(18*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("\n=================SynchronousQueue================");
        SynchronousQueue<String> queue1 = new SynchronousQueue<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    System.out.println("消费者等到中...");
                    System.out.println("消费者获取值到:"+queue1.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            System.out.println("生产者已经把值传过去..");
            //会阻塞，直到别的线程take了
            queue1.put("曾庆耀");
            System.out.println("生产者等待消费者获取完成..");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("\n=================SynchronousQueue================");
        /**
         * 跟SynchronousQueue的区别是，多了一个内部存储的容器，采用CAS操作性能更好
         * 跟LinkedBlockingDeque，有无限的容量，但内部基本都是用锁来实现的，性能没有那么好
         * LinkedTransferQueue就是集合上面的两种优点，有无限的容量，而且使用CAS来保证生产者和消费者的协调，性能更好
         */
        LinkedTransferQueue<String> queue2 = new LinkedTransferQueue<>();
        queue2.put("hello");
        queue2.put("world");
        queue2.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });

    }

    private static String time(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return "["+format.format(new Date()) + "] ";
    }














    public static void main(String[] args) {



        // o代表对象，offset代表变量的地址，newValue代表新的值
        // CAS机制当中使用了3个基本操作数：内存地址V，旧的预期值A，计算后要修改后的新值B
        // 循环先获得内存地址的值V，这个就是旧的预期值A，然后底层用一条CPU的原子指令，进行比较和设置
//        sun/misc/ Unsafe.java
//        public final int getAndSetInt(Object o, long offset, int newValue) {
//            int v;
//            do {
//                v = getIntVolatile(o, offset);
//            } while (!compareAndSwapInt(o, offset, v, newValue));
//            return v;
//        }


//        for (int i = 0; i < 3; i++) {
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (atomicInteger.get() < 1000) {
//                        System.out.println("Thread name:" + Thread.currentThread().getName() + " :"
//                                + atomicInteger.incrementAndGet());
//                    }
//                }
//            });
//            t.start();
//        }

        Synchronized aSynchronized = new Synchronized(0);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                Synchronized.isStaticSync();
//                aSynchronized.isInstanceSync();
//                aSynchronized.isInstanceSyncCode();
                aSynchronized.isReentranntLock();
            }
        });

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
//                Synchronized.isStaticSync();
//                aSynchronized.isInstanceSync();
//                aSynchronized.isInstanceSyncCode();
                aSynchronized.isReentranntLock();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
//                Synchronized.isStaticSync();
//                aSynchronized.isInstanceSync();
//                aSynchronized.isInstanceSyncCode();
                aSynchronized.isReentranntLock();
            }
        });

        thread.start();
        thread1.start();
        thread2.start();

        // 主线程等待子线程执行完毕
        try {
            thread.join();
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        runReentrantLock();

        runReentrantReadWriterLock();
        runAtomic();

        runArrayBlockingQueue();

    }

}
