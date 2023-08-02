package com.tining.anvilpanel.storage;

import com.tining.anvilpanel.AnvilPanel;
import com.tining.anvilpanel.model.enums.ConfigFileNameEnum;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

/**
 * 配置文件管理
 */
public final class ConfigReader {


    /**
     * main函数实体
     */
    private static AnvilPanel main = AnvilPanel.getInstance();

    /**
     * 插件目录
     */
    private static final File ROOT_FOLDER = main.getDataFolder();

    /**
     * 主配置文件
     */
    private static FileConfiguration config = AnvilPanel.getInstance().getConfig();

    /**
     * 表配置文件
     */
    private static final Map<String, FileConfiguration> configMap = new HashMap<>();

    /**
     * 返回配置表，不包含config主配置
     *
     * @return
     */
    public static Map<String, FileConfiguration> getConfigMap() {
        return configMap;
    }

    /**
     * 重载所有配置文件
     */
    public static void reloadConfig() {
        //重载主配置文件
        AnvilPanel.getInstance().reloadConfig();
        config = AnvilPanel.getInstance().getConfig();
        //加载语言文件
        try {
            LangReader.reloadLang();
        }catch (Exception ignore){}
        //重载配置表中的文件到内存缓存中
        for (ConfigFileNameEnum w : ConfigFileNameEnum.values()) {
            String configName = w.getName();
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(new File(ROOT_FOLDER, configName));
            configMap.put(configName, configuration);
        }
        // 重载面板
        PanelReader.getInstance().reload();
        GroupReader.getInstance().reload();
    }

    /**
     * 获取原初文件配置
     * @param name
     * @return
     */
    public static FileConfiguration getFileConfig(String name){
        return ConfigReader.getConfigMap().get(name);
    }

    /**
     * 保存并重载插件
     */
    public static void saveConfig(String fileName, FileConfiguration fileConfiguration) {
        //重载配置表中的文件
        try {
            fileConfiguration.save(new File(ROOT_FOLDER, fileName));
        } catch (Exception e) {
            main.getLogger().info(e.toString());
        }
    }

    /**
     * 保存并重载插件
     */
    public static void saveConfig() {
        AnvilPanel.getInstance().saveConfig();
        //重载配置表中的文件
        for (ConfigFileNameEnum w : ConfigFileNameEnum.values()) {
            String configName = w.getName();
            if (!Objects.isNull(configMap.get(configName))) {
                try {
                    configMap.get(configName).save(configName);
                } catch (Exception e) {
                    AnvilPanel.getInstance().getLogger().info(e.toString());
                }
            }
        }
        reloadConfig();
    }

    /**
     * 初次释放配置文件
     */
    public static void initRelease() {
        for (ConfigFileNameEnum w : ConfigFileNameEnum.values()) {
            String configName = w.getName();
            try {
                File configFile = new File(ROOT_FOLDER, configName);
                if (!configFile.exists()) {
                    main.saveResource(configName, false);
                }
            } catch (Exception e) {
                main.getLogger().info(e.toString());
            }
        }
    }

    /**
     * 获取当前语言
     * @return
     */
    public static String getLanguage() {
        return ConfigReader.config.getString("lang");
    }


    /**
     * 获取op名称
     *
     * @return
     */
    public static String getOP() {
        return config.getString("OP");
    }


}