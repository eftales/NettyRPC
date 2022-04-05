package netty.client;

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

public class BussinessHandler extends SimpleChannelInboundHandler<MessagePOJO.MyReply> {
    private static Logger log = Logger.getLogger(BussinessHandler.class);
    public ChannelHandlerContext ctx;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePOJO.MyReply msg) throws Exception {
        List<MessagePOJO.Student> stus = msg.getStusList();
        for(int i=0;i<stus.size();++i){
            log.info(stus.get(i));
        }

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }
}
