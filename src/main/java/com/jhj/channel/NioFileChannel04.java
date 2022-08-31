package com.jhj.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileChannel04 {
    private static ByteBuffer allocate;

    public static void main(String[] args) throws IOException {

        //输入流
        FileInputStream fileInputStream = new FileInputStream("F://1.txt");
        //获取channel
        FileChannel channel = fileInputStream.getChannel();

        //输出流
        FileOutputStream fileOutputStream = new FileOutputStream("F://2.txt");
        //获取channel
        FileChannel channel1 = fileOutputStream.getChannel();

        //拷贝
        channel1.transferFrom(channel,0,channel.size());

        fileInputStream.close();
        fileOutputStream.close();
    }
}
