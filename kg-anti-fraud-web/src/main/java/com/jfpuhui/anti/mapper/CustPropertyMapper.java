package com.jfpuhui.anti.mapper;

import com.jfpuhui.anti.pojo.CustProperty;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-19-17:41
 */
public interface CustPropertyMapper {

    /**根据点id批量查询出节点属性数据集
     * @param certiIds
     * @return
     */
    List<CustProperty> selectByCertIds(@Param("set") Set<String> certiIds);

    /**根据身份证查询客户属性信息
     * @param certNos
     * @return
     */
    List<CustProperty> selectByCertNos(@Param("set") Set<String> certNos);
}
