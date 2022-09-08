package com.jhj.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
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
                            /*
                            说明 IdleStateHandler
                            是netty提供的 空闲状态处理器
                            参数一 表示多长时间没有读 就会发送一个心跳检测包
                            参数二 表示多长时间没有写 就会发送一个心跳检测包
                            参数三 表示多长时间没有读写 就会发送一个心跳检测包

                            当IdleStateEvent 触发后，就会传递给管道的下一个handler去处理
                            通过调用下一个handler的userEventTiggered方法 在该方法中处理

                             */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));

                            //加入一个对空闲检测进一步处理的handler(自定义)
                            pipeline.addLast(new MyServerHandler());

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
