package netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import netty.codec.MessagePOJO;
import org.apache.log4j.Logger;


import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BussinessHandler extends SimpleChannelInboundHandler<MessagePOJO.MyQuery> {
    private static Logger log = Logger.getLogger(Server.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Client "+ctx.channel().remoteAddress() +" connect at "+new Date());
    }

    @Override
    // 当收到一个完整的应用层消息报文，channelRead会被触发一次
    protected void channelRead0(ChannelHandlerContext ctx, MessagePOJO.MyQuery msg) throws Exception {
        List<Integer> stus =  msg.getStuIDList();

        MessagePOJO.MyReply.Builder reply = MessagePOJO.MyReply.newBuilder();
        MessagePOJO.Student.Builder stuBuilder = MessagePOJO.Student.newBuilder();

        for(int i=0;i<stus.size();++i){
            int id = stus.get(i);
            reply.addStus(stuBuilder.setName(String.valueOf(id)).setId(id).setScore(id).build());
        }

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
