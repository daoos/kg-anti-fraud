package com.jfpuhui.anti.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-13-17:49
 */
@Controller
@Slf4j
public class TestController {
    @RequestMapping(value = { "test_mysql" })
    @ResponseBody
    public String testMysql(){
        //调用服务
        log.debug("进入TestController.testMysql()");



        return "success";
    }

}
