package com.zengqy.io;

import java.io.*;
import java.util.Properties;

/**
 * @包名: com.zengqy.io
 * @USER: zengqy
 * @DATE: 2022/4/11 17:22
 * @描述: JAVA IO的使用
 * ○ InputStream(字节流)缓冲与BufferInputStream(处理流)缓冲区别
 * 	• InputStream和BufferInputStream数据流复制一个167M的数据文件 ，耗时分别为：1495ms 和 673ms
 * 	• InputStream：通过源码可以看到，如果用read()方法读取一个文件，每读取一个字节就要访问一次硬盘，
 * 	  这种读取的方式效率是很低的。即便使用read(byte b[])方法一次读取多个字节，每次读取多个字节就要访问一次硬盘。
 * 	  当读取的文件较大时，也会频繁的对磁盘操作。
 * 	• BufferedInputStream：也就是说，Buffered类初始化时会创建一个较大的byte数组，
 * 	  一次性从底层输入流中读取多个字节来填充byte数组，当程序读取一个或多个字节时，可直接从byte数组中获取，
 * 	  当内存中的byte读取完后，会再次用底层输入流填充缓冲区数组。这种从直接内存中读取数据的方式要比每次都访问磁盘的效率高很多。
 * ○ 总结：
 * 	• InputStream每次read，都需要从硬件读取文件一个或者若干个字节。
 *    BufferedInputStream会提前读取默认大小字节到内部缓冲区，每次read都直接从内存读取一个或若干个字节。
 */
public class TestIoStream {
    public static void main(String[] args) {

        // 字节流实现文件的拷贝实现，对于非文本文件(.jpg,.mp3,.mp4,.avi,.doc,.ppt)，使用字节流文件
        testFileInputOutputStream();

        // 字符流实现文件的拷贝，对于文本文件（.txt,.java,.c,.cpp），使用字符流处理
        testFileReaderWriter();

        // 字节缓冲流 实现文件的拷贝，是处理流(包装流)
        testBufferedStream();


        // 把数据或者类进行序列号和反序列化存储
        testObjectOutputStream();

        // 存储属性的值
        testProperties();

        // 字符缓冲流
        testBufferedReaderWrite();
    }


    /**
     * 输入输出字节流综合使用——文件拷贝实现
     * 可以根据拷贝的需求调整bytes数组的大小，一般是1024的整数倍，使用缓冲后效率大大提高。
     */
    public static void testFileInputOutputStream() {
        System.out.println("\n=====================testFileInputOutputStream======================");
        File srcFile = new File("src\\com\\zengqy\\io\\dolby_test.ts");
        if (!srcFile.exists()) {
            try {
                System.out.println("provide.db not exists! in " + new File("").getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File desFile = new File("src\\com\\zengqy\\io\\dolby_test_cp.ts");

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;

        long start = System.currentTimeMillis();
        try {
            fileInputStream = new FileInputStream(srcFile);
            fileOutputStream = new FileOutputStream(desFile);

            byte[] bytes = new byte[1024];
            int rlen;
            while ((rlen = fileInputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, rlen);
            }

            long time = System.currentTimeMillis()-start;
            System.out.println("使用 FileInputOutputStream ，字节流 拷贝文件成功，耗时: "+time+"ms");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 字符流拷贝文件
     */
    public static void testFileReaderWriter() {

        System.out.println("\n=====================testFileReaderWriter======================");

        File srcFile = new File("src\\com\\zengqy\\io\\TestIoStream.java");
        if (!srcFile.exists()) {
            System.out.println("File not exists");
        }

        File destFile = new File("src\\com\\zengqy\\io\\TestIoStream.java_tmp");

        FileReader fileReader = null;
        FileWriter fileWriter = null;

        try {
            fileReader = new FileReader(srcFile);
            fileWriter = new FileWriter(destFile);

            int rlen;
            char[] chars = new char[8];

            while ((rlen = fileReader.read(chars)) != -1) {
                fileWriter.write(chars,0,rlen);
            }

            System.out.println("使用 FileReaderWriter，字符流 拷贝文本文件成功");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {


            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 内部也是封装了字节数组。没有指定缓冲区大小，默认的字节是8192
     */
    public static void testBufferedStream() {

        System.out.println("\n=====================testBufferedStream======================");

        try {

            // 输入流
            InputStream bufferedInputStream = new BufferedInputStream(
                    new FileInputStream("src\\com\\zengqy\\io\\dolby_test.ts"));

            // 输出流
            OutputStream bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream("src\\com\\zengqy\\io\\dolby_test_buffercp.ts"));

            int rlen;
            byte[] bytes = new byte[1024];

            long start = System.currentTimeMillis();
            while ((rlen = bufferedInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, rlen);
                bufferedOutputStream.flush();
//                System.out.println("rlen: " + rlen);
            }
            long time = System.currentTimeMillis()-start;
            System.out.println("使用 BufferedOutputStream ，字节流 拷贝文件成功，耗时: "+time+"ms");

            // // 要求：先关闭外层的流，在关闭外层流的同时，内层流也会自动的进行关闭
            bufferedInputStream.close();
            bufferedOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void testBufferedReaderWrite() {
        System.out.println("\n=====================testBufferedReaderWrite======================");
        try {

            // 1 InputStreamReader:将 InputStream(字节流) 转换为 Reader(字符流) 输入
            // 2 OutputStreamWriter:将 Writer(字符流) 以字节流 OutputStream(字节流) 输出
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            FileOutputStream fileOutputStream = new FileOutputStream("src\\com\\zengqy\\io\\test.txt");

            // 默认以utf-8保存文件
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            String line = null;
            System.out.println("请输入：");
            while ((line = bufferedReader.readLine()) != null) {
                if ("quit".equals(line))
                    break;

                bufferedWriter.write(line);
                bufferedWriter.newLine(); //换行
                bufferedWriter.flush(); //刷新

            }

            bufferedReader.close();
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void testObjectOutputStream() {

        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(new FileOutputStream("src\\com\\zengqy\\io\\obj.dat"));

            oos.writeInt(99);
            oos.writeBoolean(false);
            oos.writeChar('b');
            oos.writeDouble(3.14);
            oos.writeUTF("曾庆耀");
            oos.writeObject(new Person("来来来",20));

            oos.close();
            System.out.println("数据保存完成，序列号完成");
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("src\\com\\zengqy\\io\\obj.dat"));

            System.out.println(ois.readInt());
            System.out.println(ois.readBoolean());
            System.out.println(ois.readChar());
            System.out.println(ois.readDouble());
            System.out.println(ois.readUTF());
            System.out.println(ois.readObject());

            ois.close();
            System.out.println("数据反序列化完成");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 存储properties数据
     */
    private static void testProperties() {
        System.out.println("\n=====================testProperties======================");

        Properties properties = new Properties();

        properties.setProperty("ip","192.168.2.1");
        properties.setProperty("user","曾庆耀");
        properties.setProperty("pwd","zengqy123.");


        try {
            properties.store(new FileOutputStream("src\\com\\zengqy\\io\\mysql.properties"),"这是注释");


            properties.load(new FileReader("src\\com\\zengqy\\io\\mysql.properties"));

            System.out.println("ip="+properties.getProperty("ip"));
            System.out.println("user="+properties.getProperty("user"));
            System.out.println("pwd="+properties.getProperty("pwd"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }







}

/**
 * transient 和 static 不会被序列化
 */
class Person implements Serializable
{
    private String name;
    private int age;

    // 序列号版本号，可以提高版本的兼容性
    private static final long serialVersionUID = 1L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}