package com.jhj.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //入站的Handler进行解码 MyByteToLongDecoder
        //MessageToByteEncoder 和 ByteToMessageDecoder不冲突
        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyLongToByteEncoder());
        //自定义的handler 处理业务罗技
        pipeline.addLast(new MyServerHandler());
    }
}
