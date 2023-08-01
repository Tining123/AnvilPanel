package com.tining.anvilpanel.gui.admin.panel;

import com.tining.anvilpanel.common.PluginUtil;
import com.tining.anvilpanel.model.Panel;
import com.tining.anvilpanel.event.storage.LangReader;
import com.tining.anvilpanel.event.storage.PanelReader;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class AdminPanelListGUI {
    private static final Logger log = Logger.getLogger("Minecraft");
    /**
     * 当前开启的菜单
     */
    private static final Map<UUID, AdminPanelListGUI> MENU_OPENING = new ConcurrentHashMap();

    /**
     * 单页大小
     */
    private static final Integer PAGE_SIZE = 54;

    /**
     * 可视区域大小
     */
    public static final Integer VIEW_SIZE = 45;

    /**
     * 向右翻页占位坐标
     */
    private static final Integer LEFT_ARROW_INDEX = 45;

    /**
     * 向左翻页占位坐标
     */
    private static final Integer RIGHT_ARROW_INDEX = 53;

    /**
     * 页码占位坐标
     */
    private static final Integer PAGE_SIGN_INDEX = 49;

    /**
     * 占位符图标
     */
    private static final Material PAGE_ARROW = Material.PAPER;

    /**
     * 页码图标
     */
    private static final Material PAGE_SIGN = Material.BOOK;

    /**
     * 默认展示图标
     */
    private static final Material DEFAULT_SIGN = Material.PAPER;

    /**
     * 收购列表名称
     */
    public static final String GUI_NAME = "管理员命令列表";

    /**
     * 自身箱子界面
     */
    Inventory inventory;

    /**
     * 构造GUI
     *
     * @param player
     * @param type
     * @param name
     */
    public AdminPanelListGUI(Player player, InventoryType type, String name) {
        this.inventory = Bukkit.createInventory(player, PAGE_SIZE, LangReader.get(GUI_NAME));
        drawPage(this.inventory, 0, player);
        MENU_OPENING.put(player.getUniqueId(), this);
        player.openInventory(inventory);
    }

    private static void drawPage(Inventory inventory, int pageNum, Player player) {
        List<Panel> panelList = PanelReader.getInstance().getForList();
        int move = 0;
        boolean set = false;

        List<ItemStack> list = new ArrayList<>();
        for (Panel panel : panelList) {
            Material signMaterial = null;
            try {
                signMaterial = Material.getMaterial(panel.getItemSign());
            } catch (Exception ignore) {
            }
            if (Objects.isNull(signMaterial)) {
                signMaterial = DEFAULT_SIGN;
            }
            ItemStack sign = new ItemStack(signMaterial);
            List<String> lores = new ArrayList<>();
            lores.add(LangReader.get("命令:") + panel.getCommand());
            PluginUtil.addLore(sign, lores);
            PluginUtil.setName(sign, LangReader.get("名称:") + panel.getName());
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
        ItemStack left = new ItemStack(PAGE_ARROW, 1);
        ItemStack right = new ItemStack(PAGE_ARROW, 1);
        ItemStack mid = new ItemStack(PAGE_SIGN, 1);
        ItemMeta leftItemMeta = left.getItemMeta();
        ItemMeta rightItemMeta = right.getItemMeta();
        ItemMeta midItemMeta = right.getItemMeta();

        if (!Objects.isNull(leftItemMeta) && pageNum != 0 && pageNum != 1) {
            leftItemMeta.setDisplayName(LangReader.get("上一页"));
            left.setItemMeta(leftItemMeta);
            inventory.setItem(LEFT_ARROW_INDEX, left);
        }
        if (!Objects.isNull(rightItemMeta) && move != 0 && (pageNum + 1) * VIEW_SIZE < list.size()) {
            rightItemMeta.setDisplayName(LangReader.get("下一页"));
            right.setItemMeta(rightItemMeta);
            inventory.setItem(RIGHT_ARROW_INDEX, right);
        }
        if (!Objects.isNull(midItemMeta) && move != 0) {
            midItemMeta.setDisplayName("< " + (pageNum + 1) + " >");
            mid.setItemMeta(midItemMeta);
            inventory.setItem(PAGE_SIGN_INDEX, mid);
        }
    }

    /**
     * 绘制第N页的列表
     */
    public static void turnPage(Inventory inventory, int slot, Player player) {
        try {
            ItemStack itemStack = inventory.getItem(PAGE_SIGN_INDEX);
            String name = itemStack.getItemMeta().getDisplayName();
            int page = Integer.parseInt(name.replace("<", "").replace(">", "").trim());

            if (Objects.equals(slot, LEFT_ARROW_INDEX)) {
                if (page < 2) {
                    return;
                }
                drawPage(inventory, page - 2, player);
                return;
            }

            if (Objects.equals(slot, RIGHT_ARROW_INDEX)) {
                drawPage(inventory, page, player);
                return;
            }
        } catch (Exception ignre) {
        }
    }


    /**
     * 获取选中的内容
     * @param inventory
     * @param slot
     * @return
     */
    public static Panel getSelectItem(Inventory inventory, int slot){
        if(slot >= VIEW_SIZE ){
            return null;
        }

        try {
            ItemStack itemStack = inventory.getItem(PAGE_SIGN_INDEX);
            String name = itemStack.getItemMeta().getDisplayName();
            int page = Integer.parseInt(name.replace("<", "").replace(">", "").trim());

            int index = (page - 1) * VIEW_SIZE + slot;
            return PanelReader.getInstance().get(index);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 是否应该翻页
     * @param slot
     * @return
     */
    public static boolean shouldTurnPage(int slot){
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

}
