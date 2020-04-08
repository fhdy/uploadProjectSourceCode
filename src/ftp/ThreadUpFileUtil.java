package ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ThreadUpFileUtil extends Thread{

    List<Map> list;
    SFTPUtil sftpUtil;

    public ThreadUpFileUtil( List<Map> list,SFTPUtil sftpUtil){
        this.list = list;
        this.sftpUtil = sftpUtil;
    }

    @Override
    public void run() {
        sftpUtil.login();
        for(Map info:list){
            File file = (File)info.get("upfile");
            String remotFileParent = (String)info.get("remotFileParent");
            String remotFileName = (String)info.get("remotFileName");
            try {
                InputStream is = new FileInputStream(file);
                sftpUtil.upload(remotFileParent, remotFileName, is);
            }catch (FileNotFoundException e1){

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        sftpUtil.logout();
    }







}
