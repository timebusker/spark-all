package com.timebusker.crawler.task;

import com.timebusker.crawler.socket.SocketServer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @DESC:SocketWriteDataTask
 * @author:timebusker
 * @date:2019/8/30
 */
@Component
public class SocketWriteDataTask {

    @Scheduled(fixedRate = 1000)
    public void execute() {
        try {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        Socket s = new Socket(SocketServer.IP_ADDRESS, SocketServer.PORT);
                        //构建IO
                        InputStream is = s.getInputStream();
                        OutputStream os = s.getOutputStream();

                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                        //向服务器端发送一条消息
                        bw.write("测试 客户端 和 服务器 通信  服务器 接收 到 消息 返回 到 客户端\n");
                        bw.flush();

                        //读取服务器返回的消息
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String mess = br.readLine();
                        System.out.println("服务器：" + mess);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
