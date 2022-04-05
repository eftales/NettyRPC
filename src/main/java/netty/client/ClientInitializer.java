package netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import netty.codec.MessagePOJO;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    public ChannelHandlerContext ctx;
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ProtobufDecoder(MessagePOJO.MyReply.getDefaultInstance())); // 指定对那种对象进行解码
        ch.pipeline().addLast(new ProtobufEncoder());
        BussinessHandler bussinessHandler = new BussinessHandler();
        ch.pipeline().addLast(bussinessHandler);
        this.ctx = bussinessHandler.ctx;
    }
}
