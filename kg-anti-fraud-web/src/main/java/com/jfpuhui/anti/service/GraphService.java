package com.jfpuhui.anti.service;

import com.jfpuhui.anti.exception.CustomException;
import com.sun.istack.internal.NotNull;

import java.util.Map;

public interface GraphService {


    public Map<String, Map> select2DGraphByCertNo(String certNo);

    public Map<String, Object> selectByCertNo4Smart(String certNo) throws CustomException;


    public Map<String, Object> selectByName4Smart(@NotNull String name) throws CustomException;

    public Map<String, Object> selectByCustType4Smart(@NotNull Integer custType) throws CustomException;

    public Map<String, Object> selectByVid4Smart(@NotNull String vid) throws CustomException;
}
