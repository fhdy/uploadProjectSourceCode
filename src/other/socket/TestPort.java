package other.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestPort

{

    public static void main(String[] args)

    {

        System.out.println(getPID("3307"));//得到进程ID,3306是端口名称

        System.out.println(getProgramName(getPID("3306")));//根据进程ID得到映像名称

//        killTask(getProgramName(getPID("3306")));//根据映像名称关闭进程

    }

     

     

    // 得到进程ID

    public static String getPID(String port){

        InputStream is = null;

        BufferedReader br = null;

        String pid = null;

        try

        {

            String[] args = new String[]{"cmd.exe","/c","netstat -aon|findstr",port};

            is = Runtime.getRuntime().exec(args).getInputStream();

            br = new BufferedReader(new InputStreamReader(is));

            String temp = br.readLine();

            if(temp != null){

                String[] strs = temp.split("\\s");

                pid=strs[strs.length-1];

            }

        }

        catch (IOException e)

        {

            e.printStackTrace();

        }finally{

            try

            {

                br.close();

            }

            catch (IOException e)

            {

                e.printStackTrace();

            }

        }

        return pid;

    }

     

    //根据进程ID得到映像名称

    public static String getProgramName(String pid){

        InputStream is = null;

        BufferedReader br = null;

        String programName = null;

        try

        {

            String[] args = new String[]{"cmd.exe","/c","tasklist|findstr",pid};

            is = Runtime.getRuntime().exec(args).getInputStream();

            br = new BufferedReader(new InputStreamReader(is));

            String temp = br.readLine();

            if(temp != null){

                String[] strs = temp.split("\\s");

                programName=strs[0];

            }

        }

        catch (IOException e)

        {

            e.printStackTrace();

        }finally{

            try

            {

                br.close();

            }

            catch (IOException e)

            {

                e.printStackTrace();

            }

        }

        return programName;

    }

     

    //根据映像名称关闭进程

    public static void killTask(String programName){

        String[] args = new String[]{"Taskkill","/f","/IM",programName};

        try

        {

            Runtime.getRuntime().exec(args);

        }

        catch (IOException e)

        {

            e.printStackTrace();

        }

    }

}
