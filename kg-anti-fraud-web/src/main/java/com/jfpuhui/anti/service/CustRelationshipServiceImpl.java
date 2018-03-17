package com.jfpuhui.anti.service;

import com.jfpuhui.anti.mapper.CustRelationshipMapper;
import com.jfpuhui.anti.pojo.CustRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-16-17:56
 */
@Service("custRelationshipService")
public class CustRelationshipServiceImpl implements CustRelationshipService {

    @Autowired
    private CustRelationshipMapper custRelationshipMapper;

    public List<CustRelationship> selectAll() {
        List<CustRelationship> custRelationships = custRelationshipMapper.selectAll();
        for (CustRelationship relationship : custRelationships) {



            System.out.println(relationship);
        }

        return custRelationships;
    }


}
