package com.jhj.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer 可以让文件直接在内存（堆外内存）修改，操作系统不需要拷贝一次
 */


public class NioFileChannel05 {
    public static void main(String[] args) throws Exception {
        RandomAccessFile rw = new RandomAccessFile("F://1.txt", "rw");
        //获取通道
        FileChannel channel = rw.getChannel();
        /**
         * 参数1 读写模式
         * 参数2 可以直接修改的起始位置
         * 参数3 映射到内存的大小 即将文件 F：//1.txt 的多少个字节映射到内存
         * 即可以直接修改的范围为0-5 不包括索引为5
         * 实际类型是DirectByteBuffer
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        map.put(0,(byte)'J');
        map.put(3,(byte)'J');
        rw.close();
        System.out.println("修改成功");

    }
}
