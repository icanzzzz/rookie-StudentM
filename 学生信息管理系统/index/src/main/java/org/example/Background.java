package org.example;

import javax.sound.sampled.*;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

class Background extends JLabel {
    Background(int width,int height){
//            System.out.println(this.getClass().getResource("/12.jpg"));   //测试图片路径
        URL resource=this.getClass().getResource("/12.jpg");    //图片路径
        ImageIcon icon=new ImageIcon(resource); //将图片传入图标
        icon.setImage(icon.getImage().getScaledInstance(width,height,Image.SCALE_DEFAULT)); //放缩图片大小
        setIcon(icon);  //将图标传入标签
        setBounds(0,0,width,height);
    }
}

class Music{
    private Clip clip;

    //获取音乐资源
    Music(){
        //测试路径位置
//        File file=new File("src/main/resources/The sun also rise.mp3");
//        System.out.print(file.getAbsoluteFile());
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/resources/The sun also rise.wav"));
            AudioFormat audioFormat=audioInputStream.getFormat();
            DataLine.Info info=new DataLine.Info(Clip.class,audioFormat);
            clip=(Clip) AudioSystem.getLine(info);
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.print("创建音乐失败");
        }
    }

    //播放音乐
    synchronized void switchMusic(boolean bool){
        if(bool)
            clip.start();
        else
            clip.stop();
    }
    synchronized void switchMusic(){
        if(clip.isRunning())
            clip.stop();
        else
            clip.start();
    }

}

class shareMenu extends MenuBar implements ActionListener {
    Menu musicMenu,instructionMenu;
    MenuItem openMusic,closeMusic,sInstruction,cInstruction,scInstruction;
    Component component;

    shareMenu(Component parentComponent){
        //传递父容器
        component=parentComponent;

        musicMenu=new Menu("music");instructionMenu=new Menu("instruction");
        //创建菜单项
        openMusic=new MenuItem("openMusic");closeMusic=new MenuItem("stopMusic");
        sInstruction=new MenuItem("student");cInstruction=new MenuItem("course");scInstruction=new MenuItem("selectCourse");
        //添加事件
        openMusic.addActionListener(this);closeMusic.addActionListener(this);
        sInstruction.addActionListener(this);cInstruction.addActionListener(this);scInstruction.addActionListener(this);

        //组装音乐菜单
        musicMenu.add(openMusic);musicMenu.add(closeMusic);
        //组装帮助菜单
        instructionMenu.add(sInstruction);instructionMenu.add(cInstruction);instructionMenu.add(scInstruction);

        //组装整个菜单
        add(musicMenu);add(instructionMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();
        
        if(obj==openMusic){
            Sms.musicBackground.switchMusic(true);
        }else if(obj==closeMusic){
            Sms.musicBackground.switchMusic(false);
        }else if(obj==sInstruction||obj==cInstruction){
            JOptionPane.showMessageDialog(component,
                    """
                            查询：在第一行输入需要查询的信息，点击查询
                            
                            添加：在第一行输入需要添加的信息，点击添加
                            
                            删除：选择任意一行，点击删除
                            
                            修改：选择任意一行，对其中内容进行修改，修改完毕点击修改
                            
                            查询成绩：选择任意一行，点击查询成绩""");
        }else if(obj==scInstruction){
            JOptionPane.showMessageDialog(component,
                    """
                            查询：在第一行输入需要查询的信息，点击查询
                            
                            添加：在第一行输入需要添加的信息，点击添加
                            
                            删除：选择任意一行，点击删除
                            
                            修改：选择任意一行，对其中内容进行修改，修改完毕点击修改""");
        }
    }
}