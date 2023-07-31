package com.tining.anvilpanel.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/**
 * 一个标准的面板实体
 */
public abstract class AbstractGUI {

    /**
     * 构造GUI
     * @param player
     * @param type
     * @param name
     */
    public AbstractGUI(Player player, InventoryType type, String name){
        this.player = player;
    }

    /**
     * 所在的面板的实体
     */
    public Inventory inventory;

    /**
     * 持有者
     */
    Player player;
}
