package netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.service.MessagePOJO;
import org.apache.log4j.Logger;


import java.util.List;

public class BussinessHandler extends SimpleChannelInboundHandler<MessagePOJO.MyReply> {
    private static Logger log = Logger.getLogger(BussinessHandler.class);
    public ChannelHandlerContext ctx;
    public RPCServiceClientProxy proxy;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }


    @Override
    protected synchronized void channelRead0(ChannelHandlerContext ctx, MessagePOJO.MyReply msg) throws Exception {
        switch (msg.getMethodName()){
            case "getStudentInfo":
                proxy.setRes(msg.getStuInfos());
                break;

            case "setStudentInfo":
                proxy.setRes(msg.getSucceed());
                break;

            default:
                return;
        }


        synchronized(ctx){
            ctx.notify(); // 通知代理类数据到达
        }

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }


}
