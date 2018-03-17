package com.jfpuhui.anti.web.controller;

import com.jfpuhui.anti.mapper.EmployeeMapper;
import com.jfpuhui.anti.pojo.Employee;
import com.jfpuhui.anti.service.CustRelationshipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-13-17:46
 */
@Controller
@Slf4j
public class HelloController {

    @Autowired
    private CustRelationshipService custRelationshipService;
    @RequestMapping(value = { "hello" })
    @ResponseBody
    public String hello(){
        //调用服务
        log.debug("进入hello...");

        custRelationshipService.selectAll();

        return "success";
    }


    @Autowired
    private EmployeeMapper employeeMapper;

    @RequestMapping(value = {"test_mysql"})
    public String testMysql(){
        log.debug("测试mysql");
        List<Employee> employees = employeeMapper.queryAll();
        System.out.println(employees);
        return "ok";
    }

}
