package org.example;

import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

class SelectFace extends Face {
    String saveid2;

    SelectFace() {
        super(new String[][]{{"", "", "","",""}}, new String[]{"课号","课名", "学号","姓名", "成绩"}, "选课管理");
        add(new Background(525, 400));   //设置背景
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if(obj==allclear){
            allclearFunction();
        }
        else if (obj == chaxun) {
            if(chaxunFunction((String) tableModel.getValueAt(0, 0), (String) tableModel.getValueAt(0, 2), (String) tableModel.getValueAt(0, 4),this))
                upDataTableModel(Sms.getstr(title.length));
            Sms.closewindow();
        }
        else if (obj == tianjia) {
            tianjiaFunction((String) tableModel.getValueAt(0, 0), (String) tableModel.getValueAt(0, 2), (String) tableModel.getValueAt(0, 4),this);
            Sms.closewindow();
        }
        else if (obj == shanchu) {
            if(selectrow==-1){
                alert(this, "您还没选择需要删除的选课信息");
            }else{
                if(shanchuFunction(saveid,saveid2,this)) {
                    //同步信息
                    saveid="";
                    saveid2="";
                    upDataTableModelRow(selectrow,new String[]{"","","","",""});
                }
                Sms.closewindow();
            }
        }
        else if (obj == xiugai) {
            if(selectrow==-1){
                alert(this, "您还没选择需要修改的选课信息");
            }else{
                if(xiugaiFunction((String) tableModel.getValueAt(selectrow,0), (String) tableModel.getValueAt(selectrow,2), (String) tableModel.getValueAt(selectrow,4),saveid,saveid2,this)){
                    //同步信息
                    saveid = (String) tableModel.getValueAt(selectrow,0);
                    saveid2 = (String) tableModel.getValueAt(selectrow,2);
                }
                Sms.closewindow();
            }
        }
    }

    boolean chaxunFunction(String cno, String sno, String c, Component component) {
        String tiaojian = "";
        if (!cno.isEmpty()) tiaojian = tiaojian + "C.Cno='" + cno + "' and ";
        if (!sno.isEmpty()) tiaojian = tiaojian + "S.Sno='" + sno + "' and ";
        if (!c.isEmpty()) tiaojian = tiaojian + "SC.C='" + c + "' and ";
        if (tiaojian.isEmpty()) {
            Sms.SendMessage(createInquireSQL("C.*,S.Sno,S.Sname,SC.C","C,S,SC","SC.Cno=C.Cno and SC.Sno=S.Sno order by Cno asc,Sno asc"));
            if(Sms.getrsnext())
                return true;
            else
                alert(component, "无信息");
        } else {
            tiaojian = tiaojian + "SC.Cno=C.Cno and SC.Sno=S.Sno order by Cno asc,Sno asc";//每个结果集都会满足学号学号对应学号，课号对应课号
            Sms.SendMessage(createInquireSQL("C.*,S.Sno,S.Sname,SC.C", "C,S,SC", tiaojian));
            if (Sms.getrsnext())
                return true;
            else
                alert(component, "这类选课信息不存在！");
        }
        return false;
    }

    boolean tianjiaFunction(String cno,String sno, String c, Component component){
        if (cno.isEmpty() || sno.isEmpty() || c.isEmpty()) {
            alert(component, "选课信息请填满再录入！");
        } else {
            if (chaxunFunction(cno, sno, "", null)) {
                //判断表中是否已有对应成绩
                alert(component, "该成绩已存在，无法添加");
            } else if (!Sms.courseFace.chaxunFunction(cno, "",component)) {
                //判断表中是否有这门课
            } else if (!Sms.studentFace.chaxunFunction(sno, "", "",component)) {
                //判断是否有这个学生
            } else {
                return sendSQL(createAddSQL("SC", "'" + cno + "','" + sno + "','" + c + "'"),component);
            }
        }
        return false;
    }

    boolean shanchuFunction(String Ocno,String Osno, Component component){
        return sendSQL(createDeleteSQL("","SC","Cno='" + Ocno + "' and Sno='" + Osno + "'"),component);
    }

    boolean xiugaiFunction(String cno,String sno, String c,String Ocno,String Osno, Component component){
        if (cno.isEmpty() || sno.isEmpty() || c.isEmpty()) {
            alert(component, "选课信息填满才能修改！");
        } else if (Ocno.equals(cno) && Osno.equals(sno)) {
            //是否更换学号以及课号
            return sendSQL(createUpDataSQL("SC","Cno='" + cno + "',Sno='" + sno + "',C='" + c + "'","Cno='" + Ocno + "' and Sno='" + Osno + "'"),component);
        } else {
            if (chaxunFunction(cno,sno,"",null)) {
                //验证表中是否已有该信息
                alert(component, "已存在此选课信息");
            } else if (!Sms.courseFace.chaxunFunction(cno,"",component)) {
                //判断是否有这门课
            } else if (!Sms.studentFace.chaxunFunction(sno,"","",component)) {
                //判断是否有这个学生
            } else {
                return sendSQL(createUpDataSQL("SC","Cno='" + cno + "',Sno='" + sno + "',C='" + c + "'","Cno='" + Ocno + "' and Sno='" + Osno + "'"),component);
            }
        }
        return false;
    }

    public void valueChanged(ListSelectionEvent e) {
        selectrow = table.getSelectedRow();//保存选择行数
        if (selectrow > -1) {
            saveid = (String) (tableModel.getValueAt(selectrow, 0));//保存选择的课程号
            saveid2 = (String) (tableModel.getValueAt(selectrow, 2));//保存选择的学号
        }
    }
}
