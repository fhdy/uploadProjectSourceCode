package page;

import data.DataClass;
import ftp.SFTPUtil;
import ftp.ThreadUpFileUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class UploadProjectPage {
	private JFrame frame;
	private JTextField textField_连接名;
	private JTextField textField_ip地址ַ;
	private JTextField textField_用户名;
	private JTextField textField_密码;
	private JTextField textField_路径;
	private JTable table_连接名;

	private DataClass dataClass;
	private String basePath;
	private String filePath;

	private int uploadFilesOnce = 10;//单个线程最多上传文件数量
	public String pomInfo;

	public UploadProjectPage(String basePath,String filePath){
		this.basePath = basePath;
		this.filePath = filePath;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		dataClass = new DataClass();

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100, 100, 750, 393);
		frame.getContentPane().setLayout(null);
		
		JLabel lblIp = new JLabel("ip\u5730\u5740");
		lblIp.setBounds(162, 89, 54, 15);
		frame.getContentPane().add(lblIp);
		
		JLabel lblNewLabel = new JLabel("\u7528\u6237\u540D");
		lblNewLabel.setBounds(162, 117, 54, 15);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel label = new JLabel("\u8FDE\u63A5\u540D");
		label.setBounds(162, 61, 54, 15);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("\u5BC6  \u7801");
		label_1.setBounds(162, 142, 54, 15);
		frame.getContentPane().add(label_1);
		
		JLabel label_2 = new JLabel("\u8DEF  \u5F84");
		label_2.setBounds(162, 176, 54, 15);
		frame.getContentPane().add(label_2);
		
		textField_连接名 = new JTextField();
		textField_连接名.setBounds(216, 58, 211, 21);
		frame.getContentPane().add(textField_连接名);
		textField_连接名.setColumns(10);
		
		textField_ip地址ַ = new JTextField();
		textField_ip地址ַ.setColumns(10);
		textField_ip地址ַ.setBounds(216, 86, 211, 21);
		frame.getContentPane().add(textField_ip地址ַ);
		
		textField_用户名 = new JTextField();
		textField_用户名.setColumns(10);
		textField_用户名.setBounds(216, 114, 211, 21);
		frame.getContentPane().add(textField_用户名);
		
		textField_密码 = new JTextField();
		textField_密码.setColumns(10);
		textField_密码.setBounds(216, 142, 211, 21);
		frame.getContentPane().add(textField_密码);
		
		textField_路径 = new JTextField();
		textField_路径.setColumns(10);
		textField_路径.setBounds(216, 173, 211, 21);
		frame.getContentPane().add(textField_路径);
		
		JButton btnNewButton_上传 = new JButton("\u4E0A\u4F20");
		btnNewButton_上传.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				upload();
			}
		});
		btnNewButton_上传.setBounds(162, 239, 93, 23);
		frame.getContentPane().add(btnNewButton_上传);
		
		JButton btnNewButton_取消 = new JButton("\u53D6\u6D88");
		btnNewButton_取消.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				frame.dispose();
			}
		});
		btnNewButton_取消.setBounds(334, 239, 93, 23);
		frame.getContentPane().add(btnNewButton_取消);
		
		JButton btnNewButton_新增 = new JButton("\u65B0\u589E/修改");
		btnNewButton_新增.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(!validateData()) {
					return;
				}
				String urlName =  textField_连接名.getText();
				String ip =  textField_ip地址ַ.getText();
				String username =  textField_用户名.getText();
				String password =  textField_密码.getText();
				String url =  textField_路径.getText();
				dataClass.addToMap(urlName,ip,username,password,url,"1",frame);
				int count=table_连接名.getSelectedRow();//
				if (count!=-1){
					table_连接名.getSelectionModel().clearSelection();
				}

				((AbstractTableModel)table_连接名.getModel()).fireTableDataChanged();

				int last = dataClass.getTableList().length;
				table_连接名.getSelectionModel().setSelectionInterval(last,last);


				table_连接名.validate();
				table_连接名.updateUI();
			}
		});
		btnNewButton_新增.setBounds(466, 172, 93, 23);
		frame.getContentPane().add(btnNewButton_新增);
		
		JButton btnNewButton_删除 = new JButton("\u5220\u9664");
		btnNewButton_删除.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int count=table_连接名.getSelectedRow();//
				if(-1==count) {
					JOptionPane.showInternalMessageDialog(frame.getContentPane(), "请选择需要删除的连接","信息", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String conName= table_连接名.getValueAt(count, 0).toString();//读取你获取行号的某一列的值（也就是字段）
				dataClass.deleteFromMap(conName,frame);
				((AbstractTableModel)table_连接名.getModel()).fireTableDataChanged();
				table_连接名.validate();
				table_连接名.updateUI();
				textField_连接名.setText("");
				textField_ip地址ַ.setText("");
				textField_用户名.setText("");
				textField_密码.setText("");
				textField_路径.setText("");
			}
		});
		btnNewButton_删除.setBounds(21, 239, 93, 23);
		frame.getContentPane().add(btnNewButton_删除);
		initConnetction();

	}
	
	private void initConnetction() {
		if(table_连接名!=null){
			frame.getContentPane().remove(table_连接名);
		}
		String[][] data = dataClass.getTableList();
		AbstractTableModel abstractTableModel = getTableModel();
		table_连接名 = new JTable(abstractTableModel);
		table_连接名.setRowSelectionAllowed(true);
		table_连接名.setVisible(true);
		table_连接名.setCellEditor(new TableCellEditor() {
			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
				return null;
			}

			@Override
			public Object getCellEditorValue() {
				return null;
			}

			@Override
			public boolean isCellEditable(EventObject anEvent) {
				return true;
			}

			@Override
			public boolean shouldSelectCell(EventObject anEvent) {
				return false;
			}

			@Override
			public boolean stopCellEditing() {
				return false;
			}

			@Override
			public void cancelCellEditing() {

			}

			@Override
			public void addCellEditorListener(CellEditorListener l) {

			}

			@Override
			public void removeCellEditorListener(CellEditorListener l) {

			}
		});
		table_连接名.removeEditor();
		table_连接名.setBounds(21, 21, 93, 200);
		TableColumn tableColumn = table_连接名.getColumnModel().getColumn(0);
		tableColumn.setMaxWidth(93);
		tableColumn.setMinWidth(93);
		table_连接名.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int count=table_连接名.getSelectedRow();//
				String conName= table_连接名.getValueAt(count, 0).toString();//
				dataClass.changeSelection(conName);
				String line = dataClass.getByKey(conName);
				String[] info = line.split("&");
				textField_连接名.setText(info[0]);
				textField_ip地址ַ.setText(info[1]);
				textField_用户名.setText(info[2]);
				textField_密码.setText(info[3]);
				textField_路径.setText(info[4]);
			}


		});


		for(int i=0;i<data.length;i++){
			String[] info = dataClass.getByKey(data[i][0]).split("&");
			String state = info[5];
			if("1".equals(state)){
				table_连接名.getSelectionModel().setSelectionInterval(i,i);
				textField_连接名.setText(info[0]);
				textField_ip地址ַ.setText(info[1]);
				textField_用户名.setText(info[2]);
				textField_密码.setText(info[3]);
				textField_路径.setText(info[4]);
			}
		}

		table_连接名.validate();
		table_连接名.updateUI();

		JScrollPane JSP= new JScrollPane(table_连接名);
		JSP.setBounds(21, 21, 93, 200);
		frame.getContentPane().add(JSP);
	}
	
	/**
	 * @return
	 */
	private boolean validateData() {
		 String urlName =  textField_连接名.getText();
		 String ip =  textField_ip地址ַ.getText();
		 String username =  textField_用户名.getText();
		 String password =  textField_密码.getText();
		 String url =  textField_路径.getText();
		 if("".equals(urlName) || urlName==null){
			 JOptionPane.showInternalMessageDialog(frame.getContentPane(), "连接名不能为空","信息", JOptionPane.INFORMATION_MESSAGE);
			 return false;
		 }
		 if("".equals(ip) || ip==null){
			 JOptionPane.showInternalMessageDialog(frame.getContentPane(), "ip不能为空","信息", JOptionPane.INFORMATION_MESSAGE);
			 return false;
		 }
		 if("".equals(username) || username==null){
			 JOptionPane.showInternalMessageDialog(frame.getContentPane(), "用户名不能为空","信息", JOptionPane.INFORMATION_MESSAGE);
			 return false;
		 }
		 if("".equals(password) || password==null){
			 JOptionPane.showInternalMessageDialog(frame.getContentPane(), "密码不能为空","信息", JOptionPane.INFORMATION_MESSAGE);
			 return false;
		 }
		 if("".equals(url) || url==null){
			 JOptionPane.showInternalMessageDialog(frame.getContentPane(), "路径不能为空","信息", JOptionPane.INFORMATION_MESSAGE);
			 return false;
		 }
		 return true;
	}

	public JFrame getFrame() {
		return frame;
	}

	private void upload() {
		SFTPUtil sftp = null;
		try {
			String basePath = this.basePath;
			String filePath = this.filePath;
			String str = textField_连接名.getText();
			if (null==str || "".equalsIgnoreCase(str)){
				JOptionPane.showInternalMessageDialog(frame.getContentPane(), "请选择需要上传的连接","信息", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			String username = textField_用户名.getText();
			String password = textField_密码.getText();
			String ip = textField_ip地址ַ.getText();
			String url = textField_路径.getText();

			if(filePath.endsWith(".jar")){
				sftp = new SFTPUtil(username, password, ip, 22);
				sftp.login();
				//上传jar
				File file = new File(filePath);
				InputStream is = new FileInputStream(file);
				sftp.upload(url, file.getName(), is);
				is.close();
				sftp.logout();
			}else{
				basePath = basePath.replaceAll("/","\\\\");
				filePath = filePath.replaceAll("/","\\\\");
				//上传非jar
				pomInfo = getPomInfo(basePath);
				List<Map> fileList = new ArrayList<Map>();
				getProjectFileInfoWithoutJar(basePath,new File(filePath),fileList);
				int count = fileList.size();
				if(count>0){
					uplaodFile(fileList);
				}
			}
			JOptionPane.showInternalMessageDialog(frame.getContentPane(), "上传成功","信息", JOptionPane.INFORMATION_MESSAGE);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void getProjectFileInfoWithoutJar(String basePath,File file,List fileList) throws Exception{
		String filePath = file.getPath();
		if(file.isDirectory()){//文件夹
			File[] files = file.listFiles();
			for(File item:files){
				if(item.getName().startsWith(".")){//以.开始的文件或者文件夹不上传
					continue;
				}
				getProjectFileInfoWithoutJar(basePath,item,fileList);
			}

		}else{//文件
			String path = file.getPath();
			int temp = 0;
			String javapath = basePath+File.separator+"src"+File.separator+"main"+File.separator+"java";
			String sourpath = basePath+File.separator+"src"+File.separator+"main"+File.separator+"resources";
			String webapath = basePath+File.separator+"src"+File.separator+"main"+File.separator+"webapp";
			temp = path.indexOf(javapath)>-1?1:0;
			if(temp==0){
				temp = path.indexOf(sourpath)>-1?2:0;
			}
			if(temp==0){
				temp = path.indexOf(webapath)>-1?3:0;
			}
			String realPath = "";
			String proName = pomInfo;
			String upFileUrl = basePath + File.separator +"target" + File.separator + proName;
			String remotUrl = textField_路径.getText().endsWith("/")?textField_路径.getText():textField_路径.getText()+"/";
			if(temp==1){//java
				realPath = filePath.substring(javapath.length());
				realPath = realPath.substring(0,realPath.indexOf(".java"))+".class";
				upFileUrl = upFileUrl +File.separator+ "WEB-INF" +File.separator+ "classes" + realPath;
				remotUrl = remotUrl +proName+"/WEB-INF/classes"+ realPath.replaceAll("\\\\","/");
			}else if(temp==2){//resource
				realPath = filePath.substring(sourpath.length());
				upFileUrl = upFileUrl +File.separator+ "WEB-INF" +File.separator+ "classes" + realPath;
				remotUrl = remotUrl +proName+"/WEB-INF/classes"+ realPath.replaceAll("\\\\","/");
			}else if(temp==3){//webapp
				realPath = filePath.substring(webapath.length());
				upFileUrl = upFileUrl + realPath;
				remotUrl = remotUrl +proName+ realPath.replaceAll("\\\\","/");
			}
			File upfile = new File(upFileUrl);

			Map infoMap = new HashMap();
			infoMap.put("upfile",upfile);
			infoMap.put("remotFileParent",remotUrl.substring(0,remotUrl.lastIndexOf("/")));
			infoMap.put("remotFileName",upfile.getName());
			fileList.add(infoMap);
		}
	}

	public String getPomInfo(String basePath) throws Exception{
		File file = new File(basePath + File.separator + "pom.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(basePath + File.separator + "pom.xml");
		NodeList project = document.getElementsByTagName("project");
		Node node = project.item(0);
		NodeList projectChil = node.getChildNodes();
		String artifactId = "";
		String version = "";
		String finalName = "";
		for(int i=0;i<projectChil.getLength();i++){
			Node item = projectChil.item(i);
			if(item.getNodeName().equalsIgnoreCase("artifactId")){
				artifactId = item.getTextContent();
				continue;
			}
			if(item.getNodeName().equalsIgnoreCase("version")){
				version = item.getTextContent();
				continue;
			}
			if(item.getNodeName().equalsIgnoreCase("build")){
				NodeList bulidChil = item.getChildNodes();
				for(int j=0;j<bulidChil.getLength();j++){
					Node buCh = bulidChil.item(j);
					if(buCh.getNodeName().equalsIgnoreCase("finalName")){
						finalName = buCh.getTextContent();
						continue;
					}
				}
			}
		}
		String proName = "";
		if(!"".equals(finalName)){
			proName = finalName;
		}else {
			proName = artifactId + "-" + version;
		}
		return proName;
	}

	public void uplaodFile(List<Map> list){
		System.out.println(list.size());
		if(list.size()>0){
			int count = list.size();
			int times = count/uploadFilesOnce;
			times = count%uploadFilesOnce==0?times:times+1;
			String username = textField_用户名.getText();
			String password = textField_密码.getText();
			String ip = textField_ip地址ַ.getText();
			if(times>10){
				times = 10;
				uploadFilesOnce = count%times==0?count/times:count/times+1;
			}
			System.out.println(times);
			System.out.println(uploadFilesOnce);
			for(int i=0;i<times;i++){
				List<Map> inList = new ArrayList<Map>();
				for(int j=i*uploadFilesOnce;j<(i+1)*uploadFilesOnce&&j<count;j++){
					inList.add(list.get(j));
				}
				SFTPUtil sftp = new SFTPUtil(username, password, ip, 22);
				ThreadUpFileUtil threadUpFileUtil = new ThreadUpFileUtil(inList,sftp);
				threadUpFileUtil.start();
			}

			System.out.println("over");
		}
	}

	public AbstractTableModel getTableModel() {
		return new AbstractTableModel() {
			public int getColumnCount() {
				return 1;
			}
			public int getRowCount() {
				return dataClass.getTableList().length;
			}
			public Object getValueAt(int row, int col) {
				switch (col) {
					case (0): {
						return dataClass.getTableList()[row][0];
					}
					default:
						return dataClass.getTableList()[row][0];
				}
			}
		};
	}

}
