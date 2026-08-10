package com.geqian.dbsnap.common.vo;

import com.geqian.dbsnap.common.bo.FieldDefinition;
import lombok.Data;

import java.util.List;

/**
 * @author geqian
 * @date 21:53 2023/9/20
 */
@Data
public class ColumnsVo {

    private List<FieldDefinition> allColumns;

    private List<String> defaultColumns;
}
