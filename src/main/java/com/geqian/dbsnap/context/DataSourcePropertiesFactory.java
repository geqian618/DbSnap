package com.geqian.dbsnap.context;

import com.geqian.dbsnap.DbSnapApplication;
import com.geqian.dbsnap.annotation.Marker;
import com.geqian.dbsnap.annotation.PackageScan;
import com.geqian.dbsnap.properties.AbstractDataSourceProperties;
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
public class DataSourcePropertiesFactory {
    private static final Map<String, Supplier<Class<? extends AbstractDataSourceProperties>>> CONNECTION_MAP = new HashMap<>();

    static {
        init();
    }


    private static void init() {

        ClassResourceScanner classScanner = new ClassResourceScanner();

        Predicate<ClassResourceScanner.ClassResourceWrapper> typeFilter = wrapper -> wrapper.isClass() && AbstractDataSourceProperties.class.isAssignableFrom(wrapper.getType()) && wrapper.isAnnotationPresent(Marker.class);

        PackageScan packageScan = DbSnapApplication.class.getAnnotation(PackageScan.class);

        List<ClassResourceScanner.ClassResourceWrapper> wrappers = classScanner.doScan(packageScan.value(), typeFilter);

        if (!CollectionUtils.isEmpty(wrappers)) {
            for (ClassResourceScanner.ClassResourceWrapper wrapper : wrappers) {
                Class<? extends AbstractDataSourceProperties> type = (Class<? extends AbstractDataSourceProperties>) wrapper.getType();
                Marker marker = type.getAnnotation(Marker.class);
                String key = marker.value();
                CONNECTION_MAP.put(key, () -> type);
            }
        }

    }

    /**
     * 获取Class对象
     *
     * @param type
     * @return
     */
    public static Class<? extends AbstractDataSourceProperties> getType(String type) {
        Supplier<Class<? extends AbstractDataSourceProperties>> supplier = CONNECTION_MAP.get(type);
        if (supplier == null) {
            throw new RuntimeException("未匹配到" + type + "对应的AbstractDataSourceProperties实现类");
        }
        return supplier.get();
    }


}
