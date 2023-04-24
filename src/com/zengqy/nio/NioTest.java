package com.zengqy.nio;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * @包名: com.zengqy.nio
 * @author: zengqy
 * @DATE: 2023/4/23 20:49
 * @描述:  nio三大组件，buffer,channel,selector(也就是多路复用)
 */
public class NioTest {


    /**
     * buffer类的介绍
     * ByteBuffer   byte类型
     * ShortBuffer  short类型
     * IntBuffer    int类型
     * LongBuffer   long类型
     * FloatBuffer  float类型
     * DoubleBuffer double类型
     * CharBuffer   char 类型
     */
    public static void testBuffer()
    {
        System.out.println("\n=====================testBuffer======================");
        // 1. 初始化有2种方式
        int[] nums = new int[]{1,2,3,4,5};

        // pos = 0; limit = 5; cap = 5;
        IntBuffer buf = IntBuffer.wrap(nums);
        IntBuffer allocateBuf = IntBuffer.allocate(10);

        // 2. get 和 put方法修改值，每次操作都会使pos的值增加
        System.out.println("buf的第一个值："+buf.get());
        System.out.println("buf的第二个值："+buf.get());
        System.out.println("buf的第三个值被修改："+buf.put(33));
        System.out.println("buf的值："+Arrays.toString(buf.array()));

        // 3. 标记和还原 ，标记此时pos的位置
        buf.mark();

        // 4. 读取剩下未读出来的值
        while (buf.hasRemaining())
        {
            System.out.println("buf剩余有"+buf.remaining()+"个内容未被读："+buf.get());

        }
        // 3. 标记和还原 ，还原上面标记的位置
        buf.reset();


        // pos此时为3，然后limit = pos; pos = 0;所以allocateBuf.put会把前3个int值都加入
        buf.flip();
        allocateBuf.put(buf);
        System.out.println("allocateBuf的值："+Arrays.toString(allocateBuf.array()));

        // 5. 重新设置pos=0
        buf.rewind();

        // 重新设置pos=0 和limit=cap的值，这时候put，就会把整个wrap都加进去
        buf.clear();
        allocateBuf.clear();
        allocateBuf.put(buf);
        System.out.println(Arrays.toString(allocateBuf.array()));

        // 比较的是剩余的remaining的值
//        allocate.equals();

    }

