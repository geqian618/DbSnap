package com.geqian.dbsnap.common.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author geqian
 * @date 21:51 2023/9/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldDefinition {

    private String label;

    private String value;

    private int order;

}
