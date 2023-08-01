package com.tining.anvilpanel.command.dispatcher;

import com.tining.anvilpanel.event.storage.ConfigReader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AdminReloadCommander extends AbstractCommander{
    @Override
    protected boolean solve(CommandSender sender, Command command, String label, String[] args) {
        ConfigReader.reloadConfig();
        return true;
    }
}
