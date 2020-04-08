package other.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Socket Server
 * <p>
 * Created by Administrator on 2018/5/2.
 */
public class Server {

    public static void main(String[] args) {

        try {

            // 初始化服务端socket连接，并监听8888端口的socket请求
            ServerSocket serverSocket = new ServerSocket(8888);

            System.out.println("****** I am Server, now begin to wait the client ******");

            int count = 0;
            
            // 处理socket请求
            Socket socket = null;
            while (true) {

                socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket);
                System.out.println("client host address is: " + socket.getInetAddress().getHostAddress());
                serverThread.start();
                count++;
                System.out.println("now client count is: " + count);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}