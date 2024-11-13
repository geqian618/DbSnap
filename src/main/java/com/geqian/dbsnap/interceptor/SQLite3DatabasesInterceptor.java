package com.geqian.dbsnap.interceptor;

import com.geqian.dbsnap.entity.TreeNode;
import com.geqian.dbsnap.jdbc.ResultSetInterceptor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author geqian
 * @date 17:57 2024/4/30
 */
@Component
public class SQLite3DatabasesInterceptor implements ResultSetInterceptor<TreeNode> {

    private final String databases = "PRAGMA database_list";


    @Override
    public boolean support(String productName, String sql) {
        return "sqlite".equalsIgnoreCase(productName) && sql.equalsIgnoreCase(databases);
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
