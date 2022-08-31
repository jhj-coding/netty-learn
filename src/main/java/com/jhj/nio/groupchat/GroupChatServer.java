package com.jhj.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PROT = 6667;

    //构造器
    public GroupChatServer() {
        //初始化
        try {
            //得到选择器
            selector = Selector.open();
            //ServerScoketChannel
            listenChannel=ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PROT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将该listenChannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen() {
        try {
            //循环处理
            while (true) {
                int count = selector.select();
                //有事件处理
                if (count > 0) {
                    //遍历得到selectionKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        //取出selectionKey
                        SelectionKey key = iterator.next();
                        //监听到accept
                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress() + "上线");
                        }

                        if (key.isReadable()) {
                            //通道发送read事件，即通道是可读的状态
                            //处理读
                            readDate(key);
                        }
                        //删除 方式重复操作
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待...");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    //读取客户端消息
    private void readDate(SelectionKey key) {

        //定义一个SocketChannel

        SocketChannel channel = null;
        try {
            //得到channel
            channel = (SocketChannel) key.channel();

            //创建buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            //获取到该channel关联的buffer
            int count = channel.read(byteBuffer);
            //根据count的值做处理
            if (count > 0) {
                //把缓冲区的数据转成字符串
                String s = new String(byteBuffer.array());
                //输出
                System.out.println("from客户端：" + s);

                //向其他客户端转发消息
                sendInfoToOtherClients(s, channel);
            }

        } catch (IOException e) {

            try {
                System.out.println(channel.getRemoteAddress() + "离线了....");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    //转发消息给其他客户端（通道）
    private void sendInfoToOtherClients(String s, SocketChannel socketChannel) throws IOException {

        System.out.println("服务器转发消息中");
        //遍历所有注册到select上的SocketChannel 并排除自己
        for (SelectionKey key : selector.keys()) {
            //通过key获取 socketChannel
            Channel channel = key.channel();
            //排除自己
            if (channel instanceof SocketChannel && channel != socketChannel) {
                //转型
                SocketChannel channel1 = (SocketChannel) channel;
                //将s存储到buffer
                ByteBuffer wrap = ByteBuffer.wrap(s.getBytes());
                //将buffer数据写入通道
                channel1.write(wrap);
            }
        }


    }


    public static void main(String[] args) {
        //创建一个服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
