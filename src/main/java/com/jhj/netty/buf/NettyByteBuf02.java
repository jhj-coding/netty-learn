package com.jhj.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyByteBuf02 {
    public static void main(String[] args) {
        //创建一个ByteBuf
        ByteBuf buffer = Unpooled.copiedBuffer("hello", CharsetUtil.UTF_8);

        //使用相关的方法
        if (buffer.hasArray()){ //是否有数组

            byte[] content = buffer.array();

            //将content转换为字符串
            System.out.println(new String(content, Charset.forName("utf-8")));

            System.out.println(buffer);

            System.out.println(buffer.arrayOffset());

            System.out.println(buffer.readerIndex());

            System.out.println(buffer.writerIndex());

            System.out.println(buffer.capacity());

            System.out.println(buffer.readableBytes()  );

            System.out.println(buffer.getCharSequence(0,4,CharsetUtil.UTF_8));
        }




    }
}
