package com.geqian.structure.service.impl;

import cn.hutool.core.io.IoUtil;
import com.geqian.structure.common.ResponseResult;
import com.geqian.structure.common.dto.ConnectionInfoDto;
import com.geqian.structure.jdbc.DruidConnectionManager;
import com.geqian.structure.service.ConnectionService;
import com.geqian.structure.utils.ThrowableUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.derby.iapi.error.StandardException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author geqian
 * @date 12:52 2023/7/9
 */
@Service
@Slf4j
public class ConnectionServiceImpl implements ConnectionService {


    @Override
    public ResponseResult<String> connection(ConnectionInfoDto connectionInfoDto) {
        try {
            if (!Objects.equals(connectionInfoDto, DruidConnectionManager.getConnectionInfo())) {
                DruidConnectionManager.setConnectionInfo(connectionInfoDto);
                DruidConnectionManager.clearDatasource();
            }
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
            ClassPathResource resource = new ClassPathResource("classpath:/database-config.json");
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
