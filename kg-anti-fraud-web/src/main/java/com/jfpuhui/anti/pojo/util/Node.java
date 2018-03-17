package com.jfpuhui.anti.pojo.util;

/**
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-16-18:20
 */
public class Node {

    private Long id;
    private String certi_no;
    private String name;
    private Integer node_type;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCerti_no() {
        return certi_no;
    }

    public void setCerti_no(String certi_no) {
        this.certi_no = certi_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNode_type() {
        return node_type;
    }

    public void setNode_type(Integer node_type) {
        this.node_type = node_type;
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
