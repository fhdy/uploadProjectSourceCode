package main;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import ftp.SFTPUtil;
import ftp.ThreadUpFileUtil;
import page.UploadProjectPage;

import java.awt.*;
import java.io.File;

/**
 * Created by ABC on 16/8/17.
 */
public class UploadProjectPlugin extends AnAction {
 
    private Project mProject;


 
    @Override
    public void actionPerformed(AnActionEvent event) {
//        mProject = event.getData(PlatformDataKeys.PROJECT);
        String basePath = mProject.getBasePath();
        VirtualFile file = DataKeys.VIRTUAL_FILE.getData(event.getDataContext());
        String filePath = file.getPath();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UploadProjectPage window = new UploadProjectPage(basePath,filePath);
                    window.getFrame().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
 
    @Override
    public void update(AnActionEvent event) {
        //在Action显示之前,根据选中文件扩展名判定是否显示此Action
//        String extension = getFileExtension(event.getDataContext());
//        this.getTemplatePresentation().setEnabled(extension != null);
        boolean flag = true;
        mProject = event.getData(PlatformDataKeys.PROJECT);
        String proName = new File(mProject.getBasePath()).getName();
        VirtualFile file = DataKeys.VIRTUAL_FILE.getData(event.getDataContext());
        String filePath = file.getPath();
        if(filePath.endsWith(proName+"/src/main") || filePath.endsWith(proName+"/webapp")){
            flag = false;
        }
        this.getTemplatePresentation().setEnabled(flag);
    }
 
    public static String getFileExtension(DataContext dataContext) {
        VirtualFile file = DataKeys.VIRTUAL_FILE.getData(dataContext);
        return file == null ? null : file.getExtension();
    }

    private void doing(){

    }

    public static void main(String[] a){
//        File file = new File("D:/workspace/web/src/main/java/cn/com/mcsca/common/.util");
//        System.out.println(file.getName().startsWith("."));

    }
}