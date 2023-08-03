package com.tining.anvilpanel.gui.admin.panel;

import com.tining.anvilpanel.common.PluginUtil;
import com.tining.anvilpanel.storage.GroupReader;
import com.tining.anvilpanel.model.Group;
import com.tining.anvilpanel.model.Panel;
import com.tining.anvilpanel.model.enums.SignMaterialEnum;
import com.tining.anvilpanel.storage.LangReader;
import com.tining.anvilpanel.storage.PanelReader;
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
 * 管理员面板设置GUI
 *
 * @author tinga
 */
public class AdminPanelSettingGUI {
    private static final Logger log = Logger.getLogger("Minecraft");
    /**
     * 当前开启的菜单
     */
    private static final Map<UUID, AdminPanelSettingGUI> MENU_OPENING = new ConcurrentHashMap();

    /**
     * 单页大小
     */
    private static final Integer PAGE_SIZE = 54;

    /**
     * 收购列表名称
     */
    public static final String GUI_NAME = "管理员命令设定列表";

    /**
     * 自身箱子界面
     */
    Inventory inventory;

    /**
     * 自身面板内容
     */
    Panel panel;


    /**
     * 构造GUI
     *
     * @param player
     */
    public AdminPanelSettingGUI(Player player, Panel panel) {
        this.inventory = Bukkit.createInventory(player, PAGE_SIZE, LangReader.get(GUI_NAME));
        this.panel = panel;
        drawPage(this.inventory, this);
        MENU_OPENING.put(player.getUniqueId(), this);
        player.openInventory(inventory);
    }

    private static void drawPage(Inventory inventory, AdminPanelSettingGUI adminPanelSettingGUI) {

        // 设置标语
        setSign(inventory, SignEnum.OATH_SETTING, SignMaterialEnum.LABEL, LangReader.get("权限设置") + "->", new ArrayList<>());
        // 全开放
        if (adminPanelSettingGUI.panel.isFree()) {
            setSign(inventory, SignEnum.FREE, SignMaterialEnum.ON, LangReader.get("向所有人开放") + ":ON", new ArrayList<>());
        } else {
            setSign(inventory, SignEnum.FREE, SignMaterialEnum.OFF, LangReader.get("向所有人开放") + ":OFF", new ArrayList<>());
        }
        // 权限组
        List<String> oathLore = new ArrayList<>();
        oathLore.add("anvilpanel.use" + "." + adminPanelSettingGUI.panel.getName());
        if (adminPanelSettingGUI.panel.isOath()) {
            setSign(inventory, SignEnum.OATH, SignMaterialEnum.ON, LangReader.get("使用权限节点限制") + ":ON", oathLore);
        } else {
            setSign(inventory, SignEnum.OATH, SignMaterialEnum.OFF, LangReader.get("使用权限节点限制") + ":OFF", oathLore);
        }
        // 自定义组
        setSign(inventory, SignEnum.GROUP, SignMaterialEnum.CONFIG, LangReader.get("自定义用户组授权"), new ArrayList<>());
        // 自定义玩家
        setSign(inventory, SignEnum.USER, SignMaterialEnum.CONFIG, LangReader.get("自定义玩家授权"), new ArrayList<>());

        // 设置配置标语
        setSign(inventory, SignEnum.SETTING, SignMaterialEnum.LABEL, LangReader.get("命令设置") + "->", new ArrayList<>());
        // 重置命令
        setSign(inventory, SignEnum.RESET_COMMAND, SignMaterialEnum.ANVIL, LangReader.get("重置命令"), new ArrayList<>());
        // 设置标志
        setSign(inventory, SignEnum.SIGN, SignMaterialEnum.PAINTING, LangReader.get("设置图标"), new ArrayList<>());
        // 设置删除
        setSign(inventory, SignEnum.DELETE, SignMaterialEnum.OFF, LangReader.get("删除"), new ArrayList<>());
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
        OATH_SETTING(11, "设置") {
            @Override
            public void deal(Inventory inventory, Player player) {
            }
        },
        SETTING(29, "设置") {
            @Override
            public void deal(Inventory inventory, Player player) {
            }
        },
        FREE(12, "所有玩家可用") {
            @Override
            public void deal(Inventory inventory, Player player) {
                AdminPanelSettingGUI adminPanelSettingGUI = MENU_OPENING.get(player.getUniqueId());
                Panel panel = adminPanelSettingGUI.panel;
                panel.setFree(!panel.isFree());
                PanelReader.getInstance().addToList(panel);
                AdminPanelSettingGUI.drawPage(inventory, adminPanelSettingGUI);
            }
        },
        OATH(13, "权限组限制") {
            @Override
            public void deal(Inventory inventory, Player player) {
                AdminPanelSettingGUI adminPanelSettingGUI = MENU_OPENING.get(player.getUniqueId());
                Panel panel = adminPanelSettingGUI.panel;
                panel.setOath(!panel.isOath());
                PanelReader.getInstance().addToList(panel);
                AdminPanelSettingGUI.drawPage(inventory, adminPanelSettingGUI);
            }
        },
        GROUP(14, "自定义组") {
            @Override
            public void deal(Inventory inventory, Player player) {
                Panel panel = MENU_OPENING.get(player.getUniqueId()).panel;
                player.closeInventory();
                // 一定要注销
                AdminPanelSettingGUI.unRegister(player);
                // 构造group
                List<Group> groupList = new ArrayList<>();
                for (String groupName : panel.getGroup()) {
                    groupList.add(GroupReader.getInstance().get(groupName));
                }
                new AdminPanelGroupListGUI(player, groupList, panel);
            }
        },
        USER(15, "自定义用户") {
            @Override
            public void deal(Inventory inventory, Player player) {
                Panel panel = MENU_OPENING.get(player.getUniqueId()).panel;
                player.closeInventory();
                // 一定要注销
                AdminPanelSettingGUI.unRegister(player);
                new AdminPanelUserListGUI(player, panel);
            }
        },
        RESET_COMMAND(30, "重置命令") {
            @Override
            public void deal(Inventory inventory, Player player) {
                AdminPanelSettingGUI adminPanelSettingGUI = MENU_OPENING.get(player.getUniqueId());
                Panel panel = adminPanelSettingGUI.panel;
                inventory.clear();
                new AdminPanelCreateGUI(player).setCommand(player, panel);
            }
        },
        SIGN(31, "设置图标") {
            @Override
            public void deal(Inventory inventory, Player player) {

                player.closeInventory();
            }
        },
        DELETE(33, "删除") {
            @Override
            public void deal(Inventory inventory, Player player) {
                Panel panel = MENU_OPENING.get(player.getUniqueId()).panel;
                player.closeInventory();
                unRegister(player);
                new AdminPanelDeleteGUI(player, panel);
            }
        };

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
