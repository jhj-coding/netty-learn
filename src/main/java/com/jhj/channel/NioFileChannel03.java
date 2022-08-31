package com.jhj.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileChannel03 {
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

        //缓冲区
        ByteBuffer allocate1 = ByteBuffer.allocate(1024);

        //读到缓冲区
        while(true){
            //将标志位重置 清空buffer 如果不clear position limit 会相同 读取为0
            allocate1.clear();

            int read = channel.read(allocate1);
            if (read == -1) {
                break;
            }
            //将读取到的数据 写入
            allocate1.flip();
            channel1.write(allocate1);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
}
