package com.geqian.dbsnap.interceptor;

import com.geqian.dbsnap.entity.TreeNode;
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
public class SQLite3DatabasesInterceptor implements ResultSetInterceptor<TreeNode> {


    @Override
    public boolean support(DatabaseMetaData metaData, String tag) {
        return "sqlite3-databases".equals(tag);
    }

    @Override
    public List<TreeNode> intercept(ResultSet resultSet) throws Exception {
        List<TreeNode> treeNodes = new ArrayList<>();
        while (resultSet.next()) {
            TreeNode treeNode = new TreeNode();
            String database = resultSet.getString("name");
            treeNode.setLabelName(database);
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }
}
