package netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import netty.codec.MessagePOJO;


import java.util.Date;
import java.util.concurrent.TimeUnit;


public class BussinessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connect at "+new Date().getTime());
//        ctx.channel().eventLoop().schedule(new Runnable() { // schedule 会先等待 TaskQueue 中的任务执行完毕，如果此时时间已到再执行
//            @Override
//            public void run() {
//
//                ctx.writeAndFlush(Unpooled.copiedBuffer("4st task, now is "+new Date().getTime(),CharsetUtil.UTF_8));
//            }
//        },5, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println();
//        ByteBuf buf = (ByteBuf)msg;
//        System.out.println("Now is "+new Date().getTime() + " "+ctx.channel().remoteAddress()+" "+ buf.toString(CharsetUtil.UTF_8));
//
//        // 不同 channel 的 TaskQueue 并发执行
//        ctx.channel().eventLoop().execute(new Runnable() { // TaskQueue 中的任务也是线性执行的！！！
//            @Override
//            public void run() {
//                try{
//                    Thread.sleep(10*1000);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//                ctx.writeAndFlush(Unpooled.copiedBuffer("1st task, now is "+new Date().getTime(),CharsetUtil.UTF_8));
//            }
//        });
//
//
//        ctx.channel().eventLoop().execute(new Runnable() { // TaskQueue 中的任务也是线性执行的！！！
//            @Override
//            public void run() {
//                try{
//                    Thread.sleep(10*1000);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                ctx.writeAndFlush(Unpooled.copiedBuffer("2st task, now is "+new Date().getTime(),CharsetUtil.UTF_8));
//            }
//        });

        if(msg instanceof MessagePOJO.MyMessage){
            MessagePOJO.MyMessage mmsg = (MessagePOJO.MyMessage) msg;


            switch (mmsg.getDataType()){
                case StudentType:
                    MessagePOJO.Student stu = mmsg.getStudent();
                    System.out.println("客户端发送的数据 id:"+stu.getId()+" name:"+stu.getName());
                    break;
                case TeacherType:
                    MessagePOJO.Teacher tch = mmsg.getTeacher();
                    System.out.println("客户端发送的数据 age:"+tch.getAge()+" name:"+tch.getName());
                    break;
            }
        }


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.channel().eventLoop().execute(new Runnable() { // 不同方法的 TaskQueue 中的任务也是顺序执行的！！！
//            @Override
//            public void run() {
//                try{
//                    Thread.sleep(10*1000);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                ctx.writeAndFlush(Unpooled.copiedBuffer("3st task, now is "+new Date().getTime(),CharsetUtil.UTF_8));
//            }
//        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println("remote close");
        ctx.close();
    }
}
