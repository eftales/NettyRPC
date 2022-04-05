package netty.client;

import netty.service.MessagePOJO;
import netty.service.RPCService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RPCServiceClientProxy implements InvocationHandler {
    private NettyClient netty;
    private Object res;

    public RPCServiceClientProxy(NettyClient netty){
        this.netty = netty;
        netty.setProxy(this);
    }

    @Override
    public synchronized Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MessagePOJO.MyQuery.Builder query = MessagePOJO.MyQuery.newBuilder();
        query.setMethodName(method.getName());


        switch (method.getName()){
            case "getStudentInfo":
                query.setStuIDs((MessagePOJO.StuIDs)args[0]);
                break;

            case "setStudentInfo":
                query.setStu((MessagePOJO.Student)args[0]);
                break;

        }
        netty.ctx.writeAndFlush(query.build());

        synchronized(netty.ctx){
            netty.ctx.wait(); // 等待收取服务端数据
        }

        return res;

    }


    public void setRes(Object res){
        this.res = res;
    }
}
