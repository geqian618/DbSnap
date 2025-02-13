package com.geqian.dbsnap.interceptor;

import com.geqian.dbsnap.entity.DerbyTableStructure;
import com.geqian.dbsnap.entity.TableStructure;
import com.geqian.dbsnap.jdbc.ResultSetInterceptor;
import org.apache.derby.catalog.TypeDescriptor;
import org.springframework.stereotype.Component;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author geqian
 * @date 18:02 2024/4/29
 */
@Component
public class DerbyInterceptor implements ResultSetInterceptor<TableStructure> {


    @Override
    public boolean support(DatabaseMetaData metaData, String tag) {
        return "derby_tableStructure".equals(tag);
    }

    @Override
    public List<TableStructure> intercept(ResultSet resultSet) throws Exception {
        List<TableStructure> structures = new ArrayList<>();
        while (resultSet.next()) {
            String columnName = resultSet.getString("columNname");
            String columnDefault = resultSet.getString("columnDefault");
            if (columnDefault != null && columnDefault.matches("^'\\S+'$")) {
                columnDefault = columnDefault.substring(1, columnDefault.length() - 1);
            }
            TypeDescriptor typeDescriptor = (TypeDescriptor) resultSet.getObject("columnType");
            DerbyTableStructure tableStructure = new DerbyTableStructure();
            tableStructure.setColumnName(columnName);
            tableStructure.setColumnType(typeDescriptor.getTypeName());
            tableStructure.setIsNullable(typeDescriptor.isNullable() ? "YES" : "No");
            tableStructure.setLength((long) typeDescriptor.getMaximumWidth());
            tableStructure.setColumnDefault(columnDefault);
            structures.add(tableStructure);
        }
        return structures;
    }
}
