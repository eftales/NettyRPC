package bio;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] argv) throws Exception{
        System.out.println("BIO Server started.");

        ExecutorService executorService = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(6666);

        while (true){
            final Socket clientSock = serverSocket.accept();
            System.out.printf("Client [%s:%d] connected.\n",clientSock.getInetAddress().toString(),clientSock.getPort());
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    byte[] bytes = new byte[1024];

                    try{
                        InputStream is = clientSock.getInputStream();
                        while(true){
                            int reads = is.read(bytes);
                            if(reads!=-1){
                                System.out.println(new String(bytes,0,reads));
                            }
                            else{
                                System.out.printf("Client [%s:%d] closed.\n",clientSock.getInetAddress().toString(),clientSock.getPort());
                                break;
                            }

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    } finally {
                        try{
                            clientSock.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                }
            });

        }
    }
}
