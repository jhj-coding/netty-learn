package com.jhj.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {

    //定义相关的属性
    private final String HOST="127.0.0.1";
    
    private final int PROT=6667;
    
    private Selector selector;
    private SocketChannel socketChannel;
    
    private String username;
    
    //构造器 完成初始化
    public GroupChatClient() throws IOException {
        selector = Selector.open();
        
        //连接服务器
        socketChannel=SocketChannel.open(new InetSocketAddress(HOST,PROT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //将channel注册到Selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
        username = socketChannel.getLocalAddress().toString().substring(1);

        System.out.println(username+"is ok");
    }

    //向服务器发送消息
    public void sendInfo(String info){
        info=username+"说："+info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //读取从服务器端回复的消息
    public void readInfo(){
        try {
            int readChannels=selector.select(2000);
            if (readChannels>0){
                //有可用的通道

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){

                    SelectionKey next = iterator.next();
                    if (next.isReadable()){
                        //得到相关的通道
                        SocketChannel channel = (SocketChannel) next.channel();
                        //得到一个buffer
                        ByteBuffer allocate = ByteBuffer.allocate(1024);
                        //读取
                        channel.read(allocate);
                        //把读到的缓冲区的数据转成字符串
                        String s = new String(allocate.array());
                        System.out.println(s.trim());
                    }
                    //删除当前selectkey 防止重复操作
                    iterator.remove();
                }
            }else {
//                System.out.println("没有可以用的通道");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //启动客户端
        GroupChatClient groupChatClient = new GroupChatClient();
        //启动一个线程 每隔3秒 读取从服务器发送的数据
        new Thread(){
            public void run(){
                while (true){
                    groupChatClient.readInfo();
                    try{
                      Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();


        //发送数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s=scanner.nextLine();
            groupChatClient.sendInfo(s);
        }
    }
}
