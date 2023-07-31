package com.tining.anvilpanel.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tining.anvilpanel.common.BeanUtils;
import com.tining.anvilpanel.model.Group;
import com.tining.anvilpanel.model.enums.ConfigFileNameEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class GroupReader {

    private final static List<Group> SAVE_LIST = new CopyOnWriteArrayList<>();

    /**
     * 添加到面板
     */
    public static void addToList(Group newGroup){
        synchronized (SAVE_LIST) {
            for (Group group : SAVE_LIST) {
                if (StringUtils.equals(group.getName(), newGroup.getName())) {
                    // 如果有就直接修改
                    BeanUtils.copyBean(newGroup,group);
                    saveAndReload();
                    return;
                }
            }
            SAVE_LIST.add(newGroup);
            saveAndReload();
        }
    }


    /**
     * 删除
     * @param group
     */
    public static void delete(Group group){
        synchronized (SAVE_LIST) {
            for (int i = 0 ; i< SAVE_LIST.size();i++) {
                if(StringUtils.equals(SAVE_LIST.get(i).getName(),group.getName())){
                    SAVE_LIST.remove(i);
                    break;
                }
            }
            saveAndReload();
        }
    }

    /**
     * 返回一个浏览用的副本
     * @return
     */
    public static List<Group> getForList(){
        return SAVE_LIST;
    }

    /**
     * 保存并重装商店信息
     */
    private static void saveAndReload(){
        FileConfiguration config = ConfigReader.getFileConfig(ConfigFileNameEnum.GROUP_FILE_NAME.getName());
        String rootSection = ConfigFileNameEnum.GROUP_FILE_NAME.getRootSection();
        config.addDefault(rootSection, formatList(SAVE_LIST));
        config.set(rootSection, formatList(SAVE_LIST));

        ConfigReader.saveConfig(ConfigFileNameEnum.GROUP_FILE_NAME.getName(), config);
        reload();
    }

    /**
     * 获取商店物品总价值表
     * @return 商店物品总价值表
     */
    public static void reload() {
        FileConfiguration config = ConfigReader.getFileConfig(ConfigFileNameEnum.GROUP_FILE_NAME.getName());
        List<Group> groupList = new ArrayList<>();
        List<Map<?,?>> sourceList = config.getMapList(ConfigFileNameEnum.GROUP_FILE_NAME.getRootSection());
        for (Map<?, ?> map : sourceList) {
            Group group = new Group();
            group.setName((String) map.get("name"));
            group.setUsers(obj2List(map, "users"));

            groupList.add(group);
        }
        if(!CollectionUtils.isEmpty(groupList)){
            synchronized (SAVE_LIST) {
                // 构建list
                SAVE_LIST.clear();
                SAVE_LIST.addAll(groupList);
            }
        }
    }

    /**
     * 根据下标获取
     * @param index
     * @return
     */
    public static Group get(int index){
        if(index < 0 || index >= SAVE_LIST.size()){
            return null;
        }
        return SAVE_LIST.get(index);
    }

    /**
     * 根据名称获取
     * @param
     * @return
     */
    public static Group get(String name){
        for (Group group : SAVE_LIST) {
            if (StringUtils.equals(group.getName(), name)) {
                return group;
            }
        }
        return null;
    }

    /**
     * 格式化
     * @param
     * @return
     */
    private static List<Map<String,String>> formatList(List<Group> groupList){
        List<Map<String,String>> formatList = new ArrayList<>();

        for (int i = 0 ; i < groupList.size();i ++){
            formatList.add(BeanUtils.convertClassToMap(groupList.get(i)));
        }
        return formatList;
    }

    /**
     * Map<?, ?>转 string + List<String>
     *
     * @return
     */
    private static List<String> obj2List(Map<?, ?> map, String key) {
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
