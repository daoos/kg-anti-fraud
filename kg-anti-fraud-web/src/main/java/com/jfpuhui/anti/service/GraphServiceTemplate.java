package com.jfpuhui.anti.service;

import com.jfpuhui.anti.common.StatusCode;
import com.jfpuhui.anti.exception.CustomException;
import com.jfpuhui.anti.mapper.CustPropertyMapper;
import com.jfpuhui.anti.mapper.CustRelationshipMapper;
import com.jfpuhui.anti.pojo.CustProperty;
import com.jfpuhui.anti.pojo.CustRelationship;
import com.jfpuhui.anti.pojo.util.Edge;
import com.jfpuhui.anti.pojo.util.Node;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-21-13:29
 */
public abstract class GraphServiceTemplate {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private final static int MAX_DISPLAY_NODES_1D = 37;    //一度节点最多36
    private final static int MAX_DISPLAY_NODES_2D = 109;    //1-6-3
    private final static int MAX_DISPLAY_NODES_CORE = 36;    //
    private final static int MAX_DISPLAY_EDGES = 1998;  // 37个点两两相连, 平均每两点间3条边
    //private final static int MAX_DISPLAY_EDGES_CORE = 126;  // 7个核两两相连, 平均每两点6条边


    public Map<String, Object> getGraph4MultiCores(List<String> nodeIds, CustRelationshipMapper custRelationshipMapper, CustPropertyMapper custPropertyMapper) throws CustomException {
        if (nodeIds.size() == 0) {
            throw new CustomException("[nodeIds] is empty, will don't excute any SQL");
        }

        Map<String, Object> graph = new HashMap<>();
        //2 判断身份证号list长度, >36给出提示, 条件太宽, 节点太多; <=36, 继续
        if (nodeIds.size() > MAX_DISPLAY_NODES_CORE) {   //core node 数目超限
            log.info("当前[核]数量大于{}, 将返回null",MAX_DISPLAY_NODES_CORE);
            graph.put("status", StatusCode.TOO_MANY_CORE_NODES);
            graph.put("nodes", null);
            graph.put("edges", null);
            return graph;
        }

        // -- 核数量在范围以内 --

        //仅一度边
        Map<String, Edge> oneDepthEdges = new HashMap<>();
        //一度节点, 含中心
        Map<String, Node> oneDepthNodes = new HashMap<>();

        //创建core node  方便下文区分节点深度
        for (String certNo : nodeIds) {
            Node node = new Node();
            node.setId(certNo);
            node.setDepth(0);   //core node, 可能是多个core
            //装进oneDepthNodes, core node ∈ oneDepth
            oneDepthNodes.put(node.getId(), node);
        }

        //3 继续: 查询核之间、1度的边数据集  --i.e. depth == 0
        List<CustRelationship> oneDepthRels = custRelationshipMapper.selectRelByGivenCertNos(nodeIds);
        //子类使用具体方法获取
        //List<CustRelationship> oneDepthRels = getAll1DRelByGivenNodeIds();


        //4 边数不超限, 则继续查二度边数据集
        if (oneDepthRels.size() > MAX_DISPLAY_EDGES) {
            log.info("当期一度范围内[边]大于{}, 将返回null",MAX_DISPLAY_EDGES);
            graph.put("status", StatusCode.TOO_MANY_1D_EDGES);
            graph.put("nodes", null);
            graph.put("edges", null);
            return graph;
        }

        //-- 一度边数量正常 --

        //遍历一度边, 封装数据
        //Note: 这时oneDepthRels中含所有核之间的边和第一度节点之间的边, 为了区分不同度的边, 这里需要进一步判断:
        //  当source node和target node均在oneDepthNodes中时, 表示为核边, 否则是第一度边
        for (CustRelationship rel : oneDepthRels) {
            //Note: 以身份证为node id
            Edge edge = new Edge(rel.getId(), rel.getuCertNo(), rel.getvCertNo(), rel.getContent(), rel.getContentType(), rel.getRelationType());

            //判断当前edge的depth属性
            if (oneDepthNodes.get(rel.getuCertNo()) != null && oneDepthNodes.get(rel.getvCertNo()) != null) {
                edge.setDepth(0);   //核间边
            } else {
                edge.setDepth(1);   //一度边
            }

            oneDepthEdges.put(String.valueOf(edge.getId()), edge);

            //region 收集node
            gatherNodes(oneDepthNodes, rel, 1);
            //endregion
        }

        //取出一度范围节点的key, 用于下文查二度范围的边
        Set<String> oneDepthNodesKeySet = oneDepthNodes.keySet();

        //判断一度范围节点个数, 小于阈值才会继续查询二度边
        if (oneDepthNodesKeySet.size() > MAX_DISPLAY_NODES_1D) {
            log.info("当前一度范围内[节点]个数大于[{}], 将仅返回一度节点和边数据", MAX_DISPLAY_NODES_1D);

            List<CustProperty> custProperties = custPropertyMapper.selectByCertNos(oneDepthNodesKeySet);
            //获取一度范围的点的属性数据集 子类实现
            //List<CustProperty> custProperties = getNodePropertyByNodeIds(oneDepthNodesKeySet);


            //封装点属性
            dumpCustPropertyData(oneDepthNodes, custProperties, 1);

            graph.put("nodes", oneDepthNodes);
            graph.put("edges", oneDepthEdges);
            return graph;
        }

        // -- 一度范围节点数量在范围以内, 则获取所有二度边 --

        //装所有节点
        HashMap<String, Node> nodes = (HashMap<String, Node>) ((HashMap<String, Node>) oneDepthNodes).clone();
        //装所有的边
        HashMap<String, Edge> edges = (HashMap<String, Edge>) ((HashMap<String, Edge>) oneDepthEdges).clone();

        List<CustRelationship> allRel = custRelationshipMapper.selectRelByGivenCertNos(oneDepthNodesKeySet);
        if (allRel.size() > MAX_DISPLAY_EDGES) {
            log.info("当前二度范围内[边]总数大于[{}], 仅返回一度节点和边数据", MAX_DISPLAY_EDGES);
            graph.put("nodes", oneDepthNodes);
            graph.put("edges", oneDepthEdges);
            return graph;
        }

        // -- 二度范围边在范围内, 则加工边数据 --
        for (CustRelationship rel : allRel) {
            //oneDepthEdges中已有的, 不用再封装了, 没有的, 创建, 封装, 存入edges中, 且depth设为2
            if (oneDepthEdges.get(rel.getId()) == null) {
                Edge edge = new Edge(rel.getId(), rel.getuCertNo(), rel.getvCertNo(), rel.getContent(), rel.getContentType(), rel.getRelationType());
                //oneDepthEdges没有=>depth==2
                edge.setDepth(2);
                //存入edges
                edges.put(String.valueOf(edge.getId()), edge);
            }

            //遇到新节点, 则存入nodes中, 且深度为2
            gatherNodes(nodes, rel, 2);
        }

        //查询所有属性: 核, 一度, 二度
        Set<String> allCertNos = nodes.keySet();    //获取所有身份证号, 查取属性数据
        List<CustProperty> custProperties = custPropertyMapper.selectByCertNos(allCertNos);

        //封装属性数据
        dumpCustPropertyData(nodes, custProperties, 2);

        //判断节点数目
        if (nodes.size() > MAX_DISPLAY_NODES_2D) {
            //0,1,2度节点之和超过阈值, 则只返回一度节点和边
            log.info("当前二度范围内[节点]总数大于[{}], 仅返回一度节点和边数据", MAX_DISPLAY_NODES_2D);

            graph.put("nodes", oneDepthNodes);
            graph.put("edges", oneDepthEdges);
        } else {
            //没有超限, 则返回1,2度全部
            log.info("返回全部二度范围内节点和边数据");
            graph.put("nodes", nodes);
            graph.put("edges", edges);
        }


        return graph;
    }

//    protected abstract List<CustProperty> getNodePropertyByNodeIds(Set<String> oneDepthNodesKeySet);
//
//    protected abstract List<CustRelationship> getAll1DRelByGivenNodeIds();


