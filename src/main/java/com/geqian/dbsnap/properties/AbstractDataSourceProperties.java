package com.geqian.dbsnap.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author geqian
 * @date 0:05 2026/8/11
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractDataSourceProperties {

    protected String type;

    protected String username;

    protected String password;

    abstract public String buildUrl(String url);

}

