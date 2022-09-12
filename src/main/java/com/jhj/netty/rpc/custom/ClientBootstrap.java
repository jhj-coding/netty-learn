package com.jhj.netty.rpc.custom;

import com.jhj.netty.rpc.netty.NettyClient;
import com.jhj.netty.rpc.publicInterface.HelloService;

public class ClientBootstrap {

    //定义协议头
    public static final String providerName="jhj#hello#";
    public static void main(String[] args) {
        NettyClient customer = new NettyClient();
        
        //创代理对象
        HelloService helloService = (HelloService)customer.getBean(HelloService.class, providerName);

        //通过代理对象调用服务提供者的方法（服务）
        String res = helloService.hello("你好 jhj~");
        System.out.println("调用的结果 res="+res );


    }
}
