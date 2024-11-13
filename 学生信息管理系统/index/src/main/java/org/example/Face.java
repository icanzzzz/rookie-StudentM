package org.example;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Face extends SmsJFrame implements ListSelectionListener {
    JButton tianjia,shanchu,xiugai,chaxun;
    String[] title;
    TableModel tableModel;
    JTable table;
    JScrollPane scrollPane;
    int selectrow=-1,selectcolumn=-1;
    String saveid=null;
    Face(String[][] cell,String[] title,String name){
        super(name);
        setSize(525,400);
        setLocationRelativeTo(null);//设置窗口位置
        setLayout(null);
        //添加菜单
        setMenuBar(new shareMenu(this));
        //添加控件
        tianjia=new JButton("添    加");shanchu=new JButton("删    除");xiugai=new JButton("修    改");chaxun=new JButton("查    询");
        chaxun.setBounds(350,90,125,30);
        tianjia.setBounds(350,140,125,30);
        shanchu.setBounds(350,190,125,30);
        xiugai.setBounds(350,240,125,30);
        chaxun.addActionListener(this);
        tianjia.addActionListener(this);
        shanchu.addActionListener(this);
        xiugai.addActionListener(this);
        add(chaxun);add(tianjia);add(shanchu);add(xiugai);

        //设置表格格式
        this.title=title;
        tableModel=new DefaultTableModel(cell,title);
        table=new JTable();
        table.setModel(tableModel);
        table.getSelectionModel().addListSelectionListener(this);
        table.setRowHeight(20);
        scrollPane=new JScrollPane(table);
        scrollPane.setBounds(20,30,300,300);
        add(scrollPane);
        //背景在继承类中设置，因为先设置背景再设置按钮，有时候按钮会加载不出来
        //设置窗口属性
        setVisible(false);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    void upDataTableModel(String[][] cell){
        tableModel=new DefaultTableModel(cell,title);
        table.setModel(tableModel);
    }
    void upDataTableModelRow(int row,String[] strList){
        for(int i=0;i<strList.length;i++){
            tableModel.setValueAt(strList[i],row,i);
        }
    }

    static String createInquireSQL(String zhi,String biao,String tiaojian){
        return "select "+zhi+" from "+biao+" where "+tiaojian;
    }

    static String createAddSQL(String biao,String zhi){
        return "insert into "+biao+" values("+zhi+")";
    }

    static String createDeleteSQL(String zhi,String biao,String tiaojian){
        return "delete "+zhi+" from "+biao+" where "+tiaojian;
    }

    static String createUpDataSQL(String biao,String zhi,String tiaojian){
        return "update "+biao+" set "+zhi+" where "+tiaojian;
    }

    static boolean sendSQL(String sql, Component component){
        if(Sms.SendMessage(sql)){
            JOptionPane.showMessageDialog(component, "成功");
            return true;
        }else{
            JOptionPane.showMessageDialog(component, "失败");
        }
        return false;
    }

    public void valueChanged(ListSelectionEvent e) {
        selectrow=table.getSelectedRow();//保存选择行数
        if(selectrow>-1){
            saveid=(String)(tableModel.getValueAt(selectrow,0));//保存选择号
        }
    }
}

//修改图标
class SmsJFrame extends JFrame implements ActionListener{
    SmsJFrame(String name){
        super(name);
        ImageIcon imageIcon=new ImageIcon(this.getClass().getResource("/tubiao.png"));
        this.setIconImage(imageIcon.getImage());
    }

    public void actionPerformed(ActionEvent e) {

    }
}