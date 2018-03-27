package com.jfpuhui.anti.mapper;

import com.jfpuhui.anti.dao.pojo.CustRelationship;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-16-17:51
 */
public interface CustRelationshipMapper {

//    List<CustRelationship> selectAll();

    /**根据身份证号查询所有2度边
     * @return
     */
    List<CustRelationship> selectAll2DRelByCertNo(String certNo);


    /**根据身份证查询所有1度边
     * @param certNo
     * @return
     */
    List<CustRelationship> selectAll1DRelByCertNo(String certNo);

    /**根据给定的身份证号set查询对应的边数据集
     * @param certNos
     * @return
     */
    List<CustRelationship> selectRelByGivenCertNos(@Param("set")Set<String> certNos);


    /**根据指定的身份号list查询边数据集
     * @param certNos
     * @return
     */
    List<CustRelationship> selectRelByGivenCertNos(@Param("set") List<String> certNos);


    /**根据给定的姓名模糊查询身份证号list
     * Note: 模糊查询, 若为了便于触发索引, 可以只用 name%
     * @param name
     * @return
     */
    List<String> selectCertNosLikeName(String name);

    /**根据给定客户类型查询身份证号list
     * @param custType
     * @return
     */
    List<String> selectCertNosByCustType(Integer custType);



    /**
     * 根据vid查核间的边, ie.0度边数据集
     */
    public List<CustRelationship> selectAll0DRelByVid(String vid);




}
