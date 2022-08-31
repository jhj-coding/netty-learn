package com.jhj.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        //创建一个线程池
        ExecutorService executorService= Executors.newCachedThreadPool();
        //如果有客户端链接，就创建一个线程 cmd telnet连接

        //创建ServerSocket
        ServerSocket serverSocket=new ServerSocket(666);

        System.out.println("服务端启动");

        while (true){
            //监听客户端连接
            System.out.println("线程信息 id="+Thread.currentThread().getId()+"名字="+Thread.currentThread().getName());
            System.out.println("等待连接...");
            final Socket socket=serverSocket.accept();
            System.out.println("连接到一个客户端");
            //就创建一个新的线程与之通讯
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //和客户端通讯
                    handler(socket);
                }
            });
        }
    }

    //编写handler方法，和客户端通讯
    public static void handler(Socket socket){
        try {
            System.out.println("线程信息 id="+Thread.currentThread().getId()+"名字="+Thread.currentThread().getName());
            byte[] bytes=new byte[1024];
            //通过socket 获取输入流
            InputStream inputStream=socket.getInputStream();

            //循环读取客户端发送的数据
            while (true){
                System.out.println("线程信息 id="+Thread.currentThread().getId()+"名字="+Thread.currentThread().getName());

                System.out.println("read...");
                int read = inputStream.read(bytes);

                if (read!=-1){
                    System.out.println(new String(bytes,0,read));
                }else{
                    break;
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client的连接");

            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
