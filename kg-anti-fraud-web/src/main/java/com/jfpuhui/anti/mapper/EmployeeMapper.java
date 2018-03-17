package com.jfpuhui.anti.mapper;

import com.jfpuhui.anti.pojo.Employee;

import java.util.List;

/**
 * 测试查询mysql
 */
public interface EmployeeMapper {

    public List<Employee> queryAll();

}
