package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

class StudentFace extends Face {
    JButton chengji;
    StudentFace(){
        super(new String[][]{{"","",""}},new String[]{"学号", "姓名", "系别"}, "学生管理");
        chengji=new JButton("查 询 成 绩");
        chengji.setBounds(350,290,125,30);
        chengji.addActionListener(this);
        add(chengji);
        add(new Background(525,400));   //设置背景
    }

    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();

        if(obj==chaxun){
            if(chaxunFunction((String) tableModel.getValueAt(0,0), (String) tableModel.getValueAt(0,1), (String) tableModel.getValueAt(0,2),this))
                upDataTableModel(Sms.getstr(title.length));
            Sms.closewindow();
        }
        else if(obj==tianjia){
            tianjiaFunction((String) tableModel.getValueAt(0,0), (String) tableModel.getValueAt(0,1), (String) tableModel.getValueAt(0,2),this);
            Sms.closewindow();
        }
        else if(obj==shanchu){
            if(selectrow==-1){
                JOptionPane.showMessageDialog(this, "您还没选择需要删除的学生信息");
            }else {
                if(shanchuFunction(saveid,this)) {
                    //同步信息
                    saveid="";
                    upDataTableModelRow(selectrow,new String[]{"","",""});
                }
                Sms.closewindow();
            }
        }
        else if(obj==xiugai){
            if(selectrow==-1){
                JOptionPane.showMessageDialog(this, "您还没选择需要修改的学生信息");
            }else {
                if(xiugaiFunction((String) tableModel.getValueAt(selectrow,0), (String) tableModel.getValueAt(selectrow,1), (String) tableModel.getValueAt(selectrow,2),saveid,this)){
                    //同步信息
                    saveid = (String) tableModel.getValueAt(selectrow,0);
                }
                Sms.closewindow();
            }
        }
        else if(obj==chengji){
            if(selectrow==-1){
                JOptionPane.showMessageDialog(this, "请选择查询的目标！");
            }else{
                chengjiFunction((String) tableModel.getValueAt(selectrow,0));
            }
        }
    }

    //此函数只负责查是否有结果集，不会关闭结果集，必须使用后必须搭配Sms.closewindow()关闭结果集
    boolean chaxunFunction(String sno, String sname, String sx, Component component){
        String tiaojian="";
        //构建条件语句
        if(!sno.isEmpty())
            tiaojian=tiaojian+"Sno='"+ sno +"' and ";
        if(!sname.isEmpty())
            tiaojian=tiaojian+"Sname='"+ sname +"' and ";
        if(!sx.isEmpty())
            tiaojian=tiaojian+"Sx='"+ sx +"' and ";
        if(tiaojian.isEmpty()){
            JOptionPane.showMessageDialog(component, "请填写查询的目标！");
        }else {
            tiaojian = tiaojian.substring(0, tiaojian.length() - 5) + " order by Sno asc";
            Sms.SendMessage(createInquireSQL("*", "S", tiaojian));
            if (Sms.getrsnext()){
                return true;
            } else {
                JOptionPane.showMessageDialog(component, "这类选课信息不存在！");
            }
        }
        return false;
    }

    boolean tianjiaFunction(String sno,String sname,String sx, Component component){
        String tiaojian="";
        if(sno.isEmpty()) {
            JOptionPane.showMessageDialog(component,"学生信息请填满再录入！" );
        }else if (chaxunFunction(sno,"","",component)) {
            //查询是否已有学号
            JOptionPane.showMessageDialog(component, "该学号已存在，无法添加");
        }else{
            if(!sname.isEmpty())
                tiaojian=",'"+sname+"'";
            if(!sx.isEmpty())
                tiaojian=",'"+tiaojian+sx+"'";
            if(tiaojian.isEmpty()){
                JOptionPane.showMessageDialog(component,"学生信息请填满再录入！" );
            }else{
                tiaojian="'"+sno+"'"+tiaojian;
                return sendSQL(createAddSQL("S",tiaojian),component);
            }
        }
        return false;
    }

    boolean shanchuFunction(String Osno, Component component){
        return sendSQL(createDeleteSQL("","S","Sno='"+Osno+"'"),component);
    }

    boolean xiugaiFunction(String sno,String sname,String sx,String Osno, Component component){
        if(sno.isEmpty()||sname.isEmpty()||sx.isEmpty()){
            JOptionPane.showMessageDialog(component,"学生信息填满才能修改！" );
        }else if(Osno.equals(sno)){
            //是否更换学号
            //修改信息
            return sendSQL(createUpDataSQL("S","Sno='"+sno+"',Sname='"+ sname +"',Sx='"+ sx +"'","Sno='"+Osno+"'"),component);
        }else if(chaxunFunction(sno,"","",component)){
            //验证表中是否已有学号
            JOptionPane.showMessageDialog(component,"已存在此学号学生" );
        }else{
            //修改信息
            return sendSQL(createUpDataSQL("S","Sno='"+sno+"',Sname='"+ sname +"',Sx='"+ sx +"'","Sno='"+Osno+"'"),component);
        }
        return false;
    }

    void chengjiFunction(String sno){
        Sms.selectFace.setVisible(true);
        Sms.selectFace.chaxunFunction("", sno,"",this);
        Sms.selectFace.upDataTableModel(Sms.getstr(5));
    }
}
