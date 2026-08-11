package com.geqian.dbsnap.controller;

import com.geqian.dbsnap.common.ResponseResult;
import com.geqian.dbsnap.properties.AbstractDataSourceProperties;
import com.geqian.dbsnap.jdbc.DruidConnectionManager;
import com.geqian.dbsnap.service.ConnectionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author geqian
 * @date 21:50 2023/7/8
 */

@Controller
public class ConnectionController {

    @Resource
    private ConnectionService connectionService;

    @GetMapping("/")
    public String index() {
        DruidConnectionManager.clearDatasource();
        return "index";
    }


    @GetMapping("/document")
    public String generator() {
        return "document";
    }


    @ResponseBody
    @GetMapping(value = "/database/config")
    public ResponseResult<String> getDatabaseConfig() {
        return connectionService.getDatabaseConfig();
    }

    @ResponseBody
    @PostMapping(value = "/dataSource/connection")
    public ResponseResult<String> connection(@RequestBody AbstractDataSourceProperties properties) {
        return connectionService.connection(properties);
    }
}