package other.socket;

import java.io.*;
import java.net.Socket;

/**
 * Socket Client
 * <p>
 * Created by Administrator on 2018/5/2.
 */
public class Client extends Thread{

    @Override
    public void run() {
        try {

            // 1.初始化客户端socket连接
            Socket socket = new Socket("localhost", 8888);
            // 2.client发送消息
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.write("[name:jim, pwd:123]");
            printWriter.flush();
            socket.shutdownOutput();

            // 3.client接收消息
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                System.out.println("I am Client, now get message from Server:" + str);
            }

            // 4.关闭资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();

            printWriter.close();
            outputStream.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        for(int i=0;i<10;i++){
            Client client = new Client();
            client.start();
        }
    }
}