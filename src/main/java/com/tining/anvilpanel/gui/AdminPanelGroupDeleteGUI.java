package com.tining.anvilpanel.gui;

import com.tining.anvilpanel.common.PluginUtil;
import com.tining.anvilpanel.model.Group;
import com.tining.anvilpanel.model.Panel;
import com.tining.anvilpanel.model.enums.PublicSignEnumInterface;
import com.tining.anvilpanel.model.enums.SignMaterialEnum;
import com.tining.anvilpanel.storage.GroupReader;
import com.tining.anvilpanel.storage.LangReader;
import com.tining.anvilpanel.storage.PanelReader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class AdminPanelGroupDeleteGUI {
    private static final Logger log = Logger.getLogger("Minecraft");
    /**
     * 当前开启的菜单
     */
    private static final Map<UUID, AdminPanelGroupDeleteGUI> MENU_OPENING = new ConcurrentHashMap();

    /**
     * 单页大小
     */
    private static final Integer PAGE_SIZE = 54;

    /**
     * 可视区域大小
     */
    public static final Integer VIEW_SIZE = 45;
    /**
     * 收购列表名称
     */
    public static final String GUI_NAME = "管理员用户组用户删除面板";

    /**
     * 自身箱子界面
     */
    Inventory inventory;

    /**
     * 选中的面板
     */
    Panel panel;

    /**
     * 选中的组
     */
    String selectGroup;

    /**
     * 构造GUI
     *
     * @param player
     */
    public AdminPanelGroupDeleteGUI(Player player, Panel panel, String group) {
        this.inventory = Bukkit.createInventory(player, PAGE_SIZE, LangReader.get(GUI_NAME));
        this.selectGroup = group;
        this.panel = panel;
        MENU_OPENING.put(player.getUniqueId(), this);
        player.openInventory(inventory);
        drawPage(this.inventory, player);
    }

    private static void drawPage(Inventory inventory, Player player) {
        String group = MENU_OPENING.get(player.getUniqueId()).selectGroup;
        // 设置功能性图标
        PluginUtil.setSign(inventory, AdminPanelGroupDeleteGUI.SignEnum.GROUP, SignMaterialEnum.LABEL, group, new ArrayList<>());
        PluginUtil.setSign(inventory, AdminPanelGroupDeleteGUI.SignEnum.CONFIRM, SignMaterialEnum.ON, LangReader.get("确认"), new ArrayList<>());
        PluginUtil.setSign(inventory, AdminPanelGroupDeleteGUI.SignEnum.CANCEL, SignMaterialEnum.OFF, LangReader.get("取消"), new ArrayList<>());
    }


    /**
     * 判断是否应该响应
     *
     * @param player
     * @param title
     * @return
     */
    public static boolean shouldEffective(Player player, String title) {
        return MENU_OPENING.containsKey(player.getUniqueId()) ||
                StringUtils.equals(title, LangReader.get(GUI_NAME));
    }

    /**
     * 取消注册
     *
     * @param player
     */
    public static void unRegister(Player player) {
        MENU_OPENING.remove(player.getUniqueId());
    }

    /**
     * 用于标记按钮坐标的枚举
     */
    public enum SignEnum implements PublicSignEnumInterface {
        /**
         * 坐标集合
         */
        CONFIRM(41, "确认") {
            @Override
            public void deal(Inventory inventory, Player player) {
                String group = MENU_OPENING.get(player.getUniqueId()).selectGroup;
                Panel panel = MENU_OPENING.get(player.getUniqueId()).panel;
                if (Objects.isNull(panel)) {
                    player.sendMessage(LangReader.get("删除发生异常"));
                    return;
                }
                if (CollectionUtils.isNotEmpty(panel.getGroup())) {
                    panel.getGroup().remove(group);
                }
                player.closeInventory();
                unRegister(player);
                PanelReader.addToList(panel);
                player.sendMessage(LangReader.get("删除成功") + ":" + panel.getName() + "->" + group);
            }
        },
        CANCEL(39, "取消") {
            @Override
            public void deal(Inventory inventory, Player player) {
                player.closeInventory();
            }
        },
        GROUP(13, "选中组") {
            @Override
            public void deal(Inventory inventory, Player player) {

            }
        },

        ;

        /**
         * 坐标
         */
        private final int slot;

        /**
         * 标签
         */
        private final String label;

        /**
         * 构造函数
         *
         * @param slot  坐标
         * @param label 标签
         */
        SignEnum(int slot, String label) {
            this.slot = slot;
            this.label = label;
        }

        /**
         * 坐标 getter
         *
         * @return 坐标
         */
        @Override
        public int getSlot() {
            return slot;
        }

        /**
         * 标签 getter
         *
         * @return 标签
         */
        @Override
        public String getLabel() {
            return label;
        }

        /**
         * 查找与给定的整数匹配的枚举
         *
         * @param checkSlot 要检查的整数
         * @return 如果找到匹配的枚举，则返回该枚举，否则返回 null
         */
        public static AdminPanelGroupDeleteGUI.SignEnum findMatchedSign(int checkSlot) {
            for (AdminPanelGroupDeleteGUI.SignEnum sign : AdminPanelGroupDeleteGUI.SignEnum.values()) {
                if (sign.getSlot() == checkSlot) {
                    return sign;
                }
            }
            return null;
        }

        /**
         * 检查给定的整数是否匹配任何坐标
         *
         * @param checkSlot 要检查的整数
         * @return 如果整数匹配任何坐标，则返回 true，否则返回 false
         */
        public static boolean isMatchedSlot(int checkSlot) {
            return findMatchedSign(checkSlot) != null;
        }

        public abstract void deal(Inventory inventory, Player player);
    }
}
