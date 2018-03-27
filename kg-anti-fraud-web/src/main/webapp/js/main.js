var KgAnti = function () {
    this.context = {};
};


//接受配置参数, 创建context
//下文需要的参数都在这里面取
KgAnti.prototype.configure = function (context) {
    /*
    * context:{
    *   canvasContainer:
    *   nodeTypeContainer:
    *   edgeTypeContainer:
    *   nodeSelected:
    *   dbClickUrl:
    * }*/

    if (typeof context !== "object") {
        throw new Error("[context] must be a type of [object]");
    }


    this.context = context;

    //外界可配的部分
    // if (context.graph == null) {
    //     try {
    //         this.context.graph = new Springy.Graph();
    //     } catch (e) {
    //         console.error("未找到依赖插件:[Spring.js]");
    //     }
    // }

};


KgAnti.prototype.springyGraph = function (graph) {
    // $('#springydemo').springy({
    this.context.canvasContainer.springy({
        graph: graph,
        nodeSelected: this.context.nodeSelected
    });
    var layout = new Springy.Layout.ForceDirected(graph, 640, 480.0, 0.5);
};


/**
 * 加载后台数据并画图
 * */
KgAnti.prototype.loadDataAndDraw = function (requestArguments) {
    if (requestArguments.url === undefined || requestArguments.url === null) {
        return;
    }


//    $.getJSON("../json/data21.json", function (data) {
//     var reqUrl = "/certno/110101195607302022.do";   //全局搜索定义的请求路径
    /* ABANDON: 貌似无法post请求
    $.getJSON(requestArguments.url,requestArguments.data, function (data) {
        console.log(requestArguments);
        console.log(requestArguments.url);
        console.log(requestArguments.data);

        //获取数据
        var json = JSON.stringify(data);
        var data = JSON.parse(json);
        //判断从后台获取的数据是否格式正确
        if (data.nodes === undefined || data.edges === undefined) {
            throw new Error("后台相应的json格式有误");
        }

        this.context.data = data;
        //初始化图形  将查到的所有节点和边全部展示
        this.initGraph(this.context);


        //根据nodeTypeSet中的nodeType显示复选框列表
        var nodeTypeHtml = this.genCategoryHtml(graphArgs.nodeTypeSet, "node");
        var edgeTypeHtml = this.genCategoryHtml(graphArgs.edgeTypeSet, 'edge');

        //todo 返回默认分类html, 放入属性中, 供外面调用
        //$('#nodeTypes').html(nodeTypeHtml);
        //$('#edgeTypes').html(edgeTypeHtml);
    }.bind(this));
    */  //回调函数绑定当前类实例, 否则, this指向的是其他域
    $.ajax({
        type: 'POST',
        url: requestArguments.url,
        data: requestArguments.data,
        success: function (data) {
            console.log(requestArguments);
            console.log(requestArguments.url);
            console.log(requestArguments.data);

            //获取数据
            var json = JSON.stringify(data);
            var data = JSON.parse(json);
            //判断从后台获取的数据是否格式正确
            if (data.nodes === undefined || data.edges === undefined) {
                throw new Error("后台响应的json格式有误");
            }
            //判断结果给出提示
            if (data.status != null) {
                alert(data.status.msg);
                return;
            }

            //创建Graph实例     没有数据不会被创建 解决没有数据导致以后闪屏问题
            this.context.graph = new Springy.Graph();

            this.context.data = data;
            //初始化图形  将查到的所有节点和边全部展示
            this.initGraph(this.context);


            //根据nodeTypeSet中的nodeType显示分类按钮
            this.genCategoryHtml("node");
            this.genCategoryHtml('edge');

            //画图
            this.springyGraph(this.context.graph);


        }.bind(this),
        dataType: 'json'
    });

}


//
// jQuery(function () {
//     var springy = window.springy = jQuery('#springydemo').springy({
//         graph: graph,
//         nodeSelected: function (node, e) {
//             console.log(e);
//             $('#showInfo').html(JSON.stringify(node.data));
// //                alert('Node selected: ' + JSON.stringify(node.data));
//             //$('body').append('<div style="width: 200px;height: 100px;background: #333;margin-left:'+e.pageX+'margin-top:"'++'";></div>');
//             //$('.shader').html(JSON.stringify(node.data)).show().css({"left":e.pageX+10+"px","top":e.pageY+10+"px"});
//         }
//
//
//     });
//     var layout = new Springy.Layout.ForceDirected(graph, 640, 480.0, 0.5);
//     console.log(layout.graph.nodes);
// });
//END: 业务代码


