package netty.service;

import java.util.List;

public interface RPCService {
    MessagePOJO.StuInfos getStudentInfo(MessagePOJO.StuIDs stuIDs);
    boolean setStudentInfo(MessagePOJO.Student stu);
}
