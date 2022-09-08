package com.jhj.zerocopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


//服务器端
public class NewIoServer {
    public static void main(String[] args) throws Exception{
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7001);

        ServerSocketChannel open = ServerSocketChannel.open();

        ServerSocket socket = open.socket();

        socket.bind(inetSocketAddress);

        //创建buffer
        ByteBuffer allocate = ByteBuffer.allocate(4096);
        while (true){
            SocketChannel accept = open.accept();

            int readcount=0;
            while (-1!=readcount){
                try {
                    readcount=accept.read(allocate);
                }catch (Exception e){
//                    e.printStackTrace();
                    break;
                }
                //让buffer倒带 position=0 mark作废
                allocate.rewind();
            }

        }
    }
}
