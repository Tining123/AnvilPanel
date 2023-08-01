package com.tining.anvilpanel.gui.V1;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.tining.anvilpanel.common.PluginUtil;
import com.tining.anvilpanel.event.storage.LangReader;
import com.tining.anvilpanel.gui.admin.group.AdminGroupListGUI;
import com.tining.anvilpanel.model.enums.SignMaterialEnum;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.logging.Logger;

/**
 * V1版本列表GUI抽象类
 * @author tinga
 */
public abstract class AbstractListGUIV1<T extends DataV1> extends AbstractGUIV1{

    protected Integer lastIndex = 45;

    protected Integer pageIndex = 46;

    protected Integer nextIndex = 47;

    List<T> dataList;

    T selectItem;

    public AbstractListGUIV1(Player player,String guiName,List<T> dataList) {
        super(player,guiName);
        this.guiName = guiName;
        this.dataList = dataList;
        this.player = player;
        this.inventory = Bukkit.createInventory(player, pageSize, LangReader.get(guiName));
        map.put(player.getUniqueId(), getClazzType(),this);
        player.openInventory(inventory);
        drawPage(inventory, 0, player);
    }

    /**
     * 是否应该翻页
     *
     * @param slot
     * @return
     */
    public static boolean shouldTurnPage(int slot, Player player) {
        AbstractListGUIV1 gui = (AbstractListGUIV1) AbstractListGUIV1.getMe(player);
        return slot >= gui.viewSize;
    }


    /**
     * 获取选中的内容
     *
     * @param inventory
     * @param slot
     * @return
     */
    public void setSelectItem(Inventory inventory, int slot, Player player) {
        AbstractListGUIV1 me = (AbstractListGUIV1) map.get(player.getUniqueId(),getClazzType());
        if (slot >= me.getViewSize()) {
            me.selectItem = null;
        }

        try {
            me.decideSelectItem(inventory,slot,player);
        } catch (Exception e) {
        }
        me.selectItem = null;
    }

    @Override
    protected void drawPage(Inventory inventory, int pageNum, Player player){
        // 前画
        beforeDraw(inventory,pageNum,player);

        AbstractListGUIV1 me = (AbstractListGUIV1) AbstractListGUIV1.getMe(player);
        int viewSize = me.viewSize;
        List<T> datalist = me.dataList;
        int move = 0;
        boolean set = false;

        List<ItemStack> list = new ArrayList<>();
        for (T data : datalist) {
            Material signMaterial = SignMaterialEnum.CONFIG.getMaterial();
            ItemStack sign = new ItemStack(signMaterial);
            PluginUtil.setName(sign, data.getName());
            list.add(sign);
        }

        for (int i = pageNum * viewSize; i < list.size() && i < (pageNum + 1) * viewSize; i++) {
            if (!Objects.isNull(list.get(i))) {
                if (!set) {
                    inventory.clear();
                    set = true;
                }
                inventory.setItem(move % viewSize, list.get(i));
                move++;
            }
        }

        //设置翻页图标
        ItemStack left = new ItemStack(SignMaterialEnum.LEFT.getMaterial(), 1);
        ItemStack right = new ItemStack(SignMaterialEnum.RIGHT.getMaterial(), 1);
        ItemStack mid = new ItemStack(SignMaterialEnum.PAGE.getMaterial(), 1);

        T selectItem = (T) me.selectItem;
        if (Objects.nonNull(selectItem)) {
            mid = new ItemStack(SignMaterialEnum.GROUP.getMaterial(), 1);
        }

        ItemMeta leftItemMeta = left.getItemMeta();
        ItemMeta rightItemMeta = right.getItemMeta();
        ItemMeta midItemMeta = right.getItemMeta();

        if (!Objects.isNull(leftItemMeta) && pageNum != 0 && pageNum != 1) {
            leftItemMeta.setDisplayName(LangReader.get("上一页"));
            left.setItemMeta(leftItemMeta);
            inventory.setItem(me.lastIndex, left);
        }
        if (!Objects.isNull(rightItemMeta) && move != 0 && (pageNum + 1) * me.viewSize < me.dataList.size()) {
            rightItemMeta.setDisplayName(LangReader.get("下一页"));
            right.setItemMeta(rightItemMeta);
            inventory.setItem(me.nextIndex, right);
        }
        if (!Objects.isNull(midItemMeta)) {
            midItemMeta.setDisplayName("< " + (pageNum + 1) + " >");
            mid.setItemMeta(midItemMeta);
            if (Objects.nonNull(selectItem)) {
                PluginUtil.addLore(mid, Collections.singletonList(selectItem.getName()));
            }

            inventory.setItem(me.pageIndex, mid);
        }

        // 后画
        afterDraw(inventory,pageNum,player);
    }

    /**
     * 绘制第N页的列表，子类翻页要用
     */
    protected void turnPage(Inventory inventory, int slot, Player player) {
        AbstractListGUIV1 me = (AbstractListGUIV1) AbstractListGUIV1.getMe(player);
        try {
            ItemStack itemStack = inventory.getItem(me.pageIndex);
            String name = itemStack.getItemMeta().getDisplayName();
            int page = Integer.parseInt(name.replace("<", "").replace(">", "").trim());

            if (Objects.equals(slot, me.lastIndex)) {
                if (page < 2) {
                    return;
                }
                drawPage(inventory, page - 2, player);
                return;
            }

            if (Objects.equals(slot, me.nextIndex)) {
                drawPage(inventory, page, player);
                return;
            }
        } catch (Exception ignre) {
        }
    }

    /**
     * 决定选中的你内容
     * @param inventory
     * @param slot
     * @param player
     */
    public abstract void decideSelectItem(Inventory inventory, int slot, Player player);

    protected abstract void beforeDraw(Inventory inventory, int pageNum, Player player);

    protected abstract void afterDraw(Inventory inventory, int pageNum, Player player);

}