//START: 方法区
/**根据contentType映射出颜色配置*/
function mapColorFromEdgeType(edgeType) {
    //TODO
    return "#990677";
}

/**根据contentType映射出颜色配置*/
function mapColorFromNodeType(nodeType) {
    //TODO
    return "#A8E667";
}


/**切换显示更深的节点和关联的边
 * 双击触发次方法
 * 显示时, 优先级低于类别, 只显示关联的一级deeper的节点和边
 var doDisplay = false;
 * 隐藏时, 优先级高于类别, 递归隐藏所有级别deeper*/
KgAnti.prototype.toggleDisplayDeeper = function (node) {
    if (node.data.depth == null) {
        return;
    }

    var doShow = true;


    /**递归搜索收集以备删除.
     * 满足: 1.属于当前显示类别的节点. 2.度更深*/
    function filterNodes4Remove(adjacency, reverseAdjacency, node, nodeSet, nodes4Remove) {
        //正向边
        if (adjacency[node.id] != null) {
            for (var relatedId in adjacency[node.id]) {
                var relatedNode = nodeSet[relatedId];
                if (relatedNode.data != null && relatedNode.data.depth == null) {
                    console.warn("No property of [depth], can't by remove by depth: node ID==" + relatedNode.id);
                    return;
                }
                if (relatedNode.data.depth > node.data.depth) {
                    //收集这个node, 以供删除
                    nodes4Remove[relatedNode.id] = relatedNode;
                    //递归搜索满足条件的下一级node
                    filterNodes4Remove(adjacency, reverseAdjacency, relatedNode, nodeSet, nodes4Remove);
                }
            }
        }
        //反向边
        if (reverseAdjacency[node.id] != null) {
            for (var relatedId in reverseAdjacency[node.id]) {
                var relatedNode = nodeSet[relatedId];
                if (relatedNode.data != null && relatedNode.data.depth == null) {
                    console.warn("No property of [depth], can't by remove by depth: node ID==" + relatedNode.id);
                    return;
                }
                if (relatedNode.data.depth > node.data.depth) {
                    //收集这个node, 以供删除
                    nodes4Remove[relatedNode.id] = relatedNode;
                    //递归搜索满足条件的下一级node
                    filterNodes4Remove(adjacency, reverseAdjacency, relatedNode, nodeSet, nodes4Remove);
                }
            }
        }
    }

    var nodes4Remove = {};
    /*graph.adjacency和graph.reverseAdjacency中一旦找到了关联的低级node,
    则: 1.本次任务是要隐藏; 2.干掉遇到的所有node*/
    filterNodes4Remove(this.context.graph.adjacency, this.context.graph.reverseAdjacency, node, this.context.graph.nodeSet, nodes4Remove);

    if (Object.keys(nodes4Remove).length > 0) {
        doShow = false;
        //批量删除
        for (var nodeId in nodes4Remove) {
            this.context.graph.removeNode(nodes4Remove[nodeId]);
        }
    }


    /**从缓存中搜寻关联节点和边, 并添加
     * @return hasCacheAndAddSuccess true, 表示缓存中有低级数据并*/
    function addNEFromCache(baseNode, graph, adjacency) {
        var hasCacheAndAddSuccess = false;
        for (var relatedNodeId in adjacency[baseNode.id]) {
            //关联node满足:1.深一度;2.属于当前显示类别中
            var relatedNode = graph.graphCache.nodeSet[relatedNodeId];

            var needDisplay = true;
            //
            try { //约定: 没有depth属性的点, 不受双击展开/收缩约束
                if (relatedNode.data.depth <= baseNode.data.depth) {
                    needDisplay = false;
                } else {
                    //所有关联节点中, 只要有一个满足低级条件, 即表示当前缓存中有数据
                    hasCacheAndAddSuccess = true;
                }
            } catch (e) {
                hasCacheAndAddSuccess = false;  //关联节点没有depth属性时, 需要额外获取数据
                console.info("No property of [depth], can't by add by depth: node ID==" + relatedNode.id);
            }

            try {   //约定: 没有data.type属性的点不受类型控制, 永远显示
                if (!(relatedNode.data.type in graph.nodeTypeSet)) {
                    needDisplay = false;
                }
            } catch (e) {
            }

            if (needDisplay) {
                graph.addNode(relatedNode);
                var edges = adjacency[baseNode.id][relatedNodeId];
                graph.addEdgesIncludedInShowedEdgeType(edges);
            }
        }
        return hasCacheAndAddSuccess;
    }

    function requestDeeperGDAndExpandGraph(requestUrl, baseNode, graph) {
        var self = this;
        $.ajax({
            type: 'POST',
            url: requestUrl,
            data: {nodeId: baseNode.id, nodeDepth: node.data.depth},
            success: function (data) {
                console.log('双击节点获取的数据');
                console.log(data);
                console.log("[self] 指向KgAnti实例吗:");
                console.log(self);
                //扩展图形网络
                var nodes = {};
                var edges = {};
                if (data != null && data.nodes != null) {
                    for (var nodeId in data.nodes) {
                        var node = new Springy.Node(nodeId, data.nodes[nodeId]);    //后台支撑数据结构: nodeId:node
                        self.initNode(node);
                        nodes[node.id] = node;
                    }
                    console.log(nodes);
                }
                if (data != null && data.edges != null) {
                    for (var edgeId in data.edges) {
                        var edgeData = data.edges[edgeId];
                        var source = nodes[edgeData.source];
                        var target = nodes[edgeData.target];
                        var edge = new Springy.Edge(edgeId, source, target, edgeData);
                        self.initEdge(edge);
                        edges[edge.id] = edge;
                    }
                    console.log(edges);
                }
                graph.expandGraph({nodes: nodes, edges: edges});

            }.bind(self),
            dataType: 'json'
        });
    }

    //本次执行显示任务
    if (doShow) {
        //去缓存里找下一级关联点边,并添加
        var hasCacheAndAddSuccess = addNEFromCache(node, this.context.graph, this.context.graph.graphCache.adjacency);
        var hasCacheAndAddSuccess_rev = addNEFromCache(node, this.context.graph, this.context.graph.graphCache.reverseAdjacency);
        //若没有缓存 -> 请求数据
        if (!(hasCacheAndAddSuccess || hasCacheAndAddSuccess_rev)) {
            requestDeeperGDAndExpandGraph.call(this, this.context.dbClickUrl, node, this.context.graph);
        }

    }


    /* ABANDON: 业务逻辑改变
    var oneLevelNeighborEdges = {}; //存储一度边
    $.getJSON(url, {nodeId: nodeId}, function (dbclickGetData) {
        //start: 根据当前节点的id从后台获取其周边一度关联节点数据  --!根据当前节点id筛选之后的!
        var dbclickGetData = JSON.stringify(dbclickGetData);
        var dbclickGetData = JSON.parse(dbclickGetData);


        //从后台传入的数据集中, 获取所有一度关联的边
        dbclickGetData.edges.forEach(function (e) {
            //一度关联节点: sourceId一致或targetId一致
            if (e.source == nodeId || e.target == nodeId) {
                oneLevelNeighborEdges[e.id] = e;
            }
        });

        //当前节点关联的真实边数(来自数据库)
        var oneLevelNeighborEdgesNum = Object.getOwnPropertyNames(oneLevelNeighborEdges).length;
        //end

        //计算当前图中此节点与一度周边节点相连接的边数
        var count = 0;
        var adjacency = args.graph.adjacency;
        for (var source in adjacency) {
            //sourceId 一致, 其上所有targetId包括在内
            if (source == nodeId) {
                //获取source:{key:value,....}key的个数, 即targetId的个数
                count += Object.getOwnPropertyNames(adjacency[source]).length;
                //sourceId一致说明肯定是周边节点
            } else {
                //否则, 需要注意判断targetId是否一致, 若是, 则也是属于周边节点
                for (var target in adjacency[source]) {
                    if (target == nodeId) {
                        count++;
                    }
                }
            }
        }

        //比较实际变数和图中当前显示的变数
        if (count < oneLevelNeighborEdgesNum) {
            //说明当前为显示全部一度变数-->全部展示出来
            args.data = dbclickGetData;
            initGraph(args);   //'递归'调用
        } else {
            //说明当前一度变数处于全部显示状态-->删除(这里仅删除边)
            if (oneLevelNeighborEdges != null && oneLevelNeighborEdges.length > 0) {
                oneLevelNeighborEdges.forEach(function (t) {
                    graph.removeEdge(t);
                })
            }
        }

    })
    */

};


