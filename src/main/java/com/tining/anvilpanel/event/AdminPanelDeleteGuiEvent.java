package com.tining.anvilpanel.event;

import com.tining.anvilpanel.gui.AdminPanelDeleteGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class AdminPanelDeleteGuiEvent implements Listener {
    /**
     * 关闭时取消注册并结算
     *
     * @param e 消息句柄
     */
    @EventHandler
    public void close(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player) {
            Player player = (Player) e.getPlayer();
            AdminPanelDeleteGUI.unRegister(player);
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
        AdminPanelDeleteGUI.unRegister(player);
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
            if (AdminPanelDeleteGUI.shouldEffective(player, e.getView().getTitle())) {
                e.setCancelled(true);
                AdminPanelDeleteGUI.SignEnum signEnum = AdminPanelDeleteGUI.SignEnum.findMatchedSign(e.getSlot());
                if (Objects.nonNull(signEnum)) {
                    signEnum.deal(e.getClickedInventory(), player);
                }
            }
        }
    }
}
