package com.geqian.dbsnap.service;

import com.geqian.dbsnap.common.dto.ConnectionInfoDto;
import com.geqian.dbsnap.common.ResponseResult;

/**
 * @author geqian
 * @date 12:52 2023/7/9
 */
public interface ConnectionService {

    ResponseResult<String> connection(ConnectionInfoDto dataSourceDto);

    ResponseResult<String> getDatabaseConfig();

}
