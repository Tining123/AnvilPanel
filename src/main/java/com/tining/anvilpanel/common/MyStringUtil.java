package com.tining.anvilpanel.common;

import com.tining.anvilpanel.model.enums.PlaceholderEnum;

/**
 * 工具类
 * @author tinga
 */
public class MyStringUtil {
    public static String getWordsAround(String str, String sub, int n){
        String[] split = str.split("\\s+");
        int count = 0;
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals(sub)) {
                count++;
                if (count == n) {
                    String leftWord = (i > 0) ? split[i - 1] : "";
                    String rightWord = (i < split.length - 1) ? split[i + 1] : "";
                    return leftWord + " " + sub + " " + rightWord;
                }
            }
        }
        return "";
    }

    public static String getWordsAroundT(String str, int n){
        String sub = PlaceholderEnum.PARAM_TEXT.getText();
        String[] split = str.split("\\s+");
        int count = 0;
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals(sub)) {
                count++;
                if (count == n) {
                    String leftWord = (i > 0) ? split[i - 1] : "";
                    String rightWord = (i < split.length - 1) ? split[i + 1] : "";
                    return "§0" + leftWord + " §4" + sub + " §0" + rightWord;
                }
            }
        }
        return "";
    }
}
