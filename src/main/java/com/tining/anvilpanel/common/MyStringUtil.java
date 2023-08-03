package com.tining.anvilpanel.common;

import com.tining.anvilpanel.model.enums.PlaceholderEnum;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * @author tinga
 */
public class MyStringUtil {

    private static final Pattern VAR_PATTERN = Pattern.compile("\\[v(\\d+)]");

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

    public static String getWordsAroundV(String str, String sub, int n){
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


    /**
     * 获取变量替换符
     * @param input
     * @return
     */
    public static Set<String> getVarPlaceholders(String input) {
        Set<String> placeholders = new TreeSet<>(new NaturalOrderComparator());
        Matcher matcher = VAR_PATTERN.matcher(input);

        while (matcher.find()) {
            String placeholder = matcher.group();
            placeholders.add(placeholder);
        }

        return placeholders;
    }

    /**
     * 自定义比较器，实现按照自然数排序
     */
    static class NaturalOrderComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            // 提取字符串中的数字部分，并转换为整数
            int n1 = Integer.parseInt(s1.substring(2, s1.length() - 1));
            int n2 = Integer.parseInt(s2.substring(2, s2.length() - 1));

            // 返回比较结果
            return Integer.compare(n1, n2);
        }
    }
}
