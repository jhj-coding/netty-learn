package com.jhj.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NewIoClient {
    public static void main(String[] args) throws Exception {
        SocketChannel open = SocketChannel.open();

        open.connect(new InetSocketAddress("localhost", 7001));

        String fileName = "F:\\1.docx";

        //得到文件channel
        FileChannel channel = new FileInputStream(fileName).getChannel();

        System.out.println(channel.size());
        //准备发送
        long start = System.currentTimeMillis();

        /**
         * transferTo底层使用零拷贝
         * 在linux下一个transferTo方法就可以完成传输
         * 在windows下 一次调用transferTo 只能发送8m,就需要分段传输 如果大于8M 就需要循环
         * int count = (int) Math.ceil(channel.size() / 8388608.0);
         * long l=0;
         * for (int i = 0; i < count-1; i++) {
         *
         *     l+=channel.transferTo(i * 8388608, (i + 1) * 8388608, open);
         * }
         * l+=channel.transferTo((count - 1) * 8388608, channel.size(), open);
         *
         * 参数1 位置
         * 参数2 大小
         * 参数3 通道
         */

//        long l = channel.transferTo(0, channel.size(), open);

//        System.out.println("发送总的字节数=" + l + "耗时" + (System.currentTimeMillis() - start));

        //关闭通道
        channel.close();
    }
}
