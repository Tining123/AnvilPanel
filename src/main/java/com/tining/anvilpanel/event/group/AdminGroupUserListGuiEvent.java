package com.tining.anvilpanel.event.group;

import com.tining.anvilpanel.gui.admin.group.AdminGroupUserListGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class AdminGroupUserListGuiEvent implements Listener {
    /**
     * 关闭时取消注册并结算
     *
     * @param e 消息句柄
     */
    @EventHandler
    public void close(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player) {
            Player player = (Player) e.getPlayer();
            AdminGroupUserListGUI.unRegister(player);
        }
    }


    /**
     * 关闭时取消注册并结算
     *
     * @param e 消息句柄
     */
    @EventHandler
    public void close(PlayerQuitEvent e) {
        Player player = (Player) e.getPlayer();
        AdminGroupUserListGUI.unRegister(player);
    }

    /**
     * 防止物品被挪动
     *
     * @param e
     */
    @EventHandler(priority = EventPriority.LOW)
    public void disableMove(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null) {
            Player player = (Player) e.getWhoClicked();
            // 是否应该响应
            if (AdminGroupUserListGUI.shouldEffective(player, e.getView().getTitle())) {
                e.setCancelled(true);
                AdminGroupUserListGUI.SignEnum signEnum = AdminGroupUserListGUI.SignEnum.findMatchedSign(e.getSlot());
                if (Objects.nonNull(signEnum)) {
                    signEnum.deal(e.getClickedInventory(), player);
                }else if(e.getSlot() < AdminGroupUserListGUI.VIEW_SIZE){
                    AdminGroupUserListGUI.setSelectItem(e.getClickedInventory(), e.getSlot(), player);
                }
            }
        }
    }
}
