package com.geqian.dbsnap.common.properties;

import lombok.Data;

/**
 * @author geqian
 * @date 5:58 2026/8/11
 */
@Data
public abstract class EmbeddedDataSourceProperties extends AbstractDataSourceProperties {

    protected String database;

    @Override
    public String buildUrl(String url) {
        return url.replace("${database}", database);
    }
}
