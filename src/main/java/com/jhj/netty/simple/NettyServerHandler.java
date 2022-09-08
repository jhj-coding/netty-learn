package com.jhj.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 说明
 * 1.我们自定义一个Handler需要继承netty规定好的某个HandlerAdapter
 * <p>
 * 2.这时我们自定义一个Handler,才能成为一个Handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取数据的事件（这里我们可以读取客户端发送的消息）

    /**
     * @param ctx :上下文对象，含有管道 piepline,通道channel,地址
     * @param msg :就是客户端发送的数据，默认Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {



        /*
        比如这里我们有一个非常耗时长的业务-》异步执行-》提交该channel对应的
        NIOEventLoop中的taskQueue中
         */
        //方法1. 用户自定义的普通任务
        // 10秒后执行 如果有多个则根据队列顺序相加 因为用一个线程
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //30秒过后返回  10+20
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        //方法2 用户自定义定时任务-》该任务提交到scheduleTaskQueue中
        //5秒后执行该定时任务
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },5, TimeUnit.SECONDS);


        System.out.println("server ctx=" + ctx);

        //将msg转成一个ByteBuf
        //ByteBuf是Netty提供的，不是NIO的ByteBuffer
        ByteBuf buf = (ByteBuf) msg;

        System.out.println("客户端发送消息是：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址" + ctx.channel().remoteAddress());
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        //wirteAndFlush 是write+flush
        //将数据写入到缓存，并刷新
        //一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~", CharsetUtil.UTF_8));

    }


    //处理异常，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
