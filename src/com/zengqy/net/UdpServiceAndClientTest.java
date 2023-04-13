package com.zengqy.net;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

/**
 * @包名: com.zengqy.net
 * @author: zengqy
 * @DATE: 2023/3/31 22:17
 * @描述:
 */
public class UdpServiceAndClientTest {


    public static void sendUdpMsg() {
        System.out.println("udp客户端发送-->：");

        DatagramSocket ds = null;

        try {
            ds = new DatagramSocket();


            byte[] buf = new byte[1024];

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("udp客户端--> 请输入发给服务器的内容：");
                String msg = scanner.nextLine();

                byte[] bytes = msg.getBytes();

                DatagramPacket dp = new DatagramPacket(bytes,
                        bytes.length, InetAddress.getByName("127.0.0.1"), 9999);

                ds.send(dp);

                // 不用管，这是为了先打印服务器的内容
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ds.close();
        }


    }


    public static void main(String[] args) {
        new Thread(new UdpService(9999)).start();
        sendUdpMsg();
    }


}


class UdpService implements Runnable {

    public int port;

    public UdpService(int port) {
        this.port = port;
    }


    public void runUdpService(int port) {
        System.out.println("UDP服务器已经启动-->");
        // udp包最大 64Kb
        byte[] buf = new byte[64 * 1024];

        DatagramSocket ds = null;

        try {
            ds = new DatagramSocket(port);

            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            while (true) {
                ds.receive(dp);

                System.out.println("======================================");
                System.out.println("UDP服务器--> " + dp.getSocketAddress());

                // 其实就是 buf
                byte[] data = dp.getData();

                String msg = new String(data, 0, dp.getLength());


                System.out.println("UDP服务器收到消息--> " + msg+"");
                System.out.println("======================================\n");
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
    }

    @Override
    public void run() {
        runUdpService(this.port);
    }
}
