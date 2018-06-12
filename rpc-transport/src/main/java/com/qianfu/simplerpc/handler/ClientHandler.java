package com.qianfu.simplerpc.handler;

import com.qianfu.simplerpc.serialization.factory.SerializerFactory;
import com.qianfu.simplerpc.serialization.serialzer.ProtostuffSerializer;
import com.qianfu.simplerpc.serialization.serialzer.Serializer;
import com.qianfu.simplerpc.sync.Sync;
import com.qianfu.simplerpc.transport.model.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Fu
 * @date 2018/6/12
 */
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private Serializer serializer = SerializerFactory.getInstance(ProtostuffSerializer.class);
    private Response response;
    private Sync sync;
    
    public ClientHandler(Sync sync) {
        this.sync = sync;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        response = serializer.deserialize(bytes, Response.class);
        sync.unlock();
    }
    
    public Response getResponse() {
        return response;
    }
}
