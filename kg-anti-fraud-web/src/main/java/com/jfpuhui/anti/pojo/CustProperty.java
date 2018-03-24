package com.jfpuhui.anti.pojo;


import java.io.Serializable;
import java.sql.Timestamp;

public class CustProperty implements Serializable{

    private long id;
    private String certId;
    private String certNo;
    private String name;
    /**
     * 客户类型: 0-白客户,1-灰色客户,2-黑名单
     */
    private Long custType;
    private java.sql.Timestamp createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCustType() {
        return custType;
    }

    public void setCustType(Long custType) {
        this.custType = custType;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
