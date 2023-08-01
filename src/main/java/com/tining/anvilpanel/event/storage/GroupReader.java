package com.tining.anvilpanel.event.storage;

import com.tining.anvilpanel.common.BeanUtils;
import com.tining.anvilpanel.model.Group;
import com.tining.anvilpanel.model.enums.ConfigFileNameEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class GroupReader extends IListReader<Group>{

    private final static GroupReader INSTANCE = new GroupReader();

    public static GroupReader getInstance(){return INSTANCE;}

    private final List<Group> SAVE_LIST = new CopyOnWriteArrayList<>();

    /**
     * 添加到面板
     */
    @Override
    public void addToList(Group newGroup){
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
    @Override
    public void delete(Group group){
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
    @Override
    public List<Group> getForList(){
        return SAVE_LIST;
    }

    /**
     * 保存并重装商店信息
     */
    @Override
    public void saveAndReload(){
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
    @Override
    public void reload() {
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
    @Override
    public Group get(int index){
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
    @Override
    public Group get(String name){
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

}
