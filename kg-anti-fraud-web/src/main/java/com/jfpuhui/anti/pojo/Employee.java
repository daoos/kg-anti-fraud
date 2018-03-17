package com.jfpuhui.anti.pojo;


/**
 * 用于测试, 可删除
 */
public class Employee {

  private double id;
  private String name;
  private double age;
  private String sex;
  private double salary;
  private java.sql.Date empdate;
  private long deptid;


  public double getId() {
    return id;
  }

  public void setId(double id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public double getAge() {
    return age;
  }

  public void setAge(double age) {
    this.age = age;
  }


  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }


  public double getSalary() {
    return salary;
  }

  public void setSalary(double salary) {
    this.salary = salary;
  }


  public java.sql.Date getEmpdate() {
    return empdate;
  }

  public void setEmpdate(java.sql.Date empdate) {
    this.empdate = empdate;
  }


  public long getDeptid() {
    return deptid;
  }

  public void setDeptid(long deptid) {
    this.deptid = deptid;
  }

}
