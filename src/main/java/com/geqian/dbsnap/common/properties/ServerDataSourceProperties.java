package com.geqian.dbsnap.common.properties;

import lombok.Data;

/**
 * @author geqian
 * @date 0:09 2026/8/11
 */
@Data
public abstract class ServerDataSourceProperties extends AbstractDataSourceProperties {

    protected String host;

    protected Integer port;

    protected String database;

    @Override
    public String buildUrl(String url) {
        return url.replace("${host}", this.host).replace("${port}", String.valueOf(this.port)).replace("${database}", this.database);
    }

}
