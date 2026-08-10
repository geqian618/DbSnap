package com.geqian.dbsnap.context;

import com.geqian.dbsnap.DbSnapApplication;
import com.geqian.dbsnap.annotation.Marker;
import com.geqian.dbsnap.annotation.PackageScan;
import com.geqian.dbsnap.entity.DefaultTableStructure;
import com.geqian.dbsnap.entity.TableStructure;
import com.geqian.dbsnap.utils.ClassResourceScanner;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author geqian
 * @date 3:13 2023/12/8
 */
public class TableStructureFactory {
    private static final Map<String, Supplier<Class<? extends TableStructure>>> TABLE_STRUCTURE_MAP = new HashMap<>();

    static {
        init();
    }


    private static void init() {

        ClassResourceScanner classScanner = new ClassResourceScanner();

        Predicate<ClassResourceScanner.ClassResourceWrapper> typeFilter = wrapper -> wrapper.isClass() && TableStructure.class.isAssignableFrom(wrapper.getType()) && wrapper.isAnnotationPresent(Marker.class);

        PackageScan packageScan = DbSnapApplication.class.getAnnotation(PackageScan.class);

        List<ClassResourceScanner.ClassResourceWrapper> wrappers = classScanner.doScan(packageScan.value(), typeFilter);

        if (!CollectionUtils.isEmpty(wrappers)) {
            for (ClassResourceScanner.ClassResourceWrapper wrapper : wrappers) {
                Class<? extends TableStructure> type = (Class<? extends TableStructure>) wrapper.getType();
                Marker marker = type.getAnnotation(Marker.class);
                String key = marker.value();
                TABLE_STRUCTURE_MAP.put(key.toLowerCase(), () -> type);
            }
        }

    }

    /**
     * 通过数据库类型，获取实体类class对象
     *
     * @param dbType
     * @return
     */
    public static Class<? extends TableStructure> getType(String dbType) {
        Supplier<Class<? extends TableStructure>> supplier = TABLE_STRUCTURE_MAP.get(dbType.toLowerCase());
        return supplier == null ? DefaultTableStructure.class : supplier.get();
    }


}
