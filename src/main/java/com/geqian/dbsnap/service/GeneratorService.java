package com.geqian.dbsnap.service;

import com.geqian.dbsnap.common.ResponseResult;
import com.geqian.dbsnap.common.dto.TableSelectDto;
import com.geqian.dbsnap.common.dto.TargetTableDto;
import com.geqian.dbsnap.common.vo.ColumnsVo;
import com.geqian.dbsnap.entity.TreeNode;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author geqian
 * @date 12:03 2023/7/12
 */
public interface GeneratorService {

    void preview(TargetTableDto targetTableDto, HttpServletResponse response);

    void downloadPdf(TargetTableDto targetTableDto, HttpServletResponse response);

    void downloadWord(TargetTableDto targetTableDto, HttpServletResponse response);

    void downloadHtml(TargetTableDto targetTableDto, HttpServletResponse response);

    ResponseResult<ColumnsVo> getTableColumnInfo();

    void downloadMarkdown(TargetTableDto targetTableDto, HttpServletResponse response);

    ResponseResult<List<TreeNode>> getDatabases();

    ResponseResult<List<TreeNode>> getTables(TableSelectDto dto);
}
