package com.jhj.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws IOException {
        //创建ServerSocketChannel ->ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //得到一个selector
        Selector selector = Selector.open();
        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //把serverSocketChannel注册到selector关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //循环等待客户端连接
        while (true){
            //等待1s,如果没有事件发生就返回
            if (selector.select(1000)==0){
                System.out.println("服务器等待1秒，无连接");
                continue;
            }
            //如果返回的大于0 获取到相关的selectionKey集合
            //如果返回大于0，表示获取到关注的事件了
            //返回关注事件的集合
            //通过selectionKeys 反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //遍历set
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                //获取到selectionKey
                SelectionKey next = iterator.next();
                //根据key对应的通道发生的事件做相应的处理
                if (next.isAcceptable()){
                    //如果是Op_Accept 则是有客户端来连接我
                    //给该客户端生成一个SocketChannel
                    SocketChannel accept = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个socketChannel"+accept.hashCode());
                    //将socketChannel设置非阻塞
                    accept.configureBlocking(false);
                    //将当前的socketChannel 注册到selector,关注事件为 Op_Read,同时给socketChannel关联一个Buffer
                    accept.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }


                if (next.isReadable()){
                    //发生Op_Read

                    //通过key反向获取对应的channel
                    SocketChannel channel = (SocketChannel)next.channel();

                    //获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer)next.attachment();
                    //读
                    channel.read(buffer);
                    System.out.println("客户端发送的数据是"+new String(buffer.array()));
                }

                //手动从集合中移动当前的selectionKey,防止重复操作
                iterator.remove();
            }
        }
    }
}
