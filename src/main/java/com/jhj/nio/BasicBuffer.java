package com.jhj.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        //创建buffer
        //可以存放5个int
        IntBuffer intBuffer=IntBuffer.allocate(5);
        //向buffer中存放数据
        for (int i=00;i<intBuffer.capacity();i++){
            intBuffer.put(i);
        }
        //读出数据
        //将buffer转换，读写切换
        intBuffer.flip();

        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
