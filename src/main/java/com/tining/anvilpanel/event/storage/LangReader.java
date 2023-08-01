package com.tining.anvilpanel.event.storage;

import com.tining.anvilpanel.AnvilPanel;
import com.tining.anvilpanel.model.enums.LangEnum;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * 语言加载文件
 *
 * @author tinga
 */
public class LangReader {

    /**
     * 本地区域名称
     */
    private static String LOCALE = LangEnum.ENGLISH.getLanguage(Locale.getDefault());

    /**
     * main函数实体
     */
    private static final AnvilPanel MAIN = AnvilPanel.getInstance();

    /**
     * 子目录
     */
    private static final String SUB_FOLDER_NAME = "lang";

    /**
     * 文件目录
     */
    private static final File ROOT_FOLDER = new File(MAIN.getDataFolder(), SUB_FOLDER_NAME);

    /**
     * 翻译词典
     */
    private static final Map<String, String> DICTIONARY = new HashMap<>();

    /**
     * 初次释放配置文件
     */
    public static void initRelease() {
        String configName = LOCALE + ".yml";
        if (!ROOT_FOLDER.exists()) {
            ROOT_FOLDER.mkdir();
        }
        try {
            //不覆盖
            MAIN.saveResource(SUB_FOLDER_NAME + "/" + configName, false);
        } catch (Exception e) {
            MAIN.getLogger().info(e.toString());
        }
    }

    /**
     * 重载语言
     */
    public static void reloadLang() {
        String configName = LOCALE + ".yml";
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(new File(ROOT_FOLDER, configName));
        ConfigurationSection getCon = configuration.getConfigurationSection("lang");
        if(Objects.isNull(getCon)) {
            return;
        }
        Map<String, Object> map = getCon.getValues(false);

        map.entrySet().stream().forEach((e) -> {
            DICTIONARY.put(e.getKey(), (String) e.getValue());
        });
    }

    /**
     * 强制设定预言
     */
    public static void setLanguage(String language) {
        LOCALE = LangEnum.ENGLISH.getLanguage(language);
    }

    /**
     * 获取当前预言设定
     *
     * @return
     */
    public static String getLanguage() {
        return LOCALE;
    }

    /**
     * 获取字典
     *
     * @return
     */
    public static Map<String, String> getDictionary() {
        return DICTIONARY;
    }

    /**
     * 查字典
     * @param str
     * @return
     */
    public static String get(String str){
        if(LangReader.getDictionary().isEmpty()){
            return str;
        }

        Map<String,String> map = LangReader.getDictionary();

        if(map.containsKey(str)){
            return map.get(str);
        }

        return str;
    }
}
