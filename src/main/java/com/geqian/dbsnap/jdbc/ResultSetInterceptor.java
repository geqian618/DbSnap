package com.geqian.dbsnap.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author geqian
 * @date 22:38 2024/4/19
 */
public interface ResultSetInterceptor<T> extends JDBCInterceptor {



    /**
     * @param metaData jdbc原生 DatabaseMetaData 对象
     * @param tag         特殊结果处理sql语句标识
     * @return
     */
    boolean support(DatabaseMetaData metaData, String tag);

    /**
     * @param resultSet sql执行结果集
     * @return 最终查询结果
     */
    List<T> intercept(ResultSet resultSet) throws Exception;

}
