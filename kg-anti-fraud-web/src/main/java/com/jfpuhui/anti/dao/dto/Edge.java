package com.jfpuhui.anti.dao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-17-12:29
 */
public class Edge implements Serializable{

    private Long id;
    /**
     * source node ID
     */
    private String source;
    /**
     * target node ID
     */
    private String target;
    private String content;
    @JsonProperty("type")
    private String contentType;
    /**
     * 是否右箭头: true-有, false-无
     */
    private Boolean directional;
    /**
     * 深度属性: 0-核,1-第一度,2-第二度,...
     */
    private Integer depth;

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Edge() {}

    public Edge(Long id, String source, String target, String content, String contentType, Boolean directional) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.content = content;
        this.contentType = contentType;
        this.directional = directional;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    public Boolean getDirectional() {
        return directional;
    }

    public void setDirectional(Boolean directional) {
        this.directional = directional;
    }

    /**edge.id一致即表示为同一条边
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        return id.equals(edge.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
