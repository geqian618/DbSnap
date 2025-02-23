package com.geqian.dbsnap.db;

import com.geqian.dbsnap.bo.LabelAndValue;

import java.util.List;

/**
 * @author geqian
 * @date 22:02 2023/9/20
 */
public class DefaultColumnHolder {

    //保存不排除字段
    private static List<LabelAndValue> defaultColumns;

    public static List<LabelAndValue> getDefaultColumns() {
        return defaultColumns;
    }

    public static void setDefaultColumns(List<LabelAndValue> defaultColumns) {
        DefaultColumnHolder.defaultColumns = defaultColumns;
    }
}
