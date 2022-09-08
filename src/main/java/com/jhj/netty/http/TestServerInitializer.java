package com.jhj.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = ch.pipeline();

        //加入netty 提供的http编解码器 heetServerCodec codec=>{code+decoder}

        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());

        //增加一个自定义的handler
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());


    }
}
