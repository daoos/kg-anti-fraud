package com.jfpuhui.anti.pojo;


import sun.text.normalizer.IntTrie;

import java.sql.Timestamp;

public class CustRelationship {

    private long id;
    private String uCertId;
    private String uCertNo;
    private String vCertId;
    private String vCertNo;
    private String content;
    private String contentType;
    private String vid;
    /**
     * true: 有向边, false:无向边
     */
    private Boolean relationType;
    private java.sql.Timestamp createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getuCertId() {
        return uCertId;
    }

    public void setuCertId(String uCertId) {
        this.uCertId = uCertId;
    }

    public String getvCertId() {
        return vCertId;
    }

    public void setvCertId(String vCertId) {
        this.vCertId = vCertId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public Boolean getRelationType() {
        return relationType;
    }

    public void setRelationType(Boolean relationType) {
        this.relationType = relationType;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getuCertNo() {
        return uCertNo;
    }

    public void setuCertNo(String uCertNo) {
        this.uCertNo = uCertNo;
    }

    public String getvCertNo() {
        return vCertNo;
    }

    public void setvCertNo(String vCertNo) {
        this.vCertNo = vCertNo;
    }
}
