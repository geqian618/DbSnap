package com.geqian.dbsnap.common.properties;

import com.geqian.dbsnap.annotation.Marker;
import com.geqian.dbsnap.constants.DatabaseTypes;
import lombok.Data;


/**
 * @author geqian
 * @date 0:22 2026/8/11
 */
@Data
@Marker(DatabaseTypes.ORACLE)
public class OracleDataSourceProperties extends AbstractDataSourceProperties {

    private String host;

    private Integer port;

    private String sid;

    private String username;

    private String password;

    @Override
    public String buildUrl(String url) {
        return url.replace("${host}", this.host).replace("${port}", String.valueOf(this.port)).replace("${sid}", this.sid);
    }
}
