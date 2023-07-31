package com.tining.anvilpanel.common;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeanUtils {
    private BeanUtils() {
    }

    /**
     * 拷贝单对象，避免拷贝null内容
     * @param source
     * @return
     */
    public static void copyBean(Object source, Object target) {
        // Create a BeanWrapper around the source object
        BeanWrapper src = new BeanWrapperImpl(source);

        // Get all property descriptors of the source object
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(PropertyDescriptor pd : pds) {
            // Check if the property value is null
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        // Get an array of the property names that are null
        String[] result = new String[emptyNames.size()];
        String[] nullProperties = emptyNames.toArray(result);

        // Copy properties from source to target, ignoring the ones that are null
        org.springframework.beans.BeanUtils.copyProperties(source, target, nullProperties);
    }


    /**
     * 类转map
     * @param obj
     * @return
     */
    public static Map<String, String> convertClassToMap(Object obj) {
        Map<String, String> resultMap = new HashMap<>();

        try {
            // 获取类的所有属性
            Field[] fields = obj.getClass().getDeclaredFields();

            for (Field field : fields) {
                // 检查属性是否被标记为 transient
                if (Modifier.isTransient(field.getModifiers())) {
                    continue; // 跳过 transient 属性
                }

                field.setAccessible(true);

                // 获取属性名和属性值
                String fieldName = field.getName();
                String fieldValue = String.valueOf(field.get(obj));

                // 将属性名和属性值添加到 Map 中
                resultMap.put(fieldName, fieldValue);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return resultMap;
    }
}