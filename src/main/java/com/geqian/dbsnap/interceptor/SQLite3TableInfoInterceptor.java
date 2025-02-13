package com.geqian.dbsnap.interceptor;

import com.geqian.dbsnap.entity.SQLite3TableStructure;
import com.geqian.dbsnap.jdbc.ResultSetInterceptor;
import org.springframework.stereotype.Component;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author geqian
 * @date 17:57 2024/4/30
 */
@Component
public class SQLite3TableInfoInterceptor implements ResultSetInterceptor<SQLite3TableStructure> {


    @Override
    public boolean support(DatabaseMetaData metaData, String tag) {
        return "sqlite3_tableStructure".equals(tag);
    }

    @Override
    public List<SQLite3TableStructure> intercept(ResultSet resultSet) throws Exception {
        List<SQLite3TableStructure> structures = new ArrayList<>();
        while (resultSet.next()) {
            SQLite3TableStructure structure = new SQLite3TableStructure();
            String columnName = resultSet.getString("name");
            String columnType = resultSet.getString("type");
            int notnull = resultSet.getInt("notnull");
            String columnDefault = resultSet.getString("dflt_value");
            int pk = resultSet.getInt("pk");
            structure.setIsNullable(notnull == 0 ? "NO" : "YES");
            structure.setColumnType(columnType);
            structure.setColumnName(columnName);
            structure.setColumnDefault(columnDefault);
            structure.setColumnKey(pk == 1 ? "主键" : "");
            structures.add(structure);
        }
        return structures;
    }
}