    public Map<String, Object> getGraph4SingleCores(String nodeId, CustRelationshipMapper custRelationshipMapper, CustPropertyMapper custPropertyMapper) throws CustomException {
        if (StringUtils.isBlank(nodeId)) {
            throw new CustomException("[nodeId] is blank null or ' '");
        }

        List<CustRelationship> oneDepthRels = custRelationshipMapper.selectAll1DRelByCertNo(nodeId);

        Map<String, Object> graph = new HashMap<>();
        if (oneDepthRels.size() == 0) {
            log.info("当前1度范围内[边]等于0, 将返回null");
            graph.put("status", StatusCode.NO_MATCHES);
            graph.put("nodes", null);
            graph.put("edges", null);
            return graph;
        }



        //仅一度边
        Map<String, Edge> oneDepthEdges = new HashMap<>();
        //一度节点, 含中心
        Map<String, Node> oneDepthNodes = new HashMap<>();

        Node core = new Node();
        core.setId(nodeId);
        core.setDepth(0);
        //core 并入 onetDepthNodes
        oneDepthNodes.put(core.getId(), core);

        //遍历一度边, 封装数据
        for (CustRelationship rel : oneDepthRels) {
            //Note: 以身份证为node id
            Edge edge = new Edge(rel.getId(), rel.getuCertNo(), rel.getvCertNo(), rel.getContent(), rel.getContentType(), rel.getRelationType());
            //因为根据唯一身份证号(node id)匹配的边数据集, 所以这里面所有的边都是1度的
            edge.setDepth(1);
            oneDepthEdges.put(String.valueOf(edge.getId()), edge);

            //region 收集node
            gatherNodes(oneDepthNodes, rel, 1);
            //endregion
        }

        Set<String> oneDepthNodesKeySet = oneDepthNodes.keySet();

        //判断一度节点个数, 小于阈值才会继续查询二度边
        if (oneDepthNodes.size() > MAX_DISPLAY_NODES_1D) {
            log.info("当前一度范围内[节点]个数大于[{}], 将仅返回一度节点和边数据", MAX_DISPLAY_NODES_1D);


            List<CustProperty> custProperties = custPropertyMapper.selectByCertNos(oneDepthNodesKeySet);

            //封装点属性
            dumpCustPropertyData(oneDepthNodes, custProperties, 1);

            graph.put("nodes", oneDepthNodes);
            graph.put("edges", oneDepthEdges);

            return graph;
        }


        // -- 一度范围节点数量在范围之内, 则获取所有二度边 --

        //装所有节点
        HashMap<String, Node> nodes = (HashMap<String, Node>) ((HashMap<String, Node>) oneDepthNodes).clone();
        //装所有的边
        HashMap<String, Edge> edges = (HashMap<String, Edge>) ((HashMap<String, Edge>) oneDepthEdges).clone();

        List<CustRelationship> allRel = custRelationshipMapper.selectRelByGivenCertNos(oneDepthNodesKeySet);
        if (allRel.size() > MAX_DISPLAY_EDGES) {
            log.info("当前二度范围内[边]总数大于[{}], 仅返回一度节点和边数据", MAX_DISPLAY_EDGES);
            graph.put("nodes", oneDepthNodes);
            graph.put("edges", oneDepthEdges);

            return graph;
        }

        // -- 二度范围边在范围之内, 则加工边数据集 --

        for (CustRelationship rel : allRel) {
            //Note: oneDepthEdges中已有的, 不用再封装了, 没有的, 创建, 封装, 存入edges中, 且depth==2
            if (oneDepthEdges.get(rel.getId()) == null) {
                Edge edge = new Edge(rel.getId(), rel.getuCertNo(), rel.getvCertNo(), rel.getContent(), rel.getContentType(), rel.getRelationType());
                //新建的边, 说明depth==2
                edge.setDepth(2);
                //存入edges
                edges.put(String.valueOf(edge.getId()), edge);
            }

            //遇到新节点, 则存入nodes中, 且深度为2 !注意别存错了!
            gatherNodes(nodes, rel, 2);
        }

        //查询所有属性: 核, 一度, 二度
        Set<String> allCertNos = nodes.keySet();    //获取所有身份证号, 查取属性数据
        List<CustProperty> custProperties = custPropertyMapper.selectByCertNos(allCertNos);

        //封装属性数据
        dumpCustPropertyData(nodes, custProperties, 2);

        //判断节点数目
        if (nodes.size() > MAX_DISPLAY_NODES_2D) {
            //0,1,2度节点之和超过阈值, 则只返回一度节点和边
            log.info("当前二度范围内[节点]总数大于[{}], 仅返回一度节点和边数据", MAX_DISPLAY_NODES_2D);

            graph.put("nodes", oneDepthNodes);
            graph.put("edges", oneDepthEdges);
        } else {
            //没有超限, 则返回1,2度全部
            log.info("返回全部二度范围内节点和边数据");
            graph.put("nodes", nodes);
            graph.put("edges", edges);
        }


        return graph;
    }


