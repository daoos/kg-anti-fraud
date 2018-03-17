package com.jfpuhui.anti.pojo;


public class CustRelationship {

  private long id;
  private long u_Cert_Id;
  private String u_Cert_No;
  private long v_Cert_Id;
  private String v_Cert_No;
  private String content;
  private String content_Type;
  private String vid;
  private long relation_Type;
  private java.sql.Timestamp create_Time;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getU_Cert_Id() {
    return u_Cert_Id;
  }

  public void setU_Cert_Id(long u_Cert_Id) {
    this.u_Cert_Id = u_Cert_Id;
  }


  public String getU_Cert_No() {
    return u_Cert_No;
  }

  public void setU_Cert_No(String u_Cert_No) {
    this.u_Cert_No = u_Cert_No;
  }


  public long getV_Cert_Id() {
    return v_Cert_Id;
  }

  public void setV_Cert_Id(long v_Cert_Id) {
    this.v_Cert_Id = v_Cert_Id;
  }


  public String getV_Cert_No() {
    return v_Cert_No;
  }

  public void setV_Cert_No(String v_Cert_No) {
    this.v_Cert_No = v_Cert_No;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public String getContent_Type() {
    return content_Type;
  }

  public void setContent_Type(String content_Type) {
    this.content_Type = content_Type;
  }


  public String getVid() {
    return vid;
  }

  public void setVid(String vid) {
    this.vid = vid;
  }


  public long getRelation_Type() {
    return relation_Type;
  }

  public void setRelation_Type(long relation_Type) {
    this.relation_Type = relation_Type;
  }


  public java.sql.Timestamp getCreate_Time() {
    return create_Time;
  }

  public void setCreate_Time(java.sql.Timestamp create_Time) {
    this.create_Time = create_Time;
  }

}
