CREATE TABLE admin--用户表
(
    Username char(10) PRIMARY key not null,--用户名
    Password char(10) not null,--密码
    Name char(10) not null--用户昵称
);
CREATE TABLE S--学生信息表
(
    Sno NVARCHAR(50) PRIMARY KEY not null,--学号
    Sname NVARCHAR(50) not null,--姓名
    Sx NVARCHAR(50) not null,--系别
);
CREATE TABLE C--课程信息表
(
    Cno NVARCHAR(50) PRIMARY KEY not null,--课号
    Cname NVARCHAR(50) not null,--课名
);
CREATE TABLE SC--选课信息表
(
    Cno NVARCHAR(50) CONSTRAINT FK_C_SC FOREIGN KEY (Cno) REFERENCES C(Cno),--课号
    Sno NVARCHAR(50) CONSTRAINT FK_S_SC FOREIGN KEY (Sno) REFERENCES S(Sno),--学号
    C NVARCHAR(50) not null,--成绩
);