    public Map<String, Object> getGraph4MutliCores(String vid, CustRelationshipMapper custRelationshipMapper, CustPropertyMapper custPropertyMapper) throws CustomException {

        if (StringUtils.isBlank(vid)) {
            throw new CustomException("[vid] is blank");
        }

        Map<String, Object> graph = new HashMap<>();

        //1. 查出所有满足给定vid的边数据集  0度点 0度边
        List<CustRelationship> coreRels = custRelationshipMapper.selectAll0DRelByVid(vid);

        if (coreRels.size() == 0) {
            log.info("当前0度范围内[边]等于0, 将返回null");
            graph.put("status", StatusCode.NO_MATCHED_CORE_EDGES);
            graph.put("nodes", null);
            graph.put("edges", null);
            return graph;
        }

        //2. 若边范围没有超限, 则加工0度边数据集, 收集0度边, 0度点
        //存0度边
        Map<String, Edge> zeroDepthEdges = new HashMap<>();
        //存0度节点
        Map<String, Node> zeroDepthNodes = new HashMap<>();
        //遍历一度边, 封装数据
        //Note: 这时oneDepthRels中含所有核之间的边和第一度节点之间的边, 为了区分不同度的边, 这里需要进一步判断:
        //  当source node和target node均在oneDepthNodes中时, 表示为核边, 否则是第一度边
        for (CustRelationship rel : coreRels) {
            //Note: 以身份证为node id
            Edge edge = new Edge(rel.getId(), rel.getuCertNo(), rel.getvCertNo(), rel.getContent(), rel.getContentType(), rel.getRelationType());

            //判断当前edge的depth属性
           /* ABANDON: 当前数据集仅为0度边, 故边都是0度属性的
            if (zeroDepthNodes.get(rel.getuCertNo()) != null && zeroDepthNodes.get(rel.getvCertNo()) != null) {
                edge.setDepth(0);   //核间边
            } else {
                edge.setDepth(1);   //一度边
            }
           */

            edge.setDepth(0);

            zeroDepthEdges.put(String.valueOf(edge.getId()), edge);

            //region 收集node
            gatherNodes(zeroDepthNodes, rel, 0);    //均是0度node
            //endregion
        }

        /*
        * 这时可判断0度点和0度边是否均满足阈值*/
        if (zeroDepthNodes.size() > MAX_DISPLAY_NODES_CORE) { //|| zeroDepthEdges.size()>MAX_DISPLAY_EDGES_CORE
            log.info("当前[核]数量大于{}, 将返回null",MAX_DISPLAY_NODES_CORE);
            graph.put("status", StatusCode.TOO_MANY_CORE_NODES);
            graph.put("nodes", null);
            graph.put("edges", null);
            return graph;
        }

        //3. 若0度点没有超限, 则获取1度范围边数据集
        //拿到所有核的ID, 据其查一度边
        Set<String> coreNodeIds = zeroDepthNodes.keySet();
        List<CustRelationship> oneDepthRels = custRelationshipMapper.selectRelByGivenCertNos(coreNodeIds);

        if (oneDepthRels.size() > MAX_DISPLAY_EDGES) {
            log.info("当前一度范围内[边]总数大于[{}], 仅返回0度节点和边数据",MAX_DISPLAY_EDGES);
            //获取0度节点属性
            List<CustProperty> custProperties = custPropertyMapper.selectByCertNos(coreNodeIds);
            //封装点属性
            dumpCustPropertyData(zeroDepthNodes, custProperties, 0);

            graph.put("nodes", zeroDepthNodes);
            graph.put("edges", zeroDepthEdges);
            graph.put("status", StatusCode.TOO_MANY_EDGES);

            return graph;
        }




        //4. 若1度边没有超限, 则加工1度边数据集, 收集1度边, 1度点
        //装一度范围节点
        HashMap<String, Node> oneDepthNodes = (HashMap<String, Node>) ((HashMap<String, Node>) zeroDepthNodes).clone();
        //装一度范围的边
        HashMap<String, Edge> oneDepthEdges = (HashMap<String, Edge>) ((HashMap<String, Edge>) zeroDepthEdges).clone();

        for (CustRelationship rel : oneDepthRels) {
            //Note: 以身份证为node id
            Edge edge = new Edge(rel.getId(), rel.getuCertNo(), rel.getvCertNo(), rel.getContent(), rel.getContentType(), rel.getRelationType());

            //判断当前edge的depth属性
            if (zeroDepthNodes.get(rel.getuCertNo()) != null && zeroDepthNodes.get(rel.getvCertNo()) != null) {
                edge.setDepth(0);   //核间边
            } else {
                edge.setDepth(1);   //一度边
            }

            oneDepthEdges.put(String.valueOf(edge.getId()), edge);

            //region 收集node
            gatherNodes(oneDepthNodes, rel, 1);
            //endregion
        }

        Set<String> oneDepthNodeIds = oneDepthNodes.keySet();
        if (oneDepthNodeIds.size()> MAX_DISPLAY_NODES_1D) {
            log.info("当前一度范围内[节点]总数大于[{}], 仅返回0度节点和边数据", MAX_DISPLAY_NODES_1D);

            graph.put("nodes", zeroDepthNodes);
            graph.put("edges", zeroDepthEdges);
            graph.put("status", StatusCode.TOO_MANY_1D_NODES);

            return graph;

        }

        //5. 若1度点没有超限, 则获取2度边数据集
        List<CustRelationship> allRel = custRelationshipMapper.selectRelByGivenCertNos(oneDepthNodeIds);
        if (allRel.size() > MAX_DISPLAY_EDGES) {
            log.info("当前二度范围内[边]总数大于[{}], 仅返回一度节点和边数据", MAX_DISPLAY_EDGES);
            graph.put("nodes", oneDepthNodes);
            graph.put("edges", oneDepthEdges);

            return graph;
        }

        // -- 二度范围边在范围之内, 则加工边数据集 --
        //6. 若2度边没有超限, 则加工2度边数据集, 收集2度边, 2度点

        //装所有范围节点
        HashMap<String, Node> nodes = (HashMap<String, Node>) ((HashMap<String, Node>) zeroDepthNodes).clone();
        //装所有范围的边
        HashMap<String, Edge> edges = (HashMap<String, Edge>) ((HashMap<String, Edge>) zeroDepthEdges).clone();


        for (CustRelationship rel : allRel) {
            //Note: oneDepthEdges中已有的, 不用再封装了, 没有的, 创建, 封装, 存入edges中, 且depth==2
            if (oneDepthEdges.get(rel.getId()) == null) {
                Edge edge = new Edge(rel.getId(), rel.getuCertNo(), rel.getvCertNo(), rel.getContent(), rel.getContentType(), rel.getRelationType());
                //新建的边, 说明depth==2
                edge.setDepth(2);
                //存入edges
                edges.put(String.valueOf(edge.getId()), edge);
            }

            //遇到新节点, 则存入nodes中, 且深度为2 !注意别存错了!
            gatherNodes(nodes, rel, 2);
        }

        //查询所有属性: 核, 一度, 二度
        Set<String> allNodeIds = nodes.keySet();    //获取所有身份证号, 查取属性数据
        List<CustProperty> custProperties = custPropertyMapper.selectByCertNos(allNodeIds);

        //封装属性数据
        dumpCustPropertyData(nodes, custProperties, 2);

        //判断节点数目
        if (nodes.size() > MAX_DISPLAY_NODES_2D) {
            //0,1,2度节点之和超过阈值, 则只返回一度节点和边
            log.info("当前二度范围内[节点]总数大于[{}], 仅返回一度节点和边数据", MAX_DISPLAY_NODES_2D);

            graph.put("nodes", oneDepthNodes);
            graph.put("edges", oneDepthEdges);
        } else {
            //没有超限, 则返回1,2度全部
            log.info("返回全部二度范围内节点和边数据");
            graph.put("nodes", nodes);
            graph.put("edges", edges);
        }


        return graph;

    }




    private void gatherNodes(Map<String, Node> nodeMap, CustRelationship custRelationship, int depth) {
        //source node
        if (nodeMap.get(custRelationship.getuCertNo()) == null) {
            Node node = new Node();
            node.setId(custRelationship.getuCertNo());
            node.setDepth(depth);
            nodeMap.put(node.getId(), node);
        }
        //target node
        if (nodeMap.get(custRelationship.getvCertNo()) == null) {
            Node node = new Node();
            node.setId(custRelationship.getvCertNo());
            node.setDepth(depth);
            nodeMap.put(node.getId(), node);
        }
    }

    private void dumpCustPropertyData(Map<String, Node> nodes, List<CustProperty> custProperties, int depth) {
        for (CustProperty prop : custProperties) {
            Node node = nodes.get(prop.getCertNo());
            //region 正常情况下, node不会为null,因为上文已经根据CertNo创建过了, 除非属性表和关系表身份证不一致
            if (node == null) {
                node = new Node();
                node.setId(prop.getCertNo());
                node.setDepth(depth);
            }
            //endregion
            node.setCertiNo(prop.getCertNo());
            node.setName(prop.getName());
            node.setNodeType(prop.getCustType());
        }
    }
}