    /**
     * 零拷贝文件
     */
    public static void testCpFile()
    {
        System.out.println("\n=====================testCpFile======================");

        File srcFile = new File("src\\com\\zengqy\\io\\dolby_test.ts");
        File desFile = new File("src\\com\\zengqy\\io\\dolby_test_cpchannel.ts");

        // jdk1.7后，在try()里面的只要实现java.io.Closeable接口的流，会自动关闭
        try (FileInputStream fis = new FileInputStream(srcFile);
             FileOutputStream fos = new FileOutputStream(desFile);
             FileChannel srcChannel = fis.getChannel();
             FileChannel desChannel = fos.getChannel()){

            long start = System.currentTimeMillis();

            //1. 发送到 desChannel
            srcChannel.transferTo(0,srcChannel.size(),desChannel);

            //2. desChannel 请求 srcChannel发送，这两种方式都可以
//            desChannel.transferFrom(srcChannel,0,srcChannel.size());

            long time = System.currentTimeMillis()-start;
            System.out.println("使用 FileChannel ，零拷贝拷贝文件成功，耗时: "+time+"ms");

        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("\n=====================testRandomAccessFile======================");
        // RandomAccessFile 支持channel读写的操作
        try(RandomAccessFile f = new RandomAccessFile("src\\com\\zengqy\\io\\raf.txt","rw");
            FileChannel fileChannel = f.getChannel()) {

            fileChannel.write(ByteBuffer.wrap("a这是修改前的b".getBytes()));
            System.out.println("fileChannel操作后的pos位置："+fileChannel.position());
            System.out.println("fileChannel操作后的size位置："+fileChannel.size());

            // 从新把pos设置为0
            fileChannel.position(0);

            ByteBuffer allocate1 = ByteBuffer.allocate((int)fileChannel.size());
            fileChannel.read(allocate1);
            allocate1.flip();
            System.out.println("读取文件内容："+new String(allocate1.array(), 0, allocate1.remaining()));

            // 直接
            //
            int pos = "a这是修改".getBytes().length;
            MappedByteBuffer mbb = fileChannel.map(FileChannel.MapMode.READ_WRITE,pos,"后".getBytes().length);
            mbb.put("后".getBytes());
            mbb.force();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * 常见的IO多路复用模型，也就是Selector的实现方案，比如现在有很多个用户连接到我们的服务器：
     *
     * select：当这些连接出现具体的某个状态时，只是知道已经就绪了，但是不知道详具体是哪一个连接已经就绪，
     *         每次调用都进行线性遍历所有连接，时间复杂度为O(n)，并且存在最大连接数限制。
     * poll：  同上，但是由于底层采用链表，所以没有最大连接数限制。
     * epoll：
     *    采用事件通知方式，当某个连接就绪，能够直接进行精准通知（
     *    这是因为在内核实现中epoll是根据每个fd上面的callback函数实现的，只要就绪会会直接回调callback函数，
     *    实现精准通知，但是只有Linux支持这种方式），时间复杂度O(1)，Java在Linux环境下正是采用的这种模式进行实现的。
     */
    public static void testSelector() {
        System.out.println("\n=====================testSelector======================");

        try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
             //开启一个新的Selector，这玩意也是要关闭释放资源的
            Selector selector = Selector.open()){

            serverChannel.bind(new InetSocketAddress(8080));

            //要使用选择器进行操作，必须使用非阻塞的方式，这样才不会像阻塞IO那样卡在accept()，而是直接通过，让选择器去进行下一步操作
            serverChannel.configureBlocking(false);

            //将选择器注册到ServerSocketChannel中，后面是选择需要监听的时间，只有发生对应事件时才会进行选择，
            // 多个事件用 | 连接，注意，并不是所有的Channel都支持以下全部四个事件，可能只支持部分
            //因为是ServerSocketChannel这里我们就监听accept就可以了，等待客户端连接

            //SelectionKey.OP_CONNECT --- 连接就绪事件，表示客户端与服务器的连接已经建立成功

            //SelectionKey.OP_ACCEPT --- 接收连接事件，表示服务器监听到了客户连接，服务器可以接收这个连接了

            //SelectionKey.OP_READ --- 读 就绪事件，表示通道中已经有了可读的数据，可以执行读操作了

            //SelectionKey.OP_WRITE
            //   --- 写 就绪事件，表示已经可以向通道写数据了（这玩意比较特殊，
            //   一般情况下因为都是可以写入的，所以可能会无限循环）
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {   //无限循环等待新的用户网络操作
                //每次选择都可能会选出多个已经就绪的网络操作，没有操作时会暂时阻塞
                int count = selector.select();
                System.out.println("监听到 "+count+" 个事件");
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    //根据不同的事件类型，执行不同的操作即可
                    if(key.isAcceptable()) {  //如果当前ServerSocketChannel已经做好准备处理Accept
                        SocketChannel channel = serverChannel.accept();
                        System.out.println("客户端已连接，IP地址为："+channel.getRemoteAddress());
                        //现在连接就建立好了，接着我们需要将连接也注册选择器，比如我们需要当这个连接有内容可读时就进行处理
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                        //这样就在连接建立时完成了注册
                    } else if(key.isReadable()) {    //如果当前连接有可读的数据并且可以写，那么就开始处理
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(128);
                        channel.read(buffer);
                        buffer.flip();
                        System.out.println("接收到客户端数据："+new String(buffer.array(), 0, buffer.remaining()));

                        //直接向通道中写入数据就行
                        channel.write(ByteBuffer.wrap("已收到！".getBytes()));
                        //别关，说不定用户还要继续通信呢
                    }
                    //处理完成后，一定记得移出迭代器，不然下次还有
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static void main(String[] args) {

        testBuffer();

        testCpFile();

        testSelector();

    }
}
