package com.jfpuhui.anti.dao.dto;

import com.jfpuhui.anti.common.StatusCode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装响应的图形数据集
 *
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-25-19:42
 */
public class Graph implements Serializable{

    private Map<String, Node> nodes = new HashMap<>();
    private Map<String, Edge> edges = new HashMap<>();
    /**
     * 存储结果状态信息
     */
    private StatusCode.Status status;

    public Graph() {}

    public Graph(Map<String, Node> nodes, Map<String, Edge> edges, StatusCode.Status status) {
        this.nodes = nodes;
        this.edges = edges;
        this.status = status;
    }

    public Map<String, Node> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String, Node> nodes) {
        this.nodes = nodes;
    }

    public Map<String, Edge> getEdges() {
        return edges;
    }

    public void setEdges(Map<String, Edge> edges) {
        this.edges = edges;
    }

    public StatusCode.Status getStatus() {
        return status;
    }

    public void setStatus(StatusCode.Status status) {
        this.status = status;
    }
}

