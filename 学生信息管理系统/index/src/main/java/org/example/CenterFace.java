package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

class CenterFace extends SmsJFrame{
    JButton student,course,select,musicButton;
    CenterFace(){
        super("学生信息管理系统");
        setSize(800,600);//设置窗口大小
        setLocationRelativeTo(null);//设置窗口位置
        setLayout(null);
        student=new JButton("学生信息管理");
        course=new JButton("课程信息管理");
        select=new JButton("选课信息管理");
        student.setBounds(300,150,200,50);
        course.setBounds(300,250,200,50);
        select.setBounds(300,350,200,50);
        student.addActionListener(this);
        course.addActionListener(this);
        select.addActionListener(this);
        add(student);add(course);add(select);
        //添加音乐控件
        ImageIcon icon=new ImageIcon(this.getClass().getResource("/laba.jfif"));
        icon.setImage(icon.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
        musicButton=new JButton(null, icon);
        musicButton.setBounds(650,450,50,50);
        musicButton.addActionListener(this);
        add(musicButton);
        //很奇怪先载入背景图这个按钮加载的就会非常慢，甚至不出现，换过来就没事了
        add(new Background(800,600));
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);//这个默认关闭方式甚至会关闭整个主程序
    }

    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();

        if(obj==musicButton){
            Sms.musicBackground.switchMusic();
        }
        else if(obj==student){
            Sms.studentFace.setVisible(true);
        }else if(obj==course){
            Sms.courseFace.setVisible(true);
        }else if(obj==select){
            Sms.selectFace.setVisible(true);
        }

    }
}
