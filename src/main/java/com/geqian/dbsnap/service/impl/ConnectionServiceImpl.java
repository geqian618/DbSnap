package com.geqian.dbsnap.service.impl;

import cn.hutool.core.io.IoUtil;
import com.geqian.dbsnap.common.ResponseResult;
import com.geqian.dbsnap.common.properties.AbstractDataSourceProperties;
import com.geqian.dbsnap.jdbc.DruidConnectionManager;
import com.geqian.dbsnap.service.ConnectionService;
import com.geqian.dbsnap.utils.ThrowableUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.derby.iapi.error.StandardException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author geqian
 * @date 12:52 2023/7/9
 */
@Service
@Slf4j
public class ConnectionServiceImpl implements ConnectionService {


    @Override
    public ResponseResult<String> connection(AbstractDataSourceProperties properties) {
        try {
            DruidConnectionManager.clearDatasource();
            DruidConnectionManager.setConnectionInfo(properties);
            DruidConnectionManager.getConnection();
        } catch (Exception e) {
            DruidConnectionManager.clearDatasource();
            if (ThrowableUtils.containsAnyOne(e, StandardException.class)) {
                return ResponseResult.fail("数据库已被另一个实例占用");
            }
            e.printStackTrace();
            return ResponseResult.fail(e.getMessage());
        }
        return ResponseResult.success("连接成功");
    }


    @Override
    public ResponseResult<String> getDatabaseConfig() {
        try {
            ClassPathResource resource = new ClassPathResource("database-config.json");
            InputStream inputStream = resource.getInputStream();
            byte[] bytes = IoUtil.readBytes(inputStream);
            String content = new String(bytes, StandardCharsets.UTF_8);
            return ResponseResult.success("success", content);
        } catch (IOException e) {
            log.error("读取database-config.json出错：", e);
            return ResponseResult.success("success", "[]");
        }
    }
}
