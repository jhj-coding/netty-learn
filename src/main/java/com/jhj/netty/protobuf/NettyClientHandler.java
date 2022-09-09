package com.jhj.netty.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 说明
 * 1.我们自定义一个Handler需要继承netty规定好的某个HandlerAdapter
 * <p>
 * 2.这时我们自定义一个Handler,才能成为一个Handler
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //当管道就绪时就会触发

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("client"+ctx);

        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,server",CharsetUtil.UTF_8));
    }

    //当通道有读取事件时会触发

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息："+buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址"+ctx.channel().remoteAddress());
    }

    //处理异常，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
