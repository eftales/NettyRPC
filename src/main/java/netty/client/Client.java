package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class Client {
    public static void main(String[] argv) throws Exception{
        System.out.println("Hello Netty Client");

        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup).
                channel(NioSocketChannel.class).
                handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ProtobufEncoder());
                        ch.pipeline().addLast(new BussinessHandler());
                    }
                });

        ChannelFuture cf =  bootstrap.connect("127.0.0.1",8888).sync();
        cf.channel().closeFuture().sync();

        workGroup.shutdownGracefully();


    }
}
