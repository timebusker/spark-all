package com.timebusker.crawler.socket;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @DESC:SocketServer
 * @author:timebusker
 * @date:2019/8/30
 */

@Component
public class SocketServer {

    public static final String IP_ADDRESS = "127.0.0.1";

    public static final int PORT = 9999;

    @PostConstruct
    public void init() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    ServerSocket server = new ServerSocket(PORT);
                    System.out.println("启动服务器....");
                    while (true) {
                        Socket socket = server.accept();
                        System.out.println("客户端:" + socket.getInetAddress().getLocalHost() + "已连接到服务器");

                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        //读取客户端发送来的消息
                        String mess = reader.readLine();
                        System.out.println("客户端：" + mess);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        bw.write(mess + "\n");
                        bw.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
