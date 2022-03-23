package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] argv) throws Exception{
        System.out.println("NIO Server started.");

        // 创建 ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 创建 Selector
        Selector selector = Selector.open();

        // 绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(7777));

        // 设置为 非阻塞
        serverSocketChannel.configureBlocking(false);

        // 将 serverSocketChannel 上的 OP_ACCEPT 事件注册到 selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            selector.select(); // 等待连接|数据到达
            System.out.println("当前连接数目为："+selector.keys().size());
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while(keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();

                // 根据 key 的通道发生的事件做对应的处理
                if(key.isAcceptable()){
                    // 连接到达，此处的 key 只可能对应 serverSocketChannel
                    // 为客户端生成一个 SocketChannel
                    ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
                    SocketChannel socketChannel = serverChannel.accept(); // 等价于下面的代码
                    // SocketChannel socketChannel = serverSocketChannel.accept();

                    socketChannel.configureBlocking(false);

                    // 将 socketChannel 注册到 selector，同时给 socketChannel 关联一个 buffer
                    socketChannel.register(selector,SelectionKey.OP_READ,ByteBuffer.allocate(1024));

                }

                if(key.isReadable()){
                    // 通过 key 反向获取到对应的 channel
                    SocketChannel socketChannel = (SocketChannel)key.channel();

                    // 获取到该channel关联的 buffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    int readLen = socketChannel.read(buffer);
                    if(readLen>0){
                        System.out.println(new String(buffer.array(),0,readLen));
                        buffer.clear();
                    }else{
                        socketChannel.close(); // 从 selector 中删除
                        System.out.println("Peer closed.");

                    }
                }

                // 手动从集合中移除 selectorKey，防止重复操作
                keyIterator.remove();

            }
        }


    }

    void bufferDemo(){
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

        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        byteBuffer.putInt(20);
        byteBuffer.flip();
        System.out.println((int)byteBuffer.get());
        System.out.println((int)byteBuffer.get());
        System.out.println((int)byteBuffer.get());
        System.out.println((int)byteBuffer.get());

    }

    void channelDemo(){
        // write file
        String text = "hello, file channel.";
        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        try{
            FileOutputStream fileOutputStream = new FileOutputStream("text.txt");

            // 通过输出流，创建文件流
            FileChannel fileChannel = fileOutputStream.getChannel();

            // 将数据写入缓冲区
            byteBuffer.put(text.getBytes());

            // 将数据从缓冲区写入文件流
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
            fileChannel.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        byteBuffer.clear();
        // read file
        try{
            FileInputStream fileInputStream = new FileInputStream("text.txt");

            // 通过输出流，创建文件流
            FileChannel fileChannel = fileInputStream.getChannel();

            // 将数据从磁盘读入文件流
            fileChannel.read(byteBuffer);
            fileChannel.close();

            System.out.println(new String(byteBuffer.array(),0,byteBuffer.position()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
