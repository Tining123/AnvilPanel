package com.tining.anvilpanel.command.dispatcher;

import com.tining.anvilpanel.gui.UserUseGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserUseCommander extends AbstractCommander {

    @Override
    protected boolean solve(CommandSender sender, Command command, String label, String[] args){
        if (args.length < 2) {
            return false;
        }

        UserUseGUI.getGui((Player) sender, args[1]);
        return true;
    }
}
