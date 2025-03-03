package com.geqian.dbsnap.entity;

import com.geqian.dbsnap.bo.FieldDefinition;
import com.geqian.dbsnap.db.DefaultColumnHolder;
import com.geqian.document4j.common.annotation.TableField;
import com.geqian.document4j.html.HTMLTableInterceptor;
import com.geqian.document4j.md.MarkdownTableInterceptor;
import com.geqian.document4j.pdf.PDFTableInterceptor;
import com.geqian.document4j.word.WordTableInterceptor;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 列字段信息
 *
 * @author geqian
 * @date 10:45 2023/1/5
 */
@Data
public abstract class TableStructure implements WordTableInterceptor, PDFTableInterceptor, MarkdownTableInterceptor, HTMLTableInterceptor {

    @TableField(value = "序号", order = 1)
    private Integer number;


    @TableField(value = "列名", order = 2)
    private String columnName;


    @TableField(value = "数据类型", order = 3)
    private String columnType;


    @TableField(value = "允许空值", order = 4, exclude = true, enums = {"Y->YES", "N->NO"})
    private String isNullable;


    @TableField(value = "默认值", order = 5, enums = "null->NULL", exclude = true)
    private String columnDefault;


    @TableField(value = "备注", order = 6, enums = "null-> ")
    private String columnComment;

    @Override
    public List<String> interceptHtmlHeaders(List<String> headers, Class<?> type) {
        return getHeaders();
    }

    @Override
    public List<String> interceptMdHeaders(List<String> headers, Class<?> type) {
        return getHeaders();
    }

    @Override
    public List<String> interceptPdfHeaders(Class<?> type, List<String> headers) {
        return getHeaders();
    }

    @Override
    public List<String> interceptWordHeaders(Class<?> type, List<Field> fields, List<String> headers) {
        return getHeaders();
    }

    @Override
    public List<Field> interceptPdfFields(Class<?> type, List<Field> fields) {
        return getFields(type);
    }


    @Override
    public List<Field> interceptWordFields(Class<?> type, List<Field> fields) {
        return getFields(type);
    }


    @Override
    public List<Field> interceptMdFields(Class<?> type, List<Field> fields) {
        return getFields(type);
    }

    @Override
    public List<Field> interceptHtmlFields(List<Field> fields, Class<?> type) {
        return getFields(type);
    }


    private List<Field> getFields(Class<?> objectType) {
        List<Class<?>> classes = new ArrayList<>();
        Class<?> classType = objectType;
        while (!Objects.equals(classType, Object.class)) {
            classes.add(classType);
            classType = classType.getSuperclass();
        }

        Map<String, Field> fieldMap = classes.stream()
                .map(pojoClass -> Stream.of(pojoClass.getDeclaredFields()).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .filter(field -> field.isAnnotationPresent(TableField.class))
                .collect(Collectors.toMap(Field::getName, Function.identity(), (oldVal, newVal) -> oldVal));

        List<FieldDefinition> fieldDefinitions = DefaultColumnHolder.getDefaultColumns();

        return fieldDefinitions.stream()
                .map(fieldDefinition -> fieldMap.get(fieldDefinition.getValue()))
                .collect(Collectors.toList());

    }


    private List<String> getHeaders() {
        return DefaultColumnHolder.getDefaultColumns()
                .stream()
                .map(FieldDefinition::getLabel)
                .collect(Collectors.toList());
    }

}
