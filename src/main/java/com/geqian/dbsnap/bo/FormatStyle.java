package com.geqian.dbsnap.bo;

import lombok.Data;

/**
 * @author geqian
 * @date 23:44 2024/11/7
 */
@Data
public class FormatStyle {

    private DatabaseDescribeStyle database;

    private TableDescribeStyle table;

    private String fileName;
}
