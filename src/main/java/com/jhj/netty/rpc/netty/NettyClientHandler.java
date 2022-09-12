package com.jhj.netty.rpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;//上下文

    private String result;//返回的消息

    private String para;//客户端调用的方法时，传入的参数

    //与服务器的连接创建后，就会被调用
    //调用顺序1
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context=ctx;//因为我们会在其它方法中使用到ctx
    }

    //收到服务器的数据后，调用方法
    //调用顺序4
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        result=msg.toString();
        notify();    //唤醒等待的线程


    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    //被代理对象调用 发送数据给服务器 -》wait —》等待被唤醒-》返回结果
    //调用顺序3 5
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(para);
        wait();//等待channelRead方法获取结果后 被唤醒
        return result;
    }

    //调用顺序2
    void setPara(String para){
        this.para=para;
    }

}
