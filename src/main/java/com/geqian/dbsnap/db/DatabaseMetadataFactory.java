package com.geqian.dbsnap.db;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author geqian
 * @date 16:12 2023/9/11
 */
@Component
@ConfigurationProperties(prefix = "database")
public class DatabaseMetadataFactory {

    private static Map<String, DatabaseMetaData> definitions;


    /**
     * 注册 DatabaseMetaData
     *
     * @param key
     * @param metadata
     */
    private static void register(String key, DatabaseMetaData metadata) {
        DatabaseMetadataFactory.definitions.put(key, metadata);
    }

    /**
     * 根据数据库，获取对象的数据库信息
     *
     * @param databaseType
     * @return
     */
    public static DatabaseMetaData getMetaData(String databaseType) {
        return DatabaseMetadataFactory.definitions.get(databaseType);
    }

    public void setDefinitions(Map<String, DatabaseMetaData> definitions) {
        DatabaseMetadataFactory.definitions = definitions;
    }
}
