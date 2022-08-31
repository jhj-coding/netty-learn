package com.jhj.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileChannel02 {
    private static ByteBuffer allocate;

    public static void main(String[] args) throws IOException {

        //创建一个文件
        File file = new File("F:\\1.txt");
        //创建输入流
        FileInputStream fileInputStream = new FileInputStream(file);
        //获取channel
        FileChannel channel = fileInputStream.getChannel();
        //创建缓冲区
        ByteBuffer allocate1 = ByteBuffer.allocate((int) file.length());
        //将通道数据读取到buffer
        channel.read(allocate1);

        //将字节数据转化为string

        System.out.println(new String(allocate1.array()));

        fileInputStream.close();


    }
}
