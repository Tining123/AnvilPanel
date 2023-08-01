package com.tining.anvilpanel.gui.admin.group;

import com.tining.anvilpanel.common.PluginUtil;
import com.tining.anvilpanel.event.storage.GroupReader;
import com.tining.anvilpanel.event.storage.IListReader;
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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * 管理员管理用户组界面
 */
public class AdminGroupListGUI {
    private static final Logger log = Logger.getLogger("Minecraft");
    /**
     * 当前开启的菜单
     */
    private static final Map<UUID, AdminGroupListGUI> MENU_OPENING = new ConcurrentHashMap();

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
    public static final String GUI_NAME = "管理员用户组列表";

    /**
     * 自身箱子界面
     */
    Inventory inventory;

    /**
     * 选中的
     */
    Group selectItem;

    /**
     * 构造GUI
     *
     * @param player
     */
    public AdminGroupListGUI(Player player) {
        this.inventory = Bukkit.createInventory(player, PAGE_SIZE, LangReader.get(GUI_NAME));
        MENU_OPENING.put(player.getUniqueId(), this);
        player.openInventory(inventory);
        drawPage(inventory, 0, player);
    }

    private static void drawPage(Inventory inventory, int pageNum, Player player) {
        List<Group> groupList = GroupReader.getInstance().getForList();
        int move = 0;
        boolean set = false;

        List<ItemStack> list = new ArrayList<>();
        for (Group group : groupList) {
            Material signMaterial = SignMaterialEnum.CONFIG.getMaterial();
            ItemStack sign = new ItemStack(signMaterial);
            PluginUtil.setName(sign, group.getName());
            list.add(sign);
        }

        for (int i = pageNum * VIEW_SIZE; i < list.size() && i < (pageNum + 1) * VIEW_SIZE; i++) {
            if (!Objects.isNull(list.get(i))) {
                if (!set) {
                    inventory.clear();
                    set = true;
                }
                inventory.setItem(move % VIEW_SIZE, list.get(i));
                move++;
            }
        }
        //设置翻页图标
        ItemStack left = new ItemStack(SignMaterialEnum.LEFT.getMaterial(), 1);
        ItemStack right = new ItemStack(SignMaterialEnum.RIGHT.getMaterial(), 1);
        ItemStack mid = new ItemStack(SignMaterialEnum.PAGE.getMaterial(), 1);

        Group selectItem = AdminGroupListGUI.MENU_OPENING.get(player.getUniqueId()).selectItem;
        if (Objects.nonNull(selectItem)) {
            mid = new ItemStack(SignMaterialEnum.GROUP.getMaterial(), 1);
        }

        ItemMeta leftItemMeta = left.getItemMeta();
        ItemMeta rightItemMeta = right.getItemMeta();
        ItemMeta midItemMeta = right.getItemMeta();


        if (!Objects.isNull(leftItemMeta) && pageNum != 0 && pageNum != 1) {
            leftItemMeta.setDisplayName(LangReader.get("上一页"));
            left.setItemMeta(leftItemMeta);
            inventory.setItem(SignEnum.LAST.slot, left);
        }
        if (!Objects.isNull(rightItemMeta) && move != 0 && (pageNum + 1) * VIEW_SIZE < list.size()) {
            rightItemMeta.setDisplayName(LangReader.get("下一页"));
            right.setItemMeta(rightItemMeta);
            inventory.setItem(SignEnum.NEXT.slot, right);
        }
        if (!Objects.isNull(midItemMeta)) {
            midItemMeta.setDisplayName("< " + (pageNum + 1) + " >");
            mid.setItemMeta(midItemMeta);
            if (Objects.nonNull(selectItem)) {
                PluginUtil.addLore(mid, Collections.singletonList(selectItem.getName() + ":" + selectItem.getUsers().size()));
            }

            inventory.setItem(SignEnum.PAGE.slot, mid);
        }
        // 设置功能性图标
        setSign(inventory, SignEnum.VIEW, SignMaterialEnum.LABEL, LangReader.get("查看组"), new ArrayList<>());
        setSign(inventory, SignEnum.CREATE, SignMaterialEnum.ANVIL, LangReader.get("创建组"), new ArrayList<>());
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
     * 绘制第N页的列表
     */
    public static void turnPage(Inventory inventory, int slot, Player player) {
        try {
            ItemStack itemStack = inventory.getItem(SignEnum.PAGE.slot);
            String name = itemStack.getItemMeta().getDisplayName();
            int page = Integer.parseInt(name.replace("<", "").replace(">", "").trim());

            if (Objects.equals(slot, SignEnum.LAST.slot)) {
                if (page < 2) {
                    return;
                }
                drawPage(inventory, page - 2, player);
                return;
            }

            if (Objects.equals(slot, SignEnum.NEXT.slot)) {
                drawPage(inventory, page, player);
                return;
            }
        } catch (Exception ignre) {
        }
    }


    /**
     * 获取选中的内容
     *
     * @param inventory
     * @param slot
     * @return
     */
    public static void setSelectItem(Inventory inventory, int slot, Player player) {
        AdminGroupListGUI adminGroupListGUI = MENU_OPENING.get(player.getUniqueId());
        if (slot >= VIEW_SIZE) {
            adminGroupListGUI.selectItem = null;
        }

        try {
            ItemStack itemStack = inventory.getItem(SignEnum.PAGE.slot);
            String name = itemStack.getItemMeta().getDisplayName();
            int page = Integer.parseInt(name.replace("<", "").replace(">", "").trim());

            int index = (page - 1) * VIEW_SIZE + slot;
            adminGroupListGUI.selectItem = GroupReader.getInstance().get(index);
            drawPage(inventory, page - 1, player);
            return;
        } catch (Exception e) {
        }
        adminGroupListGUI.selectItem = null;
    }

    /**
     * 是否应该翻页
     *
     * @param slot
     * @return
     */
    public static boolean shouldTurnPage(int slot) {
        return slot >= VIEW_SIZE;
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
        LAST(45, "上一页") {
            @Override
            public void deal(Inventory inventory, Player player) {
                turnPage(inventory, LAST.slot, player);
            }
        },
        NEXT(47, "下一页") {
            @Override
            public void deal(Inventory inventory, Player player) {
                turnPage(inventory, NEXT.slot, player);
            }
        },
        PAGE(46, "页码") {
            @Override
            public void deal(Inventory inventory, Player player) {

            }
        },
        VIEW(49, "查看") {
            @Override
            public void deal(Inventory inventory, Player player) {
                Group select = MENU_OPENING.get(player.getUniqueId()).selectItem;
                if(Objects.isNull(select)){
                    return;
                }
                player.closeInventory();
                AdminGroupListGUI.unRegister(player);
                new AdminGroupUserListGUI(player, select);
            }
        },
        CREATE(50, "创建") {
            @Override
            public void deal(Inventory inventory, Player player) {
                player.closeInventory();
                new AdminGroupCreateGUI().openAnvilGUI(player);
            }
        },
        DELETE(53, "删除") {
            @Override
            public void deal(Inventory inventory, Player player) {
                if (Objects.isNull(MENU_OPENING.get(player.getUniqueId()).selectItem)) {
                    player.sendMessage(LangReader.get("请选择组"));
                    return;
                }
                player.closeInventory();
                unRegister(player);
                new AdminGroupDeleteGUI(player, MENU_OPENING.get(player.getUniqueId()).selectItem);
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
        public static AdminGroupListGUI.SignEnum findMatchedSign(int checkSlot) {
            for (AdminGroupListGUI.SignEnum sign : AdminGroupListGUI.SignEnum.values()) {
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
