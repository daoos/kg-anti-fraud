package com.jfpuhui.anti.pojo.util;

/**
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-17-12:29
 */
public class Edge {

    private Long id;
    private Long source;
    private Long target;
    private String content;
    private String content_type;
    private Integer hasarrow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public Integer getHasarrow() {
        return hasarrow;
    }

    public void setHasarrow(Integer hasarrow) {
        this.hasarrow = hasarrow;
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
