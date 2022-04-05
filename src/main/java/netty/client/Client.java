package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import netty.codec.MessagePOJO;

public class Client {
    public static void main(String[] argv) throws Exception{
        System.out.println("Hello Netty Client");

        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ClientInitializer initializer = new ClientInitializer();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup).
                channel(NioSocketChannel.class).
                handler(initializer);

        ChannelFuture cf =  bootstrap.connect("127.0.0.1",8888).sync();

        // 生成信息并发送
        MessagePOJO.MyQuery.Builder msg = MessagePOJO.MyQuery.newBuilder();
        msg.addStuID(1).addStuID(2).addStuID(3);
        initializer.ctx.writeAndFlush(msg);

        cf.channel().closeFuture().sync();
        workGroup.shutdownGracefully();


    }
}
