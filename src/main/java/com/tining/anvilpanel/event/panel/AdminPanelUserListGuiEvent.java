package com.tining.anvilpanel.event.panel;

import com.tining.anvilpanel.gui.admin.panel.AdminPanelUserListGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class AdminPanelUserListGuiEvent implements Listener {
    /**
     * 关闭时取消注册并结算
     *
     * @param e 消息句柄
     */
    @EventHandler
    public void close(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player) {
            Player player = (Player) e.getPlayer();
            AdminPanelUserListGUI.unRegister(player);
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
        AdminPanelUserListGUI.unRegister(player);
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
            if (AdminPanelUserListGUI.shouldEffective(player, e.getView().getTitle())) {
                e.setCancelled(true);
                AdminPanelUserListGUI.SignEnum signEnum = AdminPanelUserListGUI.SignEnum.findMatchedSign(e.getSlot());
                if (Objects.nonNull(signEnum)) {
                    signEnum.deal(e.getClickedInventory(), player);
                }else if(e.getSlot() < AdminPanelUserListGUI.VIEW_SIZE){
                    AdminPanelUserListGUI.setSelectItem(e.getClickedInventory(), e.getSlot(), player);
                }
            }
        }
    }
}
