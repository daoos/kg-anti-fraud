package com.jfpuhui.anti.service;

import com.jfpuhui.anti.common.StatusCode;
import com.jfpuhui.anti.dao.dto.Edge;
import com.jfpuhui.anti.dao.dto.Graph;
import com.jfpuhui.anti.dao.dto.Node;
import com.jfpuhui.anti.exception.CustomException;
import com.jfpuhui.anti.mapper.CustPropertyMapper;
import com.jfpuhui.anti.mapper.CustRelationshipMapper;
import com.jfpuhui.anti.dao.pojo.CustProperty;
import com.jfpuhui.anti.dao.pojo.CustRelationship;
import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-16-17:56
 */
@Service("graphService")
@Slf4j
public class GraphServiceImpl implements GraphService {


    @Autowired
    private CustRelationshipMapper custRelationshipMapper;
    @Autowired
    private CustPropertyMapper custPropertyMapper;

    /**
     * 根据身份证号查询
     * 1 根据身份证号查询对应的节点id
     * 2 根据节点id们查询查询对应的2度边,点数据集
     *
     * @return
     */
    public Map<String, Map> select2DGraphByCertNo(String certNo) {

        //根据身份证号码查询对应的1,2度边
        List<CustRelationship> custRelationships = custRelationshipMapper.selectAll2DRelByCertNo(certNo);

        Map<String, Map> graph = new HashMap<>();
        Map<String, Edge> edges = new HashMap<>();
        HashMap<String, Node> nodes = new HashMap<>();

        //遍历边的时候, 存储点id, 自动去重
        Set<String> certIds = new HashSet<>();

        for (CustRelationship rel : custRelationships) {
            // 边
            Edge edge = new Edge();
            edge.setId(rel.getId());
            edge.setSource(rel.getuCertId());
            edge.setTarget(rel.getvCertId());
            edge.setContent(rel.getContent());
            edge.setContentType(rel.getContentType());
            edge.setDirectional(rel.getRelationType());

            edges.put(String.valueOf(edge.getId()), edge);

            //点
            //一条边对应两个点, 让set自动去重
            certIds.add(rel.getuCertId());
            certIds.add(rel.getvCertId());
        }

        //根据点id们获取所有点属性数据集
        List<CustProperty> custProperties = custPropertyMapper.selectByCertIds(certIds);

        //封装点set
        for (CustProperty record : custProperties) {
            Node node = new Node();
            node.setId(record.getCertId());
            node.setCertiNo(record.getCertNo());
            node.setName(record.getName());
            node.setNodeType(record.getCustType());

            nodes.put(String.valueOf(node.getId()), node);
        }

        graph.put("nodes", nodes);
        graph.put("edges", edges);

        return graph;
    }


    /**
     * 根据身份证号查询目标客户所有的1或者2度节点
     * 1 当所有1度节点超过36时, 不再查询2度
     * 2 1度节点数<=36时, 才去查询2度, 当1,2度之和大于36时, 只返回1度
     * Note: 以身份证号作为节点id
     *
     * @param certNo
     * @return
     */
    @Override
    public Graph selectByCertNo4Smart(@NotNull String certNo) throws CustomException {

        Graph graph4SingleCores = new GraphServiceTemplate() {
        }.getGraph4SingleCores(certNo, custRelationshipMapper, custPropertyMapper);

        return graph4SingleCores;
    }


    /**
     * 根据姓名查找图数据, 智能返回合适的结果集
     * 模糊查询匹配到的核数量可能为多核
     *
     * @param name
     * @return
     */
    public Graph selectByName4Smart(@NotNull String name) throws CustomException {
        //1 查询所有满足条件的身份证号list, 每个身份证号代表一个node
        List<String> certNos = custRelationshipMapper.selectCertNosLikeName(name);

        //有可能没有匹配到任何身份证号
        Graph graph4MultiCores = new Graph();
        if (certNos == null || certNos.size() == 0) {
            graph4MultiCores.setStatus(StatusCode.NO_MATCHED_CORE_NODES);
        }
        //用模板获取
        graph4MultiCores = new GraphServiceTemplate() {
        }.getGraph4MultiCores(certNos, custRelationshipMapper, custPropertyMapper);

        return graph4MultiCores;
    }


    /**
     * 根据客户类型查询
     * 如查出所有黑客户的核心及其一度,二度节点...
     *
     * @param custType
     * @return
     */
    public Graph selectByCustType4Smart(@NotNull Integer custType) throws CustomException {
        List<String> certNos = custRelationshipMapper.selectCertNosByCustType(custType);
        //有可能没有匹配到任何身份证号
        Graph graph4MultiCores = new Graph();
        if (certNos == null || certNos.size() == 0) {
            graph4MultiCores.setStatus(StatusCode.NO_MATCHED_CORE_NODES);
        }

        graph4MultiCores = new GraphServiceTemplate() {
        }.getGraph4MultiCores(certNos, custRelationshipMapper, custPropertyMapper);

        return graph4MultiCores;
    }

    @Override
    public Graph selectByVid4Smart(String vid) throws CustomException {
        Graph graph4MutliCores = new GraphServiceTemplate() {
        }.getGraph4MutliCores(vid, custRelationshipMapper, custPropertyMapper);

        return graph4MutliCores;
    }

    @Override
    public Graph expandGraphByCertNoBy1D(String certNo, Integer nodeDepth) {
        Graph graph = new Graph();
        List<CustRelationship> oneDepthRels = custRelationshipMapper.selectAll1DRelByCertNo(certNo);
        if (oneDepthRels.size()<0) {
            graph.setStatus(StatusCode.NO_MATCHES);
            return graph;
        }

        GraphServiceTemplate tmpl = new GraphServiceTemplate() {};
        tmpl.gatherNodeEdge(oneDepthRels,graph.getEdges(),graph.getNodes(),nodeDepth+1);

        //获取节点属性数据
        List<CustProperty> custProperties = custPropertyMapper.selectByCertNos(graph.getNodes().keySet());
        tmpl.dumpCustPropertyData(graph.getNodes(),custProperties,nodeDepth+1);


        return graph;
    }


}
