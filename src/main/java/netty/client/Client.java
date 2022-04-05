package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.service.MessagePOJO;
import netty.service.RPCService;

import java.lang.reflect.Proxy;

public class Client {
    public static void main(String[] argv) throws Exception{


        // 创建按代理类
        RPCService rpcService = (RPCService) Proxy.newProxyInstance(
                RPCService.class.getClassLoader(), // 传入ClassLoader
                new Class[] { RPCService.class }, // 传入要实现的接口
                new RPCServiceClientProxy(new NettyClient())); // 传入处理调用方法的InvocationHandler

        MessagePOJO.StuIDs.Builder stuIDs = MessagePOJO.StuIDs.newBuilder();
        stuIDs.addStuID(0).addStuID(1).addStuID(2).addStuID(3);

        MessagePOJO.StuInfos res = rpcService.getStudentInfo(stuIDs.build());
        // 打印返回值
        for(int i=0;i<res.getStusCount();++i){
            System.out.println(res.getStus(i));
        }


    }
}
