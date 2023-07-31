package com.tining.anvilpanel.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdminCommand extends BaseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 1) {
            return false;
        }
        // 构造参数包
        CommandPack commandPack = new CommandPack();
        commandPack.sender = sender;
        commandPack.command = command;
        commandPack.label = label;
        commandPack.args = args;

        try {
            AdminRouter.valueOf(StringUtils.upperCase(args[0])).deal(commandPack);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid command: " + command);
        }
        return true;
    }


}
