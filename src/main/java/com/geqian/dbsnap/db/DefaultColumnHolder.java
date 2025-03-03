package com.geqian.dbsnap.db;

import com.geqian.dbsnap.bo.FieldDefinition;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author geqian
 * @date 22:02 2023/9/20
 */
public class DefaultColumnHolder {

    //保存不排除字段
    private static List<FieldDefinition> defaultColumns;

    public static List<FieldDefinition> getDefaultColumns() {
        return defaultColumns;
    }

    public static void setDefaultColumns(List<FieldDefinition> defaultColumns) {
        DefaultColumnHolder.defaultColumns = defaultColumns.stream()
                .sorted(Comparator.comparingInt(FieldDefinition::getOrder))
                .collect(Collectors.toList());
    }
}
