package com.tining.anvilpanel.model.enums;

/**
 * @author tinga
 */
public enum PlaceholderEnum {

    /**
     * 玩家名称
     */
    PLAYER_NAME("player name", "[p]"),


    /**
     * 命令
     */
    PARAM_TEXT("param text ", "[t]"),


    /**
     * 分隔符
     */
    SPLITER("spliter ", ";"),

    OP_COMMAND("op command", "op:"),

    ;

    private final String name;
    private final String text;

    /**
     * 枚举的构造函数
     * @param name
     * @param text
     */
    PlaceholderEnum(String name, String text) {
        this.name = name;
        this.text = text;
    }

    /**
     * 获取 name 变量的值
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * text 变量的值
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * 根据 text 获取对应的枚举量
     * @param text
     * @return
     */
    public static PlaceholderEnum getByText(String text) {
        for (PlaceholderEnum placeholderEnum : PlaceholderEnum.values()) {
            if (placeholderEnum.getText().equals(text)) {
                return placeholderEnum;
            }
        }
        // 如果找不到匹配的枚举量，返回 null 或抛出异常，具体根据需要处理
        return null; 
    }

    /**
     * 根据 name 获取对应的枚举量
     * @param name
     * @return
     */
    public static PlaceholderEnum getByName(String name) {
        for (PlaceholderEnum placeholderEnum : PlaceholderEnum.values()) {
            if (placeholderEnum.getName().equals(name)) {
                return placeholderEnum;
            }
        }
        // 如果找不到匹配的枚举量，返回 null 或抛出异常，具体根据需要处理
        return null; 
    }

    /**
     * 翻译命令
     * @param command
     * @param playerName
     * @param param
     * @return
     */
    public static String transCommand(String command, String playerName, String param){
      return command.replace(PLAYER_NAME.getText(), playerName).replace(PARAM_TEXT.getText(), param) ;
    }
}
