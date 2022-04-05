package netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import netty.service.MessagePOJO;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    BussinessHandler bussinessHandler = new BussinessHandler();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ProtobufDecoder(MessagePOJO.MyReply.getDefaultInstance())); // 指定对那种对象进行解码
        ch.pipeline().addLast(new ProtobufEncoder());
        ch.pipeline().addLast(bussinessHandler);

    }

    public void setProxy(RPCServiceClientProxy proxy){
        bussinessHandler.proxy = proxy;
    }

    public ChannelHandlerContext getCtx(){
        return bussinessHandler.ctx;
    }


}
