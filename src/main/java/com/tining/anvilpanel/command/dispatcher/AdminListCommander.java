package com.tining.anvilpanel.command.dispatcher;

import com.tining.anvilpanel.gui.admin.panel.AdminPanelListGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class AdminListCommander extends AbstractCommander{
    @Override
    protected boolean solve(CommandSender sender, Command command, String label, String[] args) {
        new AdminPanelListGUI((Player) sender, InventoryType.CHEST, "");
        return true;
    }
}
