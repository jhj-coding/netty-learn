package com.jhj.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     *decode 会根据接收的数据，被调用多次，知道确定没有新的元素被添加到list,或者是ByteBuf 没有更多的可读字节为止
     * 如果list out不为空 就会将list的内容传递给下一个channelinboundhandler处理，该处理器的方法也会调用多次
     *
     * @param ctx   上下文        the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param in    入站的ByteBuf        the {@link ByteBuf} from which to read data
     * @param out   List集合，将解码后的数据传给下一个handler        the {@link List} to which decoded messages should be added
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        System.out.println("decoder被调用");
        //因为long8个字节,需要判断有8个字节才能读取一个long
        if (in.readableBytes()>=8){
            out.add(in.readLong());
        }
    }
}
