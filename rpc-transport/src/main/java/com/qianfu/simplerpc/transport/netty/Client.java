package com.qianfu.simplerpc.transport.netty;

import com.qianfu.simplerpc.handler.ClientHandler;
import com.qianfu.simplerpc.serialization.factory.SerializerFactory;
import com.qianfu.simplerpc.serialization.serialzer.ProtostuffSerializer;
import com.qianfu.simplerpc.serialization.serialzer.Serializer;
import com.qianfu.simplerpc.sync.Sync;
import com.qianfu.simplerpc.transport.model.Request;
import com.qianfu.simplerpc.transport.model.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author Fu
 * @date 2018/6/12
 */
public class Client {
    private Serializer serializer = SerializerFactory.getInstance(ProtostuffSerializer.class);
    private Sync sync = new Sync();
    
    private int port;
    private String host;
    
    
    public Client(String host, int port) {
        this.port = port;
        this.host = host;
    }
    
    public Client(String inetAddress) {
        if (inetAddress != null && !inetAddress.isEmpty()) {
            String[] addressArr = inetAddress.split(":");
            this.host = addressArr[0];
            this.port = Integer.valueOf(addressArr[1]);
        }
    }
    
    public Response request(Request request) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            ClientHandler clientHandler = new ClientHandler(sync);
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(clientHandler);
                        }
                    });
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port)).sync();
            
            byte[] bytes = serializer.serialize(request);
            future.channel().writeAndFlush(Unpooled.buffer().writeBytes(bytes));
            sync.lock();
            
            // 等待链接关闭
            //future.channel().closeFuture().sync();
            return clientHandler.getResponse();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
