package com.tining.anvilpanel.command.dispatcher;

import com.tining.anvilpanel.gui.AdminGroupListGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminGroupCommander extends AbstractCommander{
    @Override
    protected boolean solve(CommandSender sender, Command command, String label, String[] args) {
        new AdminGroupListGUI((Player) sender);
        return true;
    }
}
