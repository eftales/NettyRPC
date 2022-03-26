package netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import netty.codec.MessagePOJO;


public class ServerInitializer extends ChannelInitializer<SocketChannel>{

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ProtobufDecoder(MessagePOJO.MyMessage.getDefaultInstance())); // 指定对那种对象进行解码
        ch.pipeline().addLast(new BussinessHandler());

    }

}
