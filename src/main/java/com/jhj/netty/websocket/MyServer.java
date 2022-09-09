package com.jhj.netty.websocket;

import com.jhj.netty.heartbeat.MyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {
    public static void main(String[] args) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();


                            //因为基于Http协议 所以使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            //以块方式写添加 ChunkedWrite 处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            /**
                             * 说明
                             * http数据在传输过程中的分段的，HttpObjectAggregator,就是可以将多个段聚合
                             * 这就是为什么 当浏览器发送大量数据时，就会发出多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            /*
                            说明
                            对应websocket,它的数据是以帧（frame）形式传播
                            可以看到webSocketFrame 下面有六个子类
                            浏览器请求时 ws://localhost:7000/xxx表示请求的uri xxx与下面参数对应
                            WebSocketServerProtocolHandler 核心功能是将http协议升级为ws协议，保持长连接
                            是通过一个状态码 101 讲http转化为websocket
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //自定义的handler 处理业务
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    });

            //启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();



        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
