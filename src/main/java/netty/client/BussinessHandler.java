package netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import netty.codec.MessagePOJO;


import java.util.Date;

public class BussinessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello", CharsetUtil.UTF_8));

        // 发送一个 student 对象给服务器
        MessagePOJO.MyMessage.Builder msg = MessagePOJO.MyMessage.newBuilder();
        msg.setDataType(MessagePOJO.DataType.StudentType).setStudent(MessagePOJO.Student.newBuilder().setId(1).setName("s1"));
        ctx.writeAndFlush(msg);

        Thread.sleep(100); // 如果连发两个消息可能会冲掉之前的消息

        msg = MessagePOJO.MyMessage.newBuilder();
        msg.setDataType(MessagePOJO.DataType.TeacherType).setTeacher(MessagePOJO.Teacher.newBuilder().setAge(30).setName("t1"));
        ctx.writeAndFlush(msg);


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        System.out.println(buf.toString(CharsetUtil.UTF_8));
    }
}
