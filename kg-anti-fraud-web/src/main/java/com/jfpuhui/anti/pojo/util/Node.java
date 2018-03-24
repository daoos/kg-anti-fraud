package com.jfpuhui.anti.pojo.util;

/**
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-16-18:20
 */
public class Node {

    private String id;
    private String certiNo;
    private String name;
    /**
     * 客户类型: 0-白客户,1-灰色客户,2-黑名单
     */
    private Long nodeType;

    /**
     * 节点所在的度, 中心节点为0, 外围一圈为1, 在外围为2,..., 搜索条件时边时, 满足条件的所有的端点就是0度
     */
    private Integer depth;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCertiNo() {
        return certiNo;
    }

    public void setCertiNo(String certiNo) {
        this.certiNo = certiNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNodeType() {
        return nodeType;
    }

    public void setNodeType(Long nodeType) {
        this.nodeType = nodeType;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    /**node.id相同则为同一个节点
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return id.equals(node.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
