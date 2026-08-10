package com.geqian.dbsnap.entity;

import com.geqian.dbsnap.annotation.Marker;
import com.geqian.dbsnap.constants.DatabaseTypes;

/**
 * @author geqian
 * @date 17:16 2024/3/1
 */
@Marker(DatabaseTypes.CLICKHOUSE)
public class ClickhouseTableStructure extends TableStructure {

    //@TableField(value = "长度", order = 7, exclude = true, enums = "null-> ")
    //private Long length;
    //
    //public Long getLength() {
    //    return length;
    //}
    //
    //public void setLength(Long length) {
    //    this.length = length;
    //}
}
