package nio;

import java.nio.IntBuffer;

public class NIOServer {
    public static void main(String[] argv){
        System.out.println("NIO Server started.");
        IntBuffer intBuffer = IntBuffer.allocate(5);

        // 向 buffer 中存储数据
        for(int i=0;i<intBuffer.capacity();++i){
            intBuffer.put(i);
        }

        // 转换 buffer 工作模式 存<->取 默认工作模式为 存
        intBuffer.flip();
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }



    }
}
