package com.tining.anvilpanel.model.enums;

import java.util.Objects;

/**
 * 标识文件数据库的枚举类
 * 用于释放文件使用
 * @author tinga
 */

public enum ConfigFileNameEnum {

    /**
     * 面板配置文件
     */
    PANEL_FILE_NAME("panels.yml", "panels"),


    /**
     * 自定义用户组
     */
    GROUP_FILE_NAME("groups.yml", "groups"),

    ;

    /**
     * 路径名称
     */
    private String name;

    private String rootSection;


    ConfigFileNameEnum(String s, String rootSection) {
        this.name = s;
        this.rootSection = rootSection;
    }

    /**
     * 通过名称获得枚举
     * @param s
     * @return
     */
    public ConfigFileNameEnum getType(String s) {
        for(ConfigFileNameEnum w: ConfigFileNameEnum.values()){
            if(Objects.equals(s,w)){
                return w;
            }
        }
        return null;
    }

    /**
     * 通过枚举获得名称
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     * 通过枚举获得根节点
     * @return
     */
    public String getRootSection(){
        return rootSection;
    }

}
