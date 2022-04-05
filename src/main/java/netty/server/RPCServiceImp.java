package netty.server;

import netty.service.MessagePOJO;
import netty.service.RPCService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RPCServiceImp implements RPCService {
    List<MessagePOJO.Student> stus;
    int size = 10;
    public RPCServiceImp(){
        stus = new ArrayList<>(size);
        MessagePOJO.Student.Builder stuBuilder = MessagePOJO.Student.newBuilder();
        for(int i=0;i<size;++i){
            stus.add(stuBuilder.setId(i).setName(String.valueOf(i)).setScore(i).build());
        }
    }
    @Override
    public MessagePOJO.StuInfos getStudentInfo(MessagePOJO.StuIDs stuIDs) {
        MessagePOJO.StuInfos.Builder stuInfos = MessagePOJO.StuInfos.newBuilder();
        for(int i=0;i<stuIDs.getStuIDCount();++i){
            stuInfos.addStus(stus.get(stuIDs.getStuID(i)));
        }


        return stuInfos.build();
    }

    @Override
    public boolean setStudentInfo(MessagePOJO.Student stu) {
        int id = stu.getId();
        if(id>=size){
            return false;
        }

        stus.set(id,stu);
        return true;
    }
}

