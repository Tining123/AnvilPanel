package com.tining.anvilpanel.event.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class IListReader<T> {
    abstract void addToList(T newObj);
    abstract void delete(T obj);
    abstract List<T> getForList();
    abstract void saveAndReload();
    abstract void reload();
    abstract T get(int index);
    abstract T get(String name);


    /**
     * Map<?, ?>转 string + List<String>
     *
     * @return
     */
    protected List<String> obj2List(Map<?, ?> map, String key) {
        if (Objects.isNull(map.get(key))) {
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> value = gson.fromJson((String) map.get(key), type);
        if(Objects.isNull(value)){
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        for (Object item : value) {
            // 在此处根据实际情况决定转换方式
            // 如果你确定列表中的对象可以安全地转换为String，可以直接调用toString()
            result.add(item.toString());
        }
        return result;
    }
}