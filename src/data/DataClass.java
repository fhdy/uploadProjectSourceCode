package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import javax.swing.JOptionPane;

public class DataClass {
	private static Map<String,String> dataMap;
    private static final String fileName = "uploadProjectPluginFile.csv";
    static {
        dataMap = new LinkedHashMap<String,String>();
        String projectPath = System.getProperty("user.dir");
        File file = new File(projectPath + File.separator + fileName);
        try {
        	if(!file.exists()) {
        		file.createNewFile();
        	}
        	
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while((line=bufferedReader.readLine()) != null){
                String[] strArr = line.split("&");
                //name ip user password preurl
                if(strArr.length==6){
                    dataMap.put(strArr[0],line);
                }
            }
            bufferedReader.close();
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    
    public void addToMap(String name,String ip,String user,String password,String url,String state,javax.swing.JFrame frame){
        String line = name+"&"+ip+"&"+user+"&"+password+"&"+url+"&"+state;
        dataMap.put(name,line);
        changeSelection(name);
        mapToFile();
    }
    
    public void deleteFromMap(String name,javax.swing.JFrame frame){
        String getLine = dataMap.get(name);
        if(getLine==null){
        	JOptionPane.showInternalMessageDialog(frame.getContentPane(), "该连接名不存在","信息", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        dataMap.remove(name);
        mapToFile();
    }


    public void mapToFile(){
        if(dataMap.size()>0){
            try {
            	initFile();
                String projectPath = System.getProperty("user.dir");
                File file = new File(projectPath + File.separator + fileName);
                OutputStream out = new FileOutputStream(file,true);
                Set<String> keys = dataMap.keySet();
                for(Object key:keys){
                    String line = dataMap.get(key);
                    out.write(line.getBytes());
                    out.write('\r');
                    out.write('\n');
                }
                out.flush();
                out.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    
    public void initFile() {
    	try {
    		String projectPath = System.getProperty("user.dir");
            File file = new File(projectPath + File.separator + fileName);
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
	}


    public String getByKey(String key) {
    	return dataMap.get(key);
    }

    public void changeSelection(String key){
        Set<String> keys = dataMap.keySet();
        List<String> lines = new ArrayList<String>();
        for(String inkey:keys){
            String line = dataMap.get(inkey);
            line = line.substring(0,line.lastIndexOf("&"))+(inkey.equals(key)?"&1":"&0");
            lines.add(line);
        }
        dataMap.clear();
        for(String line:lines){
            String inkey = line.split("&")[0];
            dataMap.put(inkey,line);
        }
    }

	public String[][] getTableList(){
        if(dataMap.size()>0){
        	Set<String> keys = dataMap.keySet();
        	String[][] arr = new String[keys.size()][1];
        	int i=0;
            for(String key:keys){
            	arr[i][0] = key;
            	i++;
            }
            return arr;
        }
        return new String[0][0];
    }
   
}
