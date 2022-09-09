package com.jhj.netty.protobuf2;

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
//public class NettyServerHandler extends ChannelInboundHandlerAdapter {
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    //读取数据的事件（这里我们可以读取客户端发送的消息）

    /**
     * @param ctx :上下文对象，含有管道 piepline,通道channel,地址
     * @param msg :就是客户端发送的数据，默认Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //读取从客户端发送的StudentPojo.student
        MyDataInfo.MyMessage myMessage = (MyDataInfo.MyMessage) msg;
        MyDataInfo.MyMessage.DataType dataType = myMessage.getDataType();
        if (dataType== MyDataInfo.MyMessage.DataType.StudentType){
            System.out.println("客户端发送消息是：" + myMessage.getStudent().getId()+myMessage.getStudent().getName());

        }else if (dataType== MyDataInfo.MyMessage.DataType.WorkerType) {
            System.out.println("客户端发送消息是：" + myMessage.getWorker().getAge()+myMessage.getWorker().getName());
        }else {
            System.out.println("参数不对");
        }

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
