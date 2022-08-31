package com.jhj.channel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileChannel {
    private static ByteBuffer allocate;

    public static void main(String[] args) throws IOException {
        String hello = "hello";
        //从创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("F:\\1.txt");;

        //通过fileoutpotstream 获取对应的FileChannel

        FileChannel channel = fileOutputStream.getChannel();
        
        //创建一个缓冲区bytebuffer
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        
        //将str放入bytebuff
        allocate.put(hello.getBytes());

        //对bytebuff flip反转
        allocate.flip();

        //将byteBuffer 数据写入filechannel
        channel.write(allocate);
        //关闭输出流
        fileOutputStream.close();
    }
}
