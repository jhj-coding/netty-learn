package com.jhj.netty.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class NettyServer {
    public static void main(String[] args) throws InterruptedException {

        //创建BossGroup和WorkerGroup

        /**
         * 说明
         * 1.创建两个线程组bossGroup和workerGroup
         * 2.bossGroup只是处理连接请求，真正的和客户端业务处理，会交给workerGroup完成
         * 3.两个都是无限循环
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程来进行设置
            bootstrap.group(bossGroup,workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //使用NioSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG,128) //设置线程队列等待连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true) //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        //创建一个通道初始化对象（匿名对象）
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    }); //给我们的wokerGroup的EventGroup对应的通道设置处理器
            System.out.println("...服务器 is ready...");

            //绑定一个端口并且同步，生成了一个ChannelFuture 对象
            //启动服务器
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给cf注册监听器 监控我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (cf.isSuccess()){
                        System.out.println("成功");
                    }else{
                        System.out.println("失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            //优雅的关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
