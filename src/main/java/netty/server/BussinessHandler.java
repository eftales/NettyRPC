package netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.service.MessagePOJO;
import org.apache.log4j.Logger;


import java.lang.reflect.Method;
import java.util.Date;

public class BussinessHandler extends SimpleChannelInboundHandler<MessagePOJO.MyQuery> {
    private static Logger log = Logger.getLogger(NettyServer.class);
    private RPCServiceImp RPCServiceImp;
    private Method[] methods;

    public BussinessHandler() {
//        RPCServiceImp rpcServiceImp =  new RPCServiceImp();
//        ClassLoader loader = rpcServiceImp.getClass().getClassLoader();
//        Class[] interfaces = rpcServiceImp.getClass().getInterfaces();
//        RPCServiceProxy rpcServiceProxy = new RPCServiceProxy(rpcServiceImp);
//        rpcService = (RPCService) Proxy.newProxyInstance(loader,interfaces,rpcServiceProxy);

        try {
            Class clz = RPCServiceImp.class;
            RPCServiceImp = (RPCServiceImp)clz.newInstance();
            methods = clz.getMethods();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Client "+ctx.channel().remoteAddress() +" connect at "+new Date());
    }

    @Override
    // 当收到一个完整的应用层消息报文，channelRead会被触发一次
    protected void channelRead0(ChannelHandlerContext ctx, MessagePOJO.MyQuery msg) throws Exception {
        MessagePOJO.MyReply.Builder reply = MessagePOJO.MyReply.newBuilder();
        reply.setMethodName(msg.getMethodName());

        // 根据 msg.getMethodName 获取需要调用的方法
        for(int i=0;i<methods.length;++i){
            if(methods[i].getName().equals(msg.getMethodName())){
                if(msg.hasStuIDs()){
                    MessagePOJO.StuInfos res =  (MessagePOJO.StuInfos)methods[i].invoke(RPCServiceImp,msg.getStuIDs());
                    reply.setStuInfos(res);

                }
                else if(msg.hasStu()){
                    boolean res =  (boolean)methods[i].invoke(RPCServiceImp,msg.hasStu());
                    reply.setSucceed(res);
                }
                else{
                    log.error("方法名和参数不匹配");
                    return;
                }

            }
        }
        log.info("RPC succeed.");
        ctx.writeAndFlush(reply);

    }

    @Override
    // 每次读取完Socket的接收缓冲区的报文，channelReadComplete会被触发一次
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println("remote close");
        ctx.close();
    }
}
