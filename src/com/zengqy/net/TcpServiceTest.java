package com.zengqy.net;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * @包名: com.zengqy.net
 * @author: zengqy
 * @DATE: 2023/3/27 16:37
 * @描述:
 */
public class TcpServiceTest {


    public static String getClietMode(Socket skt) throws Exception {
        InputStream is = skt.getInputStream();

        BufferedReader bfReader = new BufferedReader(new InputStreamReader(is, "utf-8"));

        String s = bfReader.readLine();
        return s;
    }


    public static void getNormalMessageMode(Socket skt) throws Exception {
        System.out.println("接受到新的客户端连接(消息模式)：" + skt);
        InputStream is = skt.getInputStream();
        OutputStream os = skt.getOutputStream();

        BufferedReader bfReader = new BufferedReader(new InputStreamReader(is, "utf-8"));
        BufferedWriter bfWriter = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));

        while (true) {
            String s = bfReader.readLine();

            if ("quit".equals(s) || (s == null)) {
                break;
            }

            bfWriter.write("--> 服务器：" + s);
            bfWriter.newLine();
            bfWriter.flush();
        }

        bfWriter.close();
        bfReader.close();
        skt.close();

        System.out.println("连接已经断开(消息模式)：" + skt);

    }


    public static boolean uploadFileMode(Socket skt,String fileName) throws IOException {
        System.out.println("接受到新的客户端连接(上传模式)：" + skt);

        InputStream is = skt.getInputStream();
        OutputStream os = skt.getOutputStream();

        String newFileName = fileName+".test";

        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFileName));

        int rlen;

        byte[] buf = new byte[1024];
        while ((rlen = bis.read(buf))!=-1)
        {
            bos.write(buf,0, rlen);
            bos.flush();
        }


        System.out.println("文件上传完成，路径："+newFileName);

        bis.close();
        bos.close();
        skt.close();
        return true;
    }


    public static void main(String[] args) throws Exception {

        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println("主机名/主机IP地址：" + localHost);


        InetAddress zengqy = InetAddress.getByName("zengqy");
        System.out.println("通过主机名获取信息，主机名/主机IP地址：" + zengqy);


        InetAddress[] bdAllByName = InetAddress.getAllByName("www.baidu.com");
        for (InetAddress inetAddress : bdAllByName) {
            System.out.println("百度的主机名和IP地址：" + inetAddress);
        }

        runTcpService();
    }

    public static void runTcpService() throws IOException {
        System.out.println("TCP服务器正在运行.....");
        ServerSocket serverSocket;

        serverSocket = new ServerSocket(33333);

        byte[] buf = new byte[1024];
        int readLen = 0;
        while (true) {
            Socket skt = serverSocket.accept();
            new Thread(new HandleTcpClientThread(skt)).start();
        }
    }

}


class HandleTcpClientThread implements Runnable {

    public Socket skt;

    public HandleTcpClientThread(Socket skt) {
        this.skt = skt;
    }

    @Override
    public void run() {
        try {
            String clietMode = TcpServiceTest.getClietMode(skt);
            if (TcpServiceWorkType.NormalSendMessageMode.equals(clietMode)) {

                TcpServiceTest.getNormalMessageMode(skt);

            } else if (TcpServiceWorkType.UploadFileMode.equals(clietMode.split(":=")[0])) {

                TcpServiceTest.uploadFileMode(skt,clietMode.split(":=")[1]);

            }

        } catch (SocketException e) {
            System.out.println("客户端是否已经关闭？" + skt.isConnected() + " " + skt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}