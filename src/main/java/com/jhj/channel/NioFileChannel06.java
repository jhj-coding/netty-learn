package com.jhj.channel;

import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering :将数据写入到buffer时，可以采用buffer数组，依次写入 分散
 * Gathering :从buffer读数据时，可以采用buffer数组，依次读 聚合
 */


public class NioFileChannel06 {
    public static void main(String[] args) throws Exception {
        //使用serverSockerChannel 和SocketChannel 网络
        ServerSocketChannel open = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        //绑定端口到socket并启动
        open.socket().bind(inetSocketAddress);
        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0]=ByteBuffer.allocate(5);
        byteBuffers[1]=ByteBuffer.allocate(3);
        //等待客户端连接
        SocketChannel accept = open.accept();

        //假定从客户端接受8个字节
        int messageLength=8;
        //循环读取
        while (true){
            int byteRead=0;
            while (byteRead<messageLength){
                long read = accept.read(byteBuffers);
                byteRead+=read;
                System.out.println("byteRead="+byteRead);
                //使用流打印 看看当前buffer的position 和limit
                Arrays.asList(byteBuffers).stream().map(buffer->
                    "position"+buffer.position()+"limit="+buffer.limit()
                ).forEach(System.out::println);

                //将所有buffer反转
                Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

                //将数据显示在客户端
                long byteWrite=0;
                while (byteWrite<messageLength){
                    long write = accept.write(byteBuffers);
                    byteWrite+=write;
                }
                //将所有的buffer进行clear
                Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());

                System.out.println("byteRead:="+byteRead+"byteWrite:="+byteWrite+"messageLength:="+messageLength);
            }
        }

    }
}
