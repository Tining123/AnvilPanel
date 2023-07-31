package com.tining.anvilpanel.command.dispatcher;

import com.tining.anvilpanel.gui.AdminPanelCreateGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCreateCommander extends AbstractCommander {
    @Override
    protected boolean solve(CommandSender sender, Command command, String label, String[] args) {
        new AdminPanelCreateGUI((Player) sender).openAnvilGUI((Player) sender);
        return true;
    }
}
