package com.jhj.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
    public static void main(String[] args) {
        //创建一个ByteBuf
        //创建对象 该对象包含一个数字 arr,是一个byte[10]
        //在netty的buffer中不需要使用flip 进行反转
        //底层维护了readerIndex writerIndex
        ByteBuf buffer = Unpooled.buffer(10);


        //写
        for (int i=0;i<10;i++){
            buffer.writeByte(i);
        }

        //输出1
        for (int i=0;i<buffer.capacity();i++){
            System.out.println(buffer.getByte(i));
        }

        //输出2
        for (int i=0;i<buffer.capacity();i++){
            System.out.println(buffer.readByte());
        }

    }
}
