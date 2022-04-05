package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    NioEventLoopGroup workGroup = new NioEventLoopGroup();
    ClientInitializer initializer = new ClientInitializer();
    public ChannelHandlerContext ctx;


    public NettyClient(){
        System.out.println("Hello Netty Client");


        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup).
                channel(NioSocketChannel.class).
                handler(initializer);

        try {
            ChannelFuture cf =  bootstrap.connect("127.0.0.1",8888).sync();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        ctx = initializer.getCtx();
    }

    public void close(){
        workGroup.shutdownGracefully();
    }

    public void setProxy(RPCServiceClientProxy proxy){
        initializer.setProxy(proxy);
    }

}
