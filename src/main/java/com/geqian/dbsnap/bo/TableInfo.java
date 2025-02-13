package com.geqian.dbsnap.bo;

import com.geqian.dbsnap.entity.TableStructure;
import lombok.Data;

import java.util.List;

/**
 * 数据库基本表信息及字段信息
 * @author geqian
 * @date 12:19 2023/7/14
 */
@Data
public class TableInfo {

    private TableDefinition tableDefinition;

    private List<? extends TableStructure> dataList;
}
