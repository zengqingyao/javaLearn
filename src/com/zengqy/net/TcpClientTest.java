package com.zengqy.net;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @包名: com.zengqy.net
 * @author: zengqy
 * @DATE: 2023/3/29 23:45
 * @描述:
 */
public class TcpClientTest {

    public static void runSendMsg() {

        Socket skt = null;
        try {
            skt = new Socket(InetAddress.getByName("127.0.0.1"),33333);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendMsg(skt);
    }

    /**
     * 发送字节流数据
     * @param skt
     * @param buf
     */
    public static void sendMsgByteStream(Socket skt, byte[] buf) {

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(skt.getOutputStream());
            bos.write(buf);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送字符流数据
     * @param writer
     * @param msg
     */
    public static void sendMsgCharStream(BufferedWriter writer, String msg) {

        try {
            writer.write(msg);

            // 结束符，服务器也需要用readLine来读取
            writer.newLine();
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMsg(Socket skt) {

        OutputStream os = null;
        InputStream is = null;
        BufferedWriter bfWriter = null;
        BufferedReader bfReader = null;

        try {
            os = skt.getOutputStream();
            is = skt.getInputStream();
            bfWriter = new BufferedWriter(new OutputStreamWriter(os, Charset.forName("UTF-8")));
            bfReader = new BufferedReader(new InputStreamReader(is,Charset.forName("UTF-8")));


            sendMsgCharStream(bfWriter,TcpServiceWorkType.NormalSendMessageMode);

            while (true)
            {
                Scanner scanner = new Scanner(System.in);

                System.out.print("请输入发给服务器的信息内容：");
                String toMsg = scanner.nextLine();

                if("quit".equals(toMsg)) {
                    break;
                }

                sendMsgCharStream(bfWriter,toMsg);

                System.out.println("收到服务器返回的内容："+bfReader.readLine());

            }

        } catch (IOException e) {
            e.printStackTrace();

        }finally {

            try {
                bfReader.close();
                bfWriter.close();
                skt.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }





    public static void runUploadFile(String fileName) {
        System.out.println("连接服务器，上传文件：" + fileName);

        File file = new File(fileName);
        if(!file.exists()) {
            System.out.println("上传的文件不存在，退出");
            return;
        }

        Socket skt = null;
        BufferedWriter bfWriter = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        int rlen;
        try {
            skt = new Socket(InetAddress.getByName("127.0.0.1"),33333);

            OutputStream os = skt.getOutputStream();
            bfWriter = new BufferedWriter(new OutputStreamWriter(os, Charset.forName("UTF-8")));

            sendMsgCharStream(bfWriter,TcpServiceWorkType.UploadFileMode+":="+fileName);

            // 不写了，因为服务器运行太慢，如果这时候发数据过去会丢失，懒得写了
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(skt.getOutputStream());


            byte[] buf = new byte[1024];
            while ( (rlen = bis.read(buf)) != -1)
            {
                bos.write(buf,0,rlen);
                bos.flush();
            }

            skt.shutdownOutput();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bos.close();
                bis.close();
                skt.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }





    public static void main(String[] args) {

        runSendMsg();
        runUploadFile("src\\com\\zengqy\\io\\上传的文件.ts");

    }


}