/**初始化图形  将查到的所有节点和边全部展示
 * @param args graph:图对象; data:数据集nodes:[]  edges:[]*/
KgAnti.prototype.initGraph = function () {

    var data = this.context.data;
    var graph = this.context.graph;

    if (data == null) {
        return;
    }

    /*
    * 节点id和边id为后台传入的id
    *
    * */
    var nodes = data.nodes;
    if (nodes != null) {
        for (var nodeKey in nodes) {
            //获取节点
            var node = new Springy.Node(nodeKey, nodes[nodeKey]);    //后台支撑数据结构: nodeId:node
            this.initNode(node);
            console.log("initNode()后:");
            console.log(node);
            graph.addNode(node);

            //将节点类型记录下来, 用于页面显示  nodeType:[nodeId,...]
            /* ABANDON: springy.js中已有搞定
            if (!(nodeData.nodeType in this.context.nodeTypeSet)) {
                this.context.nodeTypeSet[nodeData.nodeType] = [];
            }
            if ($.inArray(nodeKey, this.context.nodeTypeSet[nodeData.nodeType]) == -1) {    //去重
                this.context.nodeTypeSet[nodeData.nodeType].push(nodeKey);
            }
            */
        }


    }

    var edges = data.edges;
    if (edges != null) {
        for (var edgeKey in edges) {
            //根据节点id在上述已经创建的node中找出node
            var source = graph.nodeSet[edges[edgeKey].source];
            var target = graph.nodeSet[edges[edgeKey].target];
            //建立边
            var edge = new Springy.Edge(edgeKey, source, target, edges[edgeKey]);

            this.initEdge(edge);

            //vid 由 content和contentType取md5唯一确定出的边
            graph.addEdge(edge);

            //将边类型记录下来 <edgeType:[edgeId,....]>
            // if (!(edgeData.contentType in this.context.edgeTypeSet)) {
            //     this.context.edgeTypeSet[edgeData.contentType] = [];
            // }
            // if ($.inArray(edgeKey, this.context.edgeTypeSet[edgeData.contentType]) == -1) {
            //     this.context.edgeTypeSet[edgeData.contentType].push(edgeKey);
            // }
        }
    }

    console.log("[initGraph()]完成, 图形参数:");
    console.log("this.context.graph.graphCache.nodeTypeSet");
    console.log(this.context.graph.graphCache.nodeTypeSet);
    console.log("this.context.graph.edgeTypeSet");
    console.log(this.context.graph.edgeTypeSet);
    console.log("this.context.graph.graphCache.edgeTypeSet");
    console.log(this.context.graph.graphCache.edgeTypeSet);

}


