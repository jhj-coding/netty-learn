package com.jhj.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {
    public static void main(String[] args) throws IOException {
        //得到一个网络通道
        SocketChannel open = SocketChannel.open();
        //设置非阻塞
        open.configureBlocking(false);
        //提供服务ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 666);
        //连接服务器
        if (!open.connect(inetSocketAddress)){
            while (!open.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作");
            }
        }
        //如果连接成功，发送数据
        String str="hello";
        ByteBuffer wrap = ByteBuffer.wrap(str.getBytes());
        //发送数据将buffer写入channel
        open.write(wrap);

        System.in.read();

    }
}
