package com.jhj.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/*
SimpleChannelInboundHandler 是ChannelInboundHandler子类
HttpObject 表示客户端和服务器端相互通讯的数据被封装成 HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {


    //当有读取事件是会触发函数
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        //判断msg是不是 httpRequest请求
        if (msg instanceof HttpRequest) {


            //由于Http不是长连接 每次用完后会断掉 一次请求的 浏览器和channel 一一对应 channel和piepeline 一一对应
            System.out.println("pipeline hashcode"+ctx.pipeline().hashCode()+"TestHttpServerHandler hash="+this.hashCode());

            System.out.println("msg 类型=" + msg.getClass());

            System.out.println("客户端地址" + ctx.channel().remoteAddress());

            //通过url 过滤指定资源
            //获取到 msg资源的url
            HttpRequest httpRequest=(HttpRequest) msg;
            URI uri = new URI(httpRequest.uri());

            if ("/favicon.ico".equals(uri.getPath())){
                System.out.println("你请求了 favicon.ico 不做响应");
                return;
            }


            //回复信息给浏览器
            ByteBuf content = Unpooled.copiedBuffer("hello,我是服务器", CharsetUtil.UTF_8);

            //构造一个http的响应 httpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            ctx.writeAndFlush(response);
        }

    }
}
