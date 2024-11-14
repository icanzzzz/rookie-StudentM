package org.example;
import java.sql.*;

//前后端分开，面相对象
public class Sms {
    static Userface userface;
    static CenterFace centerFace;
    static StudentFace studentFace;
    static CourseFace courseFace;
    static SelectFace selectFace;
    static Music musicBackground;
    static Connection SMconnection;
    public static void main(String[] args) {
        SMconnection=Connect();
        if(SMconnection!=null){
            userface=new Userface();
        }
    }

    //创建管理系统
    static boolean enterFace(){
        try{
            centerFace = new CenterFace();
            studentFace = new StudentFace();
            courseFace = new CourseFace();
            selectFace = new SelectFace();
            musicBackground = new Music();
            return true;
        }catch (Exception e){
            System.out.print(e.getMessage());
            return false;
        }
    }

    //SQL信息方法利用管程保证数据不会在并发中出现冲突
    static Statement stmt;
    static ResultSet rs;
    static boolean rsnext;
    static int rowcount;

    //发送SQL语句
    synchronized static boolean SendMessage(String sql){
        rsnext=false;
        rowcount=0;
        if (SMconnection != null) {
            try {
                stmt = SMconnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = stmt.executeQuery(sql);
                //调整到最后一行记录行数
                if (rs.last()) {
                    rowcount = rs.getRow();
                }
                //返回第一行
                rs.beforeFirst();
                rsnext = rs.next();
            } catch (SQLException e) {
                System.out.print("SQL Exception occur.Message is:" + e.getMessage());
            }
            return true;
        }
        return false;
    }

    //关闭获取结果
    synchronized static boolean closewindow(){
        try {
            rs.close();
            stmt.close();
            return true;
        } catch (SQLException e) {
            System.out.print("关闭失败："+e.getMessage());
            return false;
        }
    }

    //获得结果集首地址
    synchronized static boolean getrsnext(){
        return rsnext;
    }

    synchronized static int getRowcount(){
        return rowcount;
    }

    //获得结果集所编成的字符串数组，column是列数
    synchronized static String[][] getstr(int column){
        String[][] str;
        if(rowcount!=0) {
            str = new String[rowcount][column];
            ResultSet rsdd = rs;
            try {
                for (int i = 0; i < rowcount; i++) {
                    for (int j = 0; j < column; j++) {
                        str[i][j] = rsdd.getString(j + 1);
                    }
                    //进入下结果集下一行
                    rsdd.next();
                }
            } catch (SQLException e) {
                System.out.print("转化字符串失败：" + e.getMessage());
                return null;
            }
        }else{
            str=new String[1][column];
            for(int i=0;i<column;i++){
                str[0][i]="";
            }
        }
        return str;
    }

    //获得密码
    synchronized static String getpass(){
        ResultSet rsdd=rs;
        try {
            return rsdd.getString("Password");
        } catch (SQLException e) {
            System.out.print("获取管理员对应密码失败"+e.getMessage());
            return null;
        }
    }

    //连接SQL方法
    private static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=StudentInformationManage;encrypt=false";
    private static final String USER = "sa";
    private static final String PASS = "sa";

    synchronized static Connection Connect(){
        try {
            //加载驱动
            Class.forName(JDBC_DRIVER);
            //建立连接
            SMconnection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("已连接");
            return SMconnection;
        } catch (Exception e) {
            System.out.print("SQL建立连接失败："+e.getMessage());
            return null;
        }
    }
}