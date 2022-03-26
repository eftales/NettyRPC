package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
    public static void main(String[] argvs) throws Exception{
        System.out.println("Hello Netty Server");
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1); // 仅处理连接请求
        NioEventLoopGroup workGroup = new NioEventLoopGroup();  // 处理业务

        // 启动服务器
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class); // 使用 NioServerSocketChannel 作为服务端的通道实现
        bootstrap.option(ChannelOption.SO_BACKLOG, 128); // 设置通道参数，设置线程队列得到的线程个数
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE,true) // 设置通道参数，设置线程队列得到的线程个数
                .childHandler(new ServerInitializer());
        ChannelFuture cf = bootstrap.bind(8888).sync();
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(cf.isSuccess()){
                    System.out.println("Bind succeed");
                }
            }
        });
        // 监听通道关闭事件，如何有该事件，下面的语句才会从阻塞中返回
        cf.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
