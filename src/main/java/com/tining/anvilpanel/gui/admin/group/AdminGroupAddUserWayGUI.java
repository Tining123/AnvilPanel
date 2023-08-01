package com.tining.anvilpanel.gui.admin.group;

import com.tining.anvilpanel.common.PluginUtil;
import com.tining.anvilpanel.model.Group;
import com.tining.anvilpanel.model.enums.SignMaterialEnum;
import com.tining.anvilpanel.event.storage.LangReader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * 选择增加用户的途径
 */
public class AdminGroupAddUserWayGUI {
    private static final Logger log = Logger.getLogger("Minecraft");
    /**
     * 当前开启的菜单
     */
    private static final Map<UUID, AdminGroupAddUserWayGUI> MENU_OPENING = new ConcurrentHashMap();

    /**
     * 单页大小
     */
    private static final Integer PAGE_SIZE = 54;

    /**
     * 收购列表名称
     */
    public static final String GUI_NAME = "管理员选择添加方式";

    /**
     * 自身箱子界面
     */
    Inventory inventory;

    /**
     * 自身组
     */
    Group group;


    /**
     * 构造GUI
     *
     * @param player
     */
    public AdminGroupAddUserWayGUI(Player player, Group group) {
        this.inventory = Bukkit.createInventory(player, PAGE_SIZE, LangReader.get(GUI_NAME));
        this.group = group;
        drawPage(this.inventory, this);
        MENU_OPENING.put(player.getUniqueId(), this);
        player.openInventory(inventory);
    }

    private static void drawPage(Inventory inventory, AdminGroupAddUserWayGUI AdminGroupAddUserWayGUI) {

        setSign(inventory, SignEnum.ONLINE, SignMaterialEnum.PLAYER, LangReader.get("选择在线玩家"), new ArrayList<>());
        setSign(inventory, SignEnum.INPUT, SignMaterialEnum.PAGE, LangReader.get("手动输入"), new ArrayList<>());

    }

    /**
     * 设置标签
     *
     * @param signEnum
     * @param signMaterialEnum
     * @param label
     * @param lore
     */
    private static void setSign(Inventory inventory, SignEnum signEnum, SignMaterialEnum signMaterialEnum
            , String label, List<String> lore) {
        Material material = signMaterialEnum.getMaterial();
        if (Objects.isNull(material)) {
            return;
        }
        ItemStack itemStack = new ItemStack(material);
        if (!CollectionUtils.isEmpty(lore)) {
            PluginUtil.addLore(itemStack, lore);
        }
        PluginUtil.setName(itemStack, label);
        inventory.setItem(signEnum.getSlot(), itemStack);
    }

    /**
     * 判断是否应该翻页
     *
     * @param player
     * @param title
     * @return
     */
    public static boolean shouldTurnPage(Player player, String title) {
        return MENU_OPENING.containsKey(player.getUniqueId()) ||
                StringUtils.equals(title, LangReader.get(GUI_NAME));
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
    public enum SignEnum {
        /**
         * 坐标集合
         */
        ONLINE(21, "在线") {
            @Override
            public void deal(Inventory inventory, Player player) {
                Group nowGroup = MENU_OPENING.get(player.getUniqueId()).group;
                player.closeInventory();
                // 一定要注销
                unRegister(player);
                List<String> onlineUser = new ArrayList<>();
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlineUser.add(onlinePlayer.getDisplayName());
                }
                new AdminGroupAddUserChestGUI(player, nowGroup, onlineUser);
            }
        },
        INPUT(23, "手动输入") {
            @Override
            public void deal(Inventory inventory, Player player) {
                Group nowGroup = MENU_OPENING.get(player.getUniqueId()).group;
                player.closeInventory();
                // 一定要注销
                unRegister(player);
                new AdminGroupAddUserAnvilGUI().openGUI(player, nowGroup);
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
        public int getSlot() {
            return slot;
        }

        /**
         * 标签 getter
         *
         * @return 标签
         */
        public String getLabel() {
            return label;
        }

        /**
         * 查找与给定的整数匹配的枚举
         *
         * @param checkSlot 要检查的整数
         * @return 如果找到匹配的枚举，则返回该枚举，否则返回 null
         */
        public static SignEnum findMatchedSign(int checkSlot) {
            for (SignEnum sign : SignEnum.values()) {
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
