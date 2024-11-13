package com.geqian.dbsnap.controller;

import com.geqian.dbsnap.common.ResponseResult;
import com.geqian.dbsnap.common.dto.TableSelectDto;
import com.geqian.dbsnap.common.dto.TargetTableDto;
import com.geqian.dbsnap.common.vo.ColumnsVo;
import com.geqian.dbsnap.entity.TreeNode;
import com.geqian.dbsnap.service.GeneratorService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author geqian
 * @date 12:01 2023/7/12
 */
@RestController
@RequestMapping("/document")
public class GeneratorController {

    @Resource
    private GeneratorService generatorService;

    @GetMapping("/getTableColumnInfo")
    public ResponseResult<ColumnsVo> getTableColumnInfo() {
        return generatorService.getTableColumnInfo();
    }


    @PostMapping("/pdf/preview")
    public void preview(@RequestBody TargetTableDto targetTableDto, HttpServletResponse response) throws Exception {
        generatorService.preview(targetTableDto, response);
    }


    @PostMapping("/pdf/download")
    public void downloadPdf(@RequestBody TargetTableDto targetTableDto, HttpServletResponse response) throws Exception {
        generatorService.downloadPdf(targetTableDto, response);
    }


    @PostMapping("/word/download")
    public void downloadWord(@RequestBody TargetTableDto targetTableDto, HttpServletResponse response) throws Exception {
        generatorService.downloadWord(targetTableDto, response);
    }

    @PostMapping("/html/download")
    public void downloadHtml(@RequestBody TargetTableDto targetTableDto, HttpServletResponse response) throws Exception {
        generatorService.downloadHtml(targetTableDto, response);
    }

    @PostMapping("/markdown/download")
    public void downloadMd(@RequestBody TargetTableDto targetTableDto, HttpServletResponse response) throws Exception {
        generatorService.downloadMarkdown(targetTableDto, response);
    }

    @GetMapping("/getDatabases")
    public ResponseResult<List<TreeNode>> getDatabases() throws Exception {
        return generatorService.getDatabases();
    }


    @GetMapping("/getTables")
    public ResponseResult<List<TreeNode>> getTables(TableSelectDto dto) throws Exception {
        return generatorService.getTables(dto);
    }

}
