package nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NIOClient {
    public static void main(String[] argv) throws Exception{
        System.out.println("NIO Client started.");

        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress("127.0.0.1",7777));

        // 设置为非阻塞
        socketChannel.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.wrap(new String("hello~").getBytes(StandardCharsets.UTF_8));
        socketChannel.write(buffer);
//        while(true){
//
//        }
        socketChannel.close();

    }
}
