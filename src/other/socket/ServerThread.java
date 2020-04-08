package other.socket;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

/**
 * Created by Administrator on 2018/5/3.
 */
public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }
    final String reqUrl = "http://localhost:8080/datasecurity/";
    @Override
    public void run() {

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        OutputStream outputStream = null;
        PrintWriter printWriter = null;

        try {

            // server接收消息
            inputStream = socket.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String str;
            StringBuffer sb = new StringBuffer();
            if ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
//                System.out.println("I am Server, now get message from Client: " + str);
            }
            socket.shutdownInput();
            str = sb.toString();
            String result = doPost(reqUrl,"param="+str);

            // server发送消息
            outputStream = socket.getOutputStream();
            printWriter = new PrintWriter(outputStream);
            printWriter.write(result);
            printWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (outputStream != null) {
                    outputStream.close();

                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String doPost(String httpUrl, String param) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);

            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(param.getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {

                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }

    public static void main(String[] a){
        String str = "{\"reqBody\":{\"data\":{\"TEMPL_ID\":\"DLYD5d515295871a412aa098618f75488b6b\",\"TEMP_PARAM\":{\"C_AB\":\"容容添加的标签\",\"C_APPLY_TIME\":\"2019-03-19 17:37:55\",\"C_BANK_CARD_NUMBER\":\"6221888200604489572\",\"C_ID_NUMBER\":\"50023319841023791X\",\"C_LOGIN_TIME\":\"2019-03-19 17:37:05\",\"C_MOBILE\":\"13368073018\",\"DS_ATT\":[{\"hash\":\"11111111111111\",\"name\":\"fastjson1.jar\",\"url\":\"http://central.maven.org/maven2/com/alibaba/fastjson/1.2.54/fastjson-1.2.54.jar\"},{\"hash\":\"222222222222\",\"name\":\"fastjson.jar\",\"url\":\"http://central.maven.org/maven2/com/github/binarywang/weixin-java-common/3.3.2.B/weixin-java-common-3.3.2.B.jar\"}],\"DS_CONTRACT\":\"20190319173805\",\"DS_CONTRACTNAME\":\"合同名称\",\"DS_ENDTIME\":\"20190319193755000\",\"DS_FIRST_ID\":\"330721198012040211\",\"DS_MONEY\":\"123456789012345.12\",\"DS_SECOND_ID\":\"440724196201110022\",\"DS_THIRD_ID\":\"550724196201110033\"}}},\"reqHead\":{\"appId\":\"cqcbank8d91cce513874b67816ac00721ddf577\",\"businessNO\":\"业务编码\",\"methodCode\":\"B070001\",\"reqTime\":\"20190419110840768\",\"serialNo\":\"流水号\",\"token\":\"6970a4df732941b59486b8f3714918e5\"},\"sign\":\"MEYCIQDa7/zO8Jgc66ptOeNLilfWWisiN71iVzL5MPwi8lOj1wIhAOOmj1sVOr0jOa5E7VfUXKT1ETuI2Fr5Fa61oYjG8LgX\"}";
        System.out.println(doPost("119.3.100.10:10002/service/datasecurity/dsSave","param="+str));
    }
}