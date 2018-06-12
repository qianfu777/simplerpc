package com.qianfu.simplerpc.handler;

import com.qianfu.simplerpc.serialization.factory.SerializerFactory;
import com.qianfu.simplerpc.serialization.serialzer.ProtostuffSerializer;
import com.qianfu.simplerpc.serialization.serialzer.Serializer;
import com.qianfu.simplerpc.spring.SpringContext;
import com.qianfu.simplerpc.transport.model.Request;
import com.qianfu.simplerpc.transport.model.Response;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author Fu
 * @date 2018/6/12
 */
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private Serializer serializer = SerializerFactory.getInstance(ProtostuffSerializer.class);
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        byte[] req = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(req);
        Request request = serializer.deserialize(req, Request.class);
        
        Response response = new Response(process(request));
        ctx.writeAndFlush(Unpooled.buffer().writeBytes(serializer.serialize(response)));
    }
    
    private Object process(Request request) {
        Object result = null;
        try {
            Method method = request.getMethod();
            Object serviceBean = SpringContext.getBean(request.getClazz());
            Class<?> actionClass = serviceBean.getClass();
            Method actionMethod = actionClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
    
            result =  actionMethod.invoke(serviceBean, request.getArgs());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }
}
