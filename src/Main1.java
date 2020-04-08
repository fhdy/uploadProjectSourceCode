import javax.swing.*;
import java.awt.*;

public class Main1 {

    public static void main(String[] args) {
        JFrame jf = new JFrame("测试窗口");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 创建内容面板，使用边界布局
        JPanel panel = new JPanel(new BorderLayout());

        // 表头（列名）
        Object[] columnNames = {"姓名","1"};

        // 表格所有行数据
        Object[][] rowData = {
                {"张三","1"},
                {"John","1"},
                {"Sue","1"},
                {"Jane","1"},
                {"Joe","1"}
        };

        JTable table = new JTable(rowData,columnNames);
//        table.setCellSelectionEnabled(true);
//        table.setVisible(true);
//        table.removeEditor();
//        table.setBounds(21, 21, 93, 200);
        table.setRowSelectionInterval(0,0);









        // 创建一个表格，指定 所有行数据 和 表头
//        JTable table = new JTable(rowData, columnNames);
//        table.setRowSelectionInterval(0,1);
        // 把 表头 添加到容器顶部（使用普通的中间容器添加表格时，表头 和 内容 需要分开添加）
//        panel.add(table.getTableHeader(), BorderLayout.NORTH);
        // 把 表格内容 添加到容器中心
//        panel.add(table, BorderLayout.CENTER);

        JScrollPane JSP= new JScrollPane(table);
        JSP.setBounds(21, 21, 93, 200);
        panel.add(table);

        jf.setContentPane(panel);
        jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);

    }

}
