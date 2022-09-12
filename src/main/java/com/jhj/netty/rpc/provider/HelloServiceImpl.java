package com.jhj.netty.rpc.provider;

import com.jhj.netty.rpc.publicInterface.HelloService;

public class HelloServiceImpl implements HelloService {

    //当有消费方调用该方法时就返回一个结果
    @Override
    public String hello(String msg) {
        System.out.println("收到客户端（消费方消息=）"+msg);
        //根据msg返回不同的结果
        if (msg!=null){
            return "你好 客户端，我已经收到你的消息["+msg
                    +"]";
        }else{
            return "";
        }
    }
}
