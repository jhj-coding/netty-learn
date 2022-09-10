package com.jhj.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //使用客户端发送10条数据
        for (int i=0;i<10;i++){
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello,server" + 1, CharsetUtil.UTF_8);
            System.out.println(i);
            ctx.writeAndFlush(byteBuf);
        }

    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);

        //将buffer转成字符串
        String s = new String(bytes, CharsetUtil.UTF_8);
        System.out.println(s);

        System.out.println("客户端接收到消息量"+(++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
