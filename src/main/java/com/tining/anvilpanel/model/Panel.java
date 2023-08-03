package com.tining.anvilpanel.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author tinga
 */
@Data
public class Panel {

    /**
     * 名称
     */
    String name;

    /**
     * 标题
     */
    String title;

    /**
     * 懒人变量的标题
     */
    List<String> subtitle;

    /**
     * 变量
     */
    Map<String,String> vars;

    /**
     * 权限组
     */
    List<String> group;

    /**
     * 权限人员
     */
    List<String> users;

    /**
     * 命令
     */
    String command;

    /**
     * 物品标志
     */
    String itemSign;

    /**
     * 是否所有人都可以用
     */
    boolean free;

    /**
     * 是否开启权限组限制
     */
    boolean oath;


//    /**
//     * 默认文本
//     */
//    String text;
//
//    /**
//     * 输入栏1
//     */
//    String slot1;
//
//    /**
//     * 输入栏2
//     */
//    String slot2;
//
//    /**
//     * 输入栏2文本
//     */
//    String slot2Text;
//
//    /**
//     * 输出栏
//     */
//    String slot3;
}