/**配置点*/
KgAnti.prototype.initNode = function (node) {
    node.data.label = node.data.name;     //可灵活指定用什么字段作为label
    //配置节点字体样式
    node.data.font = "30px Verdana, sans-serif";
    //填充颜色
    node.data.fillColor = 'rgba(90,25,222,0.25)';
    //文本颜色
    node.data.color = "#993366";
    //显示图片
    // node.data.image = ;
    node.data.radius = 35;


    // 添加双击事件, 展示/收起周边节点
    node.data.ondoubleclick = function () {
        //判断当前节点是否显示了全部周边节点-->然后据此展开还是收缩
        //alert('double click');
        this.toggleDisplayDeeper(node);
    }.bind(this);
};
KgAnti.prototype.initEdge = function (edge) {
    edge.data.color = mapColorFromEdgeType(edge.data.contentType);
    edge.data.label = edge.data.type + "\n" + edge.data.content;      //可根据需求灵活配置边上显示的内容
    edge.data.font = "20px Verdana, sans-serif";
};


/**根据复选框状态切换被选中类别的显示或隐藏*/
KgAnti.prototype.toggleDisplayByType = function (categoryLi, graphType) {
    var flag;
    if (categoryLi.hasClass("color-grey")) {
        //切换按钮样式
        categoryLi.removeClass("color-grey");
        categoryLi.find('span').removeClass("bg-grey");
        flag = true;
    } else {
        categoryLi.addClass("color-grey");
        categoryLi.find('span').addClass("bg-grey");
        flag = false;
    }

    var typeName = categoryLi.attr("typeName");
    if (typeName === undefined) {    //考虑兼容性
        typeName = categoryLi.prop("typeName");
    }
    if (typeName === undefined) {
        throw new Error('未获取[typeName], 将无法确定切换显示哪一类点/边元素');
    }

    //删除此类别下的所有图形元素
    //var selectedType = categoryLi.val();
    switch (graphType) {
        case 'node':
            //取出全局变量this.context中的 *TypeSet
            if (flag) {  //要显示
                //根据nodeId 在 Springy.graphCache中找到对应点, 然后恢复显示
                this.context.graph.recoverNodeByType(typeName);
            } else {  //要隐藏
                //批量删除节点
                this.context.graph.removeNodeByType(typeName);
            }
            console.log("[toggleDisplayByType] this.context.graph.nodeTypeSet");
            console.log(this.context.graph.nodeTypeSet);
            console.log("[toggleDisplayByType] this.context.graph.graphCache.nodeTypeSet 缓存:");
            console.log(this.context.graph.graphCache.nodeTypeSet);

            console.log("this.context.graph.edgeTypeSet");
            console.log(this.context.graph.edgeTypeSet);
            console.log("this.context.graph.graphCache.edgeTypeSet");
            console.log(this.context.graph.graphCache.edgeTypeSet);

            break;
        case 'edge':
            //取出全局变量this.context中的 *TypeSet
            if (flag) {  //要显示
                //根据nodeId 在 graph.graphCache中找到对应点, 然后恢复显示
                this.context.graph.recoverEdgeByType(typeName);
            } else {  //要隐藏
                //批量删除节点
                this.context.graph.removeEdgeByType(typeName);
            }
            console.log("[toggleDisplayByType]  this.context.graph.edgeTypeSet");
            console.log(this.context.graph.edgeTypeSet);
            console.log("[toggleDisplayByType] 缓存: ");
            console.log(this.context.graph.graphCache.edgeTypeSet);
            break;
        default:
            throw new Error("node? edge?");
    }


}

/**根据节点类别或边类别生成分类选择列表的html
 * @param typeSet 节点或边类型set
 * @param graphType 'node'  'edge'*/
KgAnti.prototype.genCategoryHtml = function (graphType) {
    // console.log(typeof graphType);
    // var html = '';
    // for (var type in typeSet) {
    //     html += '<input class="' + graphType + '" type="checkbox" checked="checked" onclick="toggleDisplayByType($(this),\'' + graphType + '\')" value="' + type + '"/>' + type
    // }
    var categoryClass = '';
    var typeContainer;
    var typeSet;
    if (graphType === 'node') {
        categoryClass = 'dot';   //点
        typeContainer = $(this.context.nodeTypeContainer);
        typeSet = this.context.graph.nodeTypeSet;
    }
    if (graphType === 'edge') {
        categoryClass = 'line'; //线
        typeContainer = $(this.context.edgeTypeContainer);
        typeSet = this.context.graph.edgeTypeSet;
    }


    var categoryLi = '';
    var categoryColor;
    //遍历typeSet, 动态生成分类开关按钮
    for (var typeKey in typeSet) {
        //todo
        categoryColor = mapColorFromNodeType(typeKey);
        categoryLi += '<li typeName="' + typeKey + '" class="categoryLi_' + graphType + '"><span style="background-color:' + categoryColor + ';"></span>' + typeKey + '</li>';
        // onclick="toggleDisplayByType($(this),\'' + graphType + '\',\''+typeKey+'\')"
    }


    var categoryHtml =
        '<ul class="' + categoryClass + '">\n' + categoryLi + '</ul>';

    //放入节点类型容器中
    if (typeContainer == null) {
        console.warn("存放[graphType]类别的开关按钮的容器未定义, 将无法显示当前类别的显示开关按钮")
    }
    else {
        typeContainer.html(categoryHtml);
        $('.categoryLi_' + graphType).bind('click', this, function (e) {
            //Note: 2th: 向回调函数传入额外的参数
            e.data.toggleDisplayByType($(this), graphType);
        })
    }

}

//END: 方法区

