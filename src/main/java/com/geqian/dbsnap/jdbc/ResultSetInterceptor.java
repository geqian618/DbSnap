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
     * @param tag  结果特殊处理sql语句标识（例如：无法起别名进行属性映射）
     * @return
     */
    boolean support(DatabaseMetaData metaData, String tag);

    /**
     * @param resultSet sql执行结果集
     * @return 最终查询结果
     */
    List<T> intercept(ResultSet resultSet) throws Exception;

}
