package com.tining.anvilpanel.command.dispatcher;

import com.tining.anvilpanel.gui.admin.panel.AdminPanelCreateGUI;
import com.tining.anvilpanel.gui.admin.panel.v1.AdminPanelCreateWayGUI;
import com.tining.anvilpanel.model.Panel;
import com.tining.anvilpanel.model.enums.PanelCreateActionEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCreateCommander extends AbstractCommander {
    @Override
    protected boolean solve(CommandSender sender, Command command, String label, String[] args) {
        new AdminPanelCreateWayGUI((Player) sender, new Panel(), PanelCreateActionEnum.NEW);
        return true;
    }
}
