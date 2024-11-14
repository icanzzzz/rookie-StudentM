package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

//登录界面
public class Userface extends SmsJFrame{
    JLabel SystemUser, SystemPassword;//输入框前标签
    JTextField SUField;//用户名输入框
    JPasswordField SPField;//密码输入框
    JButton Load, GetOut;//登录登出按钮

    //构造窗口
    Userface() {
        super("系统登录");
        setSize( 636, 440);//设置窗口的大小
        setLocationRelativeTo(null);//设置位置
        setLayout(null);//设置容器布局
        // 控件初始化
        SystemUser = new JLabel("用户名：");
        SystemPassword = new JLabel("密码：");
        Font Ft = new Font("Serif", Font.PLAIN, 20);
        SystemUser.setFont(Ft);
        SystemPassword.setFont(Ft);
        SUField = new JTextField(12);
        SPField = new JPasswordField(12);
        Load = new JButton("登录");
        GetOut = new JButton("退出");
        SystemUser.setBounds(430,100,150,20);
        SUField.setBounds(430,130,150,30);
        SystemPassword.setBounds(430,170,150,20);
        SPField.setBounds(430,200,150,30);
        Load.setBounds(430,260,60,30);
        GetOut.setBounds(520,260,60,30);
        Load.addActionListener(this);
        //设定点击退出按钮后退出程序
        GetOut.addActionListener(this);
        //添加控件
        add(SystemUser);
        add(SUField);
        add(SystemPassword);
        add(SPField);
        add(Load);
        add(GetOut);
        add(new Background(this.getClass().getResource("/fengmian.jpg"),400,400));
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);//这个默认关闭方式甚至会关闭整个主程序
    }

    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();
        String Userid,Passid;

        if(obj==Load){
            Userid=SUField.getText();
            Passid=new String(SPField.getPassword());
            if(Userid.isEmpty()||Passid.isEmpty()||Userid.length()>10||Passid.length()>10){
                JOptionPane.showMessageDialog(this,"输入非法，请输入十位数以内账号及密码" );
            }else{
                String sql="select * from admin where Username='"+SUField.getText();
                for(int i=10-sql.length();i>0;i=10-sql.length()){
                    sql=sql+" ";
                }
                sql+="'";
                Sms.SendMessage(sql);
                if(Sms.getrsnext()){
                    for(int i=10-Passid.length();i>0;i=10-Passid.length()){
                        Passid=Passid+" ";
                    }
                    String str=Sms.getpass();
                    if(Passid.equals(str)){
                        JOptionPane.showMessageDialog(this,"登录成功");
                        if(Sms.enterFace()){
                            dispose();
                            Sms.musicBackground.switchMusic(true);
                        }else{
                            JOptionPane.showMessageDialog(this,"载入系统失败");
                        }
                    }else{
                        JOptionPane.showMessageDialog(this,"密码错误");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this,"用户名错误");
                }
                Sms.closewindow();
            }
        }else if(obj==GetOut){
            System.exit(1);
        }
    }
}
