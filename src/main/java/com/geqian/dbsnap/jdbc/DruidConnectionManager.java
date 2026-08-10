package com.geqian.dbsnap.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.geqian.dbsnap.common.properties.AbstractDataSourceProperties;
import com.geqian.dbsnap.db.CurrentDatabaseMetadataHolder;
import com.geqian.dbsnap.db.DatabaseMetaData;
import com.geqian.dbsnap.db.DatabaseMetadataFactory;

import java.sql.Connection;
import java.util.Properties;

/**
 * @author geqian
 * @date 20:16 2023/1/4
 */
public class DruidConnectionManager {

    private static volatile DruidDataSource dataSource;

    private static AbstractDataSourceProperties dataSourceProperties;


    public static void setConnectionInfo(AbstractDataSourceProperties dataSourceProperties) {
        DruidConnectionManager.dataSourceProperties = dataSourceProperties;
    }

    public static void initDataSource() {
        try {
            DatabaseMetaData metadata = DatabaseMetadataFactory.getMetaData(dataSourceProperties.getType());
            Class.forName(metadata.getDriverClass());
            String url = dataSourceProperties.buildUrl(metadata.getUrl());
            Properties properties = new Properties();
            String threadSize = String.valueOf(Runtime.getRuntime().availableProcessors() + 1);
            //初始化连接数量
            properties.setProperty("initialSize", threadSize);
            //最大连接数
            properties.setProperty("maxActive", threadSize);
            //最大等待时间
            properties.setProperty("maxWait", "30000");
            properties.setProperty("driverClassName", metadata.getDriverClass());
            properties.setProperty("url", url);
            properties.setProperty("username", dataSourceProperties.getUsername());
            properties.setProperty("password", dataSourceProperties.getPassword());
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
            // 失败后重连的次数
            dataSource.setConnectionErrorRetryAttempts(0);
            //请求失败之后中断
            dataSource.setBreakAfterAcquireFailure(true);
            CurrentDatabaseMetadataHolder.setMetaData(metadata);
        } catch (Exception e) {
            throw new RuntimeException("初始化数据源失败", e);
        }
    }

    public static Connection getConnection() {
        if (dataSource == null) {
            synchronized (DruidConnectionManager.class) {
                if (dataSource == null) {
                    initDataSource();
                }
            }
        }
        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("获取连接失败，" + e.getMessage(), e);
        }
    }


    public static AbstractDataSourceProperties getDataSourceProperties() {
        return dataSourceProperties;
    }

    public static void clearDatasource() {
        DruidConnectionManager.dataSource = null;
    }
}
