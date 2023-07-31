package com.tining.anvilpanel.command;


import com.tining.anvilpanel.command.dispatcher.UserUseCommander;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

//import javax.annotation.ParametersAreNonnullByDefault;

public class UserCommand extends BaseCommand implements CommandExecutor {

    Logger logger = Logger.getLogger("command");


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /**
         * 获取价格
         */
        //Map<String, Double> worth = ConfigReader.getWorth();

        if (!(sender instanceof Player)) {
            return false;
        }

        // 构造参数包
        CommandPack commandPack = new CommandPack();
        commandPack.sender = sender;
        commandPack.command = command;
        commandPack.label = label;
        commandPack.args = args;

        if(args.length < 1){
            return false;
        }

        try {
            UserRouter.valueOf(StringUtils.upperCase(args[0])).deal(commandPack);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid command: " + command);
        }

        return new UserUseCommander().deal(commandPack);
    }

}