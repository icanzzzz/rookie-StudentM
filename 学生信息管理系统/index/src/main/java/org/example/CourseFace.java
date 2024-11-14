package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

class CourseFace extends Face {
    JButton chengji;

    CourseFace() {
        super(new String[][]{{"", "", ""}}, new String[]{"课号", "课程"}, "课程管理");
        chengji = new JButton("查 询 成 绩");
        chengji.setBounds(350, 290, 125, 30);
        chengji.addActionListener(this);
        add(chengji);
        add(new Background(525, 400));   //设置背景
        add(new Background(525, 400));   //设置背景
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if(obj==allclear){
            allclearFunction();
        }
        else if (obj == chaxun) {
            if (chaxunFunction((String) tableModel.getValueAt(0, 0), (String) tableModel.getValueAt(0, 1),this))
                upDataTableModel(Sms.getstr(title.length));
            Sms.closewindow();
        }
        else if (obj == tianjia) {
            tianjiaFunction((String) tableModel.getValueAt(0, 0), (String) tableModel.getValueAt(0, 1),this);
            Sms.closewindow();
        }
        else if (obj == shanchu) {
            if (selectrow == -1) {
                alert(this, "您还没选择需要删除的课程信息");
            } else {
                if (shanchuFunction(saveid,this)) {
                    //同步信息
                    saveid = "";
                    upDataTableModelRow(selectrow, new String[]{"", ""});
                }
                Sms.closewindow();
            }
        }
        else if (obj == xiugai) {
            if (selectrow == -1) {
                alert(this, "您还没选择需要修改的课程信息");
            } else {
                if (xiugaiFunction((String) tableModel.getValueAt(selectrow, 0), (String) tableModel.getValueAt(selectrow, 1), saveid,this)) {
                    //同步信息
                    saveid = (String) tableModel.getValueAt(selectrow, 0);
                }
                Sms.closewindow();
            }
        }
        else if (obj == chengji) {
            if (selectrow == -1) {
                alert(this, "请选择查询的目标！");
            } else {
                chengjiFunction((String) tableModel.getValueAt(selectrow, 0));
            }
        }
    }

    boolean chaxunFunction(String cno, String cname, Component component) {
        String tiaojian = "";
        //构建条件语句
        if (!cno.isEmpty()) {
            tiaojian = tiaojian + "Cno='" + cno + "' and ";
        }
        if (!cname.isEmpty()) {
            tiaojian = tiaojian + "Cname='" + cname + "' and ";
        }
        if (tiaojian.isEmpty()) {
            Sms.SendMessage("select * from C order by Cno asc");
            if(Sms.getrsnext())
                return true;
            else
                alert(component,"无信息");
        } else {
            tiaojian = tiaojian.substring(0, tiaojian.length() - 5) + " order by Cno asc";
            Sms.SendMessage(createInquireSQL("*", "C", tiaojian));
            if (Sms.getrsnext())
                return true;
            else
                alert(component, "这类信息的学生不存在！");
        }
        return false;
    }

    boolean tianjiaFunction(String cno, String cname, Component component) {
        String tiaojian = "";
        if (cno.isEmpty()) {
            alert(component, "课程信息请填满再录入！");
        } else if (chaxunFunction(cno, "",null)) {
            //查询是否已有课号
            alert(component, "该课号已存在，无法添加");
        } else {
            if (!cname.isEmpty())
                tiaojian = tiaojian + ",'" + cname + "'";
            if (tiaojian.isEmpty()){
                alert(component, "课程信息请填满再录入！");
            } else {
                tiaojian = "'" + cno + "'" + tiaojian;
                return sendSQL(createAddSQL("C",tiaojian),component);
            }
        }
        return false;
    }

    boolean shanchuFunction(String Ocno, Component component) {
        return sendSQL(createDeleteSQL("","C","Cno='"+Ocno+"'"),component);
    }

    boolean xiugaiFunction(String cno, String cname, String Ocno, Component component) {
        if (cno.isEmpty() || cname.isEmpty()) {
            alert(component, "学生信息填满才能修改！");
        } else if (Ocno.equals(cno)) {
            //是否更换学号
            //修改信息
            return sendSQL(createUpDataSQL("C", "Cno='" + cno + "',Cname='" + cname + "'", "Cno='" + Ocno + "'"),component);
        } else if (chaxunFunction(cno, "",null)) {
            //验证表中是否已有学号
            alert(component, "已存在此学号学生");
        } else {
            //修改信息
            return sendSQL(createUpDataSQL("C", "Cno='" + cno + "',Cname='" + cname + "'", "Cno='" + Ocno + "'"),component);
        }
        return false;
    }

    void chengjiFunction(String cno) {
        Sms.selectFace.setVisible(true);
        Sms.selectFace.chaxunFunction(cno, "", "",this);
        Sms.selectFace.upDataTableModel(Sms.getstr(5));
    }
}