package com.jfpuhui.anti.service;

import com.jfpuhui.anti.dao.dto.Graph;
import com.jfpuhui.anti.exception.CustomException;
import com.sun.istack.internal.NotNull;

import java.util.Map;

public interface GraphService {


    public Map<String, Map> select2DGraphByCertNo(String certNo);

    public Graph selectByCertNo4Smart(String certNo) throws CustomException;


    public Graph selectByName4Smart(@NotNull String name) throws CustomException;

    public Graph selectByCustType4Smart(@NotNull Integer custType) throws CustomException;

    public Graph selectByVid4Smart(@NotNull String vid) throws CustomException;

    /**根据身份证号, 这里就是node ID, 查取一度范围内的点,边数据集
     * @param certNo
     * @return
     */
    Graph expandGraphByCertNoBy1D(String certNo, Integer nodeDepth);
}
