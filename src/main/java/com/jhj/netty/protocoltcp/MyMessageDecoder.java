package com.jhj.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyMessageDecoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("decode 被调用");

        int length = in.readInt();

        byte[] bytes = new byte[length];

        in.readBytes(bytes);

        //封装成MessageProtocol对象，放入out，传递给下一个handler

        MessageProtocol messageProtocol=new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(bytes);

        out.add(messageProtocol);
    }
}
