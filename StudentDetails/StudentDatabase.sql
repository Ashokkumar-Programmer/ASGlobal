create database studentdetails;

use studentdetails;

create table studentdata(studentRollno bigint primary key, studentName varchar(70) not null, studentDOB date not null, studentClass varchar(10) not null, studentDOJ date not null, StudentDistrict varchar(120) not null, StudentGrade varchar(3) not null, studentFeesPaid varchar(10) not null);

select * from studentdata;