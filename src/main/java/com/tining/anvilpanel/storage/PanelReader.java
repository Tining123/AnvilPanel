package com.tining.anvilpanel.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tining.anvilpanel.common.BeanUtils;
import com.tining.anvilpanel.model.Panel;
import com.tining.anvilpanel.model.enums.ConfigFileNameEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 用于管理Panel列表
 *
 * @author tinga
 */
public class PanelReader {

    private final static List<Panel> SAVE_LIST = new CopyOnWriteArrayList<>();

    /**
     * 添加到面板
     *
     * @param newPanel
     */
    public static void addToList(Panel newPanel) {
        synchronized (SAVE_LIST) {
            for (Panel panel : SAVE_LIST) {
                if (StringUtils.equals(panel.getName(), newPanel.getName())) {
                    // 如果有就直接修改
                    BeanUtils.copyBean(newPanel, panel);
                    saveAndReload();
                    return;
                }
            }
            SAVE_LIST.add(newPanel);
            saveAndReload();
        }
    }

    /**
     * 删除
     *
     * @param panel
     */
    public static void delete(Panel panel) {
        synchronized (SAVE_LIST) {
            for (int i = 0; i < SAVE_LIST.size(); i++) {
                if (StringUtils.equals(SAVE_LIST.get(i).getName(), panel.getName())) {
                    SAVE_LIST.remove(i);
                    break;
                }
            }
            saveAndReload();
        }
    }

    /**
     * 返回一个浏览用的副本
     *
     * @return
     */
    public static List<Panel> getForList() {
        return SAVE_LIST;
    }

    /**
     * 保存并重装商店信息
     */
    private static void saveAndReload() {
        FileConfiguration config = ConfigReader.getFileConfig(ConfigFileNameEnum.PANEL_FILE_NAME.getName());
        String rootSection = ConfigFileNameEnum.PANEL_FILE_NAME.getRootSection();
        config.addDefault(rootSection, formatPanelList(SAVE_LIST));
        config.set(rootSection, formatPanelList(SAVE_LIST));

        ConfigReader.saveConfig(ConfigFileNameEnum.PANEL_FILE_NAME.getName(), config);
        reload();
    }

    /**
     * 获取商店物品总价值表
     *
     * @return 商店物品总价值表
     */
    public static void reload() {
        FileConfiguration config = ConfigReader.getFileConfig(ConfigFileNameEnum.PANEL_FILE_NAME.getName());
        List<Panel> panelList = new ArrayList<>();
        List<Map<?, ?>> sourceList = config.getMapList(ConfigFileNameEnum.PANEL_FILE_NAME.getRootSection());
        for (Map<?, ?> map : sourceList) {
            Panel panel = new Panel();
            panel.setName((String) map.get("name"));
            panel.setText((String) map.get("text"));
            panel.setTitle((String) map.get("title"));
            panel.setCommand((String) map.get("command"));
//            panel.setSlot1(map.get("slot1") != null ? (String) map.get("slot1") : "BARRIER");
//            panel.setSlot2(map.get("slot2") != null ? (String) map.get("slot2") : "BARRIER");
//            panel.setSlot2Text(map.get("slot2text") != null ? (String) map.get("slot2text") : "");
//            panel.setSlot3(map.get("slot3") != null ? (String) map.get("slot3") : "BARRIER");

            panel.setOath(map.get("oath") != null && Boolean.parseBoolean((String) map.get("oath")));
            panel.setFree(map.get("free") != null && Boolean.parseBoolean((String) map.get("free")));

            // 处理特殊内容
            panel.setSubtitle(obj2List(map, "subtitle"));
            panel.setGroup(obj2List(map, "group"));
            panel.setUsers(obj2List(map, "users"));

            panelList.add(panel);
        }
        if (!CollectionUtils.isEmpty(panelList)) {
            synchronized (SAVE_LIST) {
                // 构建list
                SAVE_LIST.clear();
                SAVE_LIST.addAll(panelList);
            }
        }
    }

    /**
     * 根据下标获取panel
     *
     * @param index
     * @return
     */
    public static Panel getPanel(int index) {
        if (index < 0 || index >= SAVE_LIST.size()) {
            return null;
        }
        return SAVE_LIST.get(index);
    }

    /**
     * 根据名称获取panel
     *
     * @param
     * @return
     */
    public static Panel getPanel(String name) {
        for (Panel panel : SAVE_LIST) {
            if (StringUtils.equals(panel.getName(), name)) {
                return panel;
            }
        }
        return null;
    }

    /**
     * 格式化面板
     *
     * @param
     * @return
     */
    private static List<Map<String, String>> formatPanelList(List<Panel> panelList) {
        List<Map<String, String>> formatList = new ArrayList<>();

        for (int i = 0; i < panelList.size(); i++) {
            formatList.add(BeanUtils.convertClassToMap(panelList.get(i)));
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
