package com.geqian.dbsnap.service;

import com.geqian.dbsnap.common.ResponseResult;
import com.geqian.dbsnap.properties.AbstractDataSourceProperties;

/**
 * @author geqian
 * @date 12:52 2023/7/9
 */
public interface ConnectionService {

    ResponseResult<String> connection(AbstractDataSourceProperties properties);

    ResponseResult<String> getDatabaseConfig();

}
