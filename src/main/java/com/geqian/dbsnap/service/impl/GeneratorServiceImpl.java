package com.geqian.dbsnap.service.impl;

import com.geqian.dbsnap.bo.FieldDefinition;
import com.geqian.dbsnap.bo.FormatStyle;
import com.geqian.dbsnap.bo.TableDefinition;
import com.geqian.dbsnap.bo.TableInfo;
import com.geqian.dbsnap.common.ResponseResult;
import com.geqian.dbsnap.common.dto.TableSelectDto;
import com.geqian.dbsnap.common.dto.TargetTableDto;
import com.geqian.dbsnap.common.vo.ColumnsVo;
import com.geqian.dbsnap.context.TableStructureFactory;
import com.geqian.dbsnap.db.DefaultColumnHolder;
import com.geqian.dbsnap.entity.TableStructure;
import com.geqian.dbsnap.entity.TreeNode;
import com.geqian.dbsnap.jdbc.DruidConnectionManager;
import com.geqian.dbsnap.mapper.DatabaseMapper;
import com.geqian.dbsnap.service.GeneratorService;
import com.geqian.dbsnap.utils.ReflectionUtils;
import com.geqian.document4j.common.annotation.TableField;
import com.geqian.document4j.html.HTMLBuilder;
import com.geqian.document4j.html.HTMLStyle;
import com.geqian.document4j.markdown.MarkDownBuilder;
import com.geqian.document4j.markdown.MarkDownStyle;
import com.geqian.document4j.pdf.PDFBuilder;
import com.geqian.document4j.pdf.PDFStyle;
import com.geqian.document4j.word.WordBuilder;
import com.geqian.document4j.word.WordStyle;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author geqian
 * @date 12:03 2023/7/12
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {

    private final Logger log = LoggerFactory.getLogger(GeneratorServiceImpl.class);

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Resource
    private DatabaseMapper databaseMapper;


    @SneakyThrows(Exception.class)
    @Override
    public void downloadPdf(TargetTableDto targetTableDto, HttpServletResponse response) {
        preview(targetTableDto, response);
    }


    @SneakyThrows(Exception.class)
    @Override
    public void preview(TargetTableDto targetTableDto, HttpServletResponse response) {
        try (OutputStream out = response.getOutputStream()) {
            String filename = targetTableDto.getFormatStyle().getFileName();
            filename = new SimpleDateFormat(filename).format(new Date()) + ".pdf";
            //byte[] pdfBytes = WordToPdfUtils.word2007ToPdf(wordBytes);
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("filename", URLEncoder.encode(filename, "UTF-8"));
            //文件设置为附件
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            buildPdfDocument(targetTableDto, out);
        }
    }

    @SneakyThrows(Exception.class)
    @Override
    public void downloadWord(TargetTableDto targetTableDto, HttpServletResponse response) {
        try (OutputStream out = response.getOutputStream()) {
            String filename = targetTableDto.getFormatStyle().getFileName();
            filename = new SimpleDateFormat(filename).format(new Date()) + ".docx";
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("filename", URLEncoder.encode(filename, "UTF-8"));
            //文件设置为附件
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            buildWordDocument(targetTableDto, out);
        }
    }

    @SneakyThrows(Exception.class)
    @Override
    public void downloadHtml(TargetTableDto targetTableDto, HttpServletResponse response) {
        try (OutputStream out = response.getOutputStream()) {
            String filename = targetTableDto.getFormatStyle().getFileName();
            filename = new SimpleDateFormat(filename).format(new Date()) + ".html";
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("filename", URLEncoder.encode(filename, "UTF-8"));
            //文件设置为附件
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            buildHtmlDocument(targetTableDto, out);
        }
    }


    @SneakyThrows(Exception.class)
    @Override
    public void downloadMarkdown(TargetTableDto targetTableDto, HttpServletResponse response) {
        try (OutputStream out = response.getOutputStream()) {
            String filename = targetTableDto.getFormatStyle().getFileName();
            filename = new SimpleDateFormat(filename).format(new Date()) + ".md";
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("filename", URLEncoder.encode(filename, "UTF-8"));
            //文件设置为附件
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            buildMdDocument(targetTableDto, out);
        }
    }

    @Override
    public ResponseResult<List<TreeNode>> getDatabases() {
        List<TreeNode> databases = databaseMapper.getDatabases();
        return ResponseResult.success(databases);
    }

    @Override
    public ResponseResult<List<TreeNode>> getTables(TableSelectDto dto) {
        List<TreeNode> tables = databaseMapper.getTables(dto.getSchemaName(), dto.getParentNodeId());
        return ResponseResult.success(tables);
    }


    @Override
    public ResponseResult<ColumnsVo> getTableColumnInfo() {

        ColumnsVo columnsVo = new ColumnsVo();

        String dbType = DruidConnectionManager.getConnectionInfo().getDatabaseType();

        Class<? extends TableStructure> classType = TableStructureFactory.getTableStructureType(dbType);

        List<Field> fields = ReflectionUtils.getFieldAllContainSuperclass(classType, field -> true);

        List<FieldDefinition> fieldDefinitions = fields.stream()
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(TableField.class).order()))
                .map(field -> new FieldDefinition(field.getAnnotation(TableField.class).value(), field.getName(), field.getAnnotation(TableField.class).order()))
                .collect(Collectors.toList());

        List<String> defaultColumns = fields.stream()
                .filter(field -> !field.getAnnotation(TableField.class).exclude())
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(TableField.class).order()))
                .map(Field::getName)
                .collect(Collectors.toList());

        columnsVo.setAllColumns(fieldDefinitions);

        columnsVo.setDefaultColumns(defaultColumns);

        return ResponseResult.success(columnsVo);
    }


    @SneakyThrows(Exception.class)
    public void buildWordDocument(TargetTableDto targetTableDto, OutputStream outputStream) {

        DefaultColumnHolder.setDefaultColumns(targetTableDto.getDefaultColumns());

        WordBuilder wordBuilder = WordBuilder.create(outputStream);

        List<TreeNode> treeNodeList = targetTableDto.getDataList();

        if (!CollectionUtils.isEmpty(treeNodeList)) {

            //过滤出全部 Schema节点
            List<TreeNode> schemaNodes = targetTableDto.getDataList().stream().filter(TreeNode::isSchemaNode).collect(Collectors.toList());

            FormatStyle formatStyle = targetTableDto.getFormatStyle();
            String databaseFormat = formatStyle.getDatabase().getFormat();
            int databaseFontSize = formatStyle.getDatabase().getFontSize();
            Boolean databaseBold = formatStyle.getDatabase().getBold();
            String tableFormat = formatStyle.getTable().getFormat();
            int tableFontSize = formatStyle.getTable().getFontSize();
            Boolean tableBold = formatStyle.getTable().getBold();
            WordStyle.Font calibri = WordStyle.Font.fontFamily("Calibri");
            WordStyle.Font titleFontSize = WordStyle.Font.fontSize(databaseFontSize);
            WordStyle.Font tableNameFontSize = WordStyle.Font.fontSize(tableFontSize);
            WordStyle.Font cellFontSize = WordStyle.Font.fontSize(10);

            for (TreeNode schemaNode : schemaNodes) {
                String schemaName = schemaNode.getLabelName();
                if (StringUtils.hasText(databaseFormat)) {
                    String databaseName = databaseFormat.replace("${databaseName}", schemaName);
                    wordBuilder.paragraph(databaseName, calibri, databaseBold ? WordStyle.Font.BOLD : null, titleFontSize);
                }
                //过滤出指定 Schema节点下的全部 table节点
                List<TreeNode> tableNodes = treeNodeList.stream()
                        .filter(data -> !data.isSchemaNode() && Objects.equals(data.getParentNodeId(), schemaNode.getNodeId()))
                        .collect(Collectors.toList());

                List<Future<TableInfo>> futureList = new ArrayList<>();

                for (TreeNode tableNode : tableNodes) {
                    Future<TableInfo> future = threadPoolExecutor.submit(() -> {
                                String tableName = tableNode.getLabelName();
                                String tableComment = tableNode.getDescription();
                                TableInfo tableInfo = new TableInfo();
                                TableDefinition tableDefinition = new TableDefinition(tableName, tableComment);
                                tableDefinition.setTableName(tableName);
                                tableDefinition.setTableComment(tableComment);
                                List<? extends TableStructure> tableStructures = databaseMapper.getTableStructureList(schemaName, tableName);
                                tableInfo.setTableDefinition(tableDefinition);
                                tableInfo.setDataList(tableStructures);
                                return tableInfo;
                            }
                    );
                    futureList.add(future);
                }

                for (Future<TableInfo> future : futureList) {
                    TableInfo tableInfo = future.get();
                    TableDefinition tableDefinition = tableInfo.getTableDefinition();
                    if (StringUtils.hasText(tableFormat)) {
                        String tableName = tableFormat.replace("${tableName}", tableDefinition.getTableName()).replace("${description}", tableDefinition.getTableComment());
                        wordBuilder.paragraph(tableName, calibri, tableBold ? WordStyle.Font.BOLD : null, tableNameFontSize);
                    }
                    wordBuilder.table(tableInfo.getDataList(), calibri, cellFontSize);
                    wordBuilder.blankRow().blankRow();
                }
            }
        }
        wordBuilder.build();
    }


    @SneakyThrows(Exception.class)
    public void buildPdfDocument(TargetTableDto targetTableDto, OutputStream outputStream) {

        DefaultColumnHolder.setDefaultColumns(targetTableDto.getDefaultColumns());

        PDFBuilder pdfBuilder = PDFBuilder.create(outputStream);

        List<TreeNode> treeNodeList = targetTableDto.getDataList();

        if (!CollectionUtils.isEmpty(treeNodeList)) {

            //过滤出全部 Schema节点
            List<TreeNode> schemaNodes = targetTableDto.getDataList().stream().filter(TreeNode::isSchemaNode).collect(Collectors.toList());

            FormatStyle formatStyle = targetTableDto.getFormatStyle();
            String databaseFormat = formatStyle.getDatabase().getFormat();
            int databaseFontSize = formatStyle.getDatabase().getFontSize();
            Boolean databaseBold = formatStyle.getDatabase().getBold();
            String tableFormat = formatStyle.getTable().getFormat();
            int tableFontSize = formatStyle.getTable().getFontSize();
            Boolean tableBold = formatStyle.getTable().getBold();

            PDFStyle.Font titleFontSize = PDFStyle.Font.fontSize(databaseFontSize);

            PDFStyle.Font tableNameFontSize = PDFStyle.Font.fontSize(tableFontSize);

            PDFStyle.Font cellFontSize = PDFStyle.Font.fontSize(10);

            PDFStyle.Paragraph paragraphSpacing = PDFStyle.Paragraph.paragraphSpacing(1.3f);


            for (TreeNode schemaNode : schemaNodes) {

                String schemaName = schemaNode.getLabelName();

                if (StringUtils.hasText(databaseFormat)) {
                    String databaseName = databaseFormat.replace("${databaseName}", schemaName);
                    pdfBuilder.paragraph(databaseName, databaseBold ? PDFStyle.Font.BOLD : null, titleFontSize);
                }

                //过滤出指定 Schema节点下的全部 table节点
                List<TreeNode> tableNodes = treeNodeList.stream()
                        .filter(data -> !data.isSchemaNode() && Objects.equals(data.getParentNodeId(), schemaNode.getNodeId()))
                        .collect(Collectors.toList());

                List<Future<TableInfo>> futureList = new ArrayList<>();

                for (TreeNode tableNode : tableNodes) {
                    Future<TableInfo> future = threadPoolExecutor.submit(() -> {
                                String tableName = tableNode.getLabelName();
                                String tableComment = tableNode.getDescription();
                                TableInfo tableInfo = new TableInfo();
                                TableDefinition tableDefinition = new TableDefinition(tableName, tableComment);
                                List<? extends TableStructure> tableStructures = databaseMapper.getTableStructureList(schemaName, tableName);
                                tableInfo.setTableDefinition(tableDefinition);
                                tableInfo.setDataList(tableStructures);
                                return tableInfo;
                            }
                    );
                    futureList.add(future);
                }

                for (Future<TableInfo> future : futureList) {
                    TableInfo tableInfo = future.get();
                    TableDefinition tableDefinition = tableInfo.getTableDefinition();
                    if (StringUtils.hasText(tableFormat)) {
                        String tableName = tableFormat.replace("${tableName}", tableDefinition.getTableName()).replace("${description}", tableDefinition.getTableComment());
                        pdfBuilder.paragraph(tableName, tableBold ? PDFStyle.Font.BOLD : null, tableNameFontSize, paragraphSpacing);
                    }
                    pdfBuilder.table(tableInfo.getDataList(), cellFontSize);
                    pdfBuilder.blankRow().blankRow();
                }
            }
        }
        pdfBuilder.build();
    }


    @SneakyThrows(Exception.class)
    public void buildMdDocument(TargetTableDto targetTableDto, OutputStream outputStream) {

        FormatStyle formatStyle = targetTableDto.getFormatStyle();
        String databaseFormat = formatStyle.getDatabase().getFormat();
        int databaseFontSize = formatStyle.getDatabase().getFontSize();
        Boolean databaseBold = formatStyle.getDatabase().getBold();
        String tableFormat = formatStyle.getTable().getFormat();
        int tableFontSize = formatStyle.getTable().getFontSize();
        Boolean tableBold = formatStyle.getTable().getBold();

        DefaultColumnHolder.setDefaultColumns(targetTableDto.getDefaultColumns());

        MarkDownBuilder markDownBuilder = MarkDownBuilder.create(outputStream);

        MarkDownStyle.Font databaseNameFontSize = MarkDownStyle.Font.fontSize(databaseFontSize);
        MarkDownStyle.Font tableNameFontSize = MarkDownStyle.Font.fontSize(tableFontSize);

        List<TreeNode> treeNodeList = targetTableDto.getDataList();

        if (!CollectionUtils.isEmpty(treeNodeList)) {

            //过滤出全部 Schema节点
            List<TreeNode> schemaNodes = targetTableDto.getDataList().stream().filter(TreeNode::isSchemaNode).collect(Collectors.toList());

            for (TreeNode schemaNode : schemaNodes) {

                String schemaName = schemaNode.getLabelName();

                if (StringUtils.hasText(databaseFormat)) {
                    String databaseName = databaseFormat.replace("${databaseName}", schemaName);
                    markDownBuilder.title(databaseName, MarkDownStyle.Title.LEVEL_3, databaseBold ? MarkDownStyle.Font.BOLD : null, databaseNameFontSize);
                }

                //过滤出指定 Schema节点下的全部 table节点
                List<TreeNode> tableNodes = treeNodeList.stream()
                        .filter(data -> !data.isSchemaNode() && Objects.equals(data.getParentNodeId(), schemaNode.getNodeId()))
                        .collect(Collectors.toList());

                List<Future<TableInfo>> futureList = new ArrayList<>();

                for (TreeNode tableNode : tableNodes) {
                    Future<TableInfo> future = threadPoolExecutor.submit(() -> {
                                String tableName = tableNode.getLabelName();
                                String tableComment = tableNode.getDescription();
                                TableInfo tableInfo = new TableInfo();
                                TableDefinition tableDefinition = new TableDefinition(tableName, tableComment);
                                List<? extends TableStructure> tableStructures = databaseMapper.getTableStructureList(schemaName, tableName);
                                tableInfo.setTableDefinition(tableDefinition);
                                tableInfo.setDataList(tableStructures);
                                return tableInfo;
                            }
                    );
                    futureList.add(future);
                }

                for (Future<TableInfo> future : futureList) {

                    TableInfo tableInfo = future.get();
                    TableDefinition tableDefinition = tableInfo.getTableDefinition();
                    if (StringUtils.hasText(tableFormat)) {
                        String tableName = tableFormat.replace("${tableName}", tableDefinition.getTableName()).replace("${description}", tableDefinition.getTableComment());
                        markDownBuilder.paragraph(tableName, tableBold ? MarkDownStyle.Font.BOLD : null, tableNameFontSize);
                    }
                    markDownBuilder.table(tableInfo.getDataList(), MarkDownStyle.CellAlignment.LEFT);
                    markDownBuilder.blankRow();
                }
                markDownBuilder.blankRow();
                markDownBuilder.blankRow();
            }
        }
        markDownBuilder.build();
    }


    @SneakyThrows(Exception.class)
    public void buildHtmlDocument(TargetTableDto targetTableDto, OutputStream outputStream) {

        DefaultColumnHolder.setDefaultColumns(targetTableDto.getDefaultColumns());

        HTMLBuilder htmlBuilder = HTMLBuilder.create(outputStream).setCatalogue(true);

        htmlBuilder.setStyleSelector("table{font-size:12px}");
        FormatStyle formatStyle = targetTableDto.getFormatStyle();
        String databaseFormat = formatStyle.getDatabase().getFormat();
        int databaseFontSize = formatStyle.getDatabase().getFontSize();
        Boolean databaseBold = formatStyle.getDatabase().getBold();
        String tableFormat = formatStyle.getTable().getFormat();
        int tableFontSize = formatStyle.getTable().getFontSize();
        Boolean tableBold = formatStyle.getTable().getBold();

        HTMLStyle.Font schemaNameFontSize = HTMLStyle.Font.fontsize(databaseFontSize + "px");
        HTMLStyle.Font tableNameFontSize = HTMLStyle.Font.fontsize(tableFontSize + "px");
        HTMLStyle.Padding paddingBottom = HTMLStyle.Padding.bottom("15px");
        List<TreeNode> treeNodeList = targetTableDto.getDataList();

        if (!CollectionUtils.isEmpty(treeNodeList)) {

            //过滤出全部 Schema节点
            List<TreeNode> schemaNodes = targetTableDto.getDataList().stream().filter(TreeNode::isSchemaNode).collect(Collectors.toList());

            for (TreeNode schemaNode : schemaNodes) {

                String schemaName = schemaNode.getLabelName();

                if (StringUtils.hasText(databaseFormat)) {
                    String databaseName = databaseFormat.replace("${databaseName}", schemaName);
                    htmlBuilder.title(databaseName, HTMLStyle.Title.LEVEL_1, databaseBold ? HTMLStyle.Font.BOLD : null, schemaNameFontSize, paddingBottom);
                }

                //过滤出指定 Schema节点下的全部 table节点
                List<TreeNode> tableNodes = treeNodeList.stream()
                        .filter(data -> !data.isSchemaNode() && Objects.equals(data.getParentNodeId(), schemaNode.getNodeId()))
                        .collect(Collectors.toList());

                List<Future<TableInfo>> futureList = new ArrayList<>();

                for (TreeNode tableNode : tableNodes) {
                    Future<TableInfo> future = threadPoolExecutor.submit(() -> {
                                String tableName = tableNode.getLabelName();
                                String tableComment = tableNode.getDescription();
                                TableInfo tableInfo = new TableInfo();
                                TableDefinition tableDefinition = new TableDefinition(tableName, tableComment);
                                List<? extends TableStructure> tableStructures = databaseMapper.getTableStructureList(schemaName, tableName);
                                tableInfo.setTableDefinition(tableDefinition);
                                tableInfo.setDataList(tableStructures);
                                return tableInfo;
                            }
                    );
                    futureList.add(future);
                }

                for (Future<TableInfo> future : futureList) {
                    TableInfo tableInfo = future.get();
                    TableDefinition tableDefinition = tableInfo.getTableDefinition();
                    if (StringUtils.hasText(tableFormat)) {
                        String tableName = tableFormat.replace("${tableName}", tableDefinition.getTableName()).replace("${description}", tableDefinition.getTableComment());
                        htmlBuilder.title(tableName, HTMLStyle.Title.LEVEL_2, tableBold ? HTMLStyle.Font.BOLD : null, tableNameFontSize, paddingBottom);
                    }
                    htmlBuilder.table(tableInfo.getDataList());
                    htmlBuilder.blankRow().blankRow();
                }
                htmlBuilder.blankRow();
                htmlBuilder.blankRow();
                htmlBuilder.blankRow();
                htmlBuilder.blankRow();
            }
        }
        htmlBuilder.build();
    }

}