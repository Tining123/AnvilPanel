package com.tining.anvilpanel.command;

import com.tining.anvilpanel.command.dispatcher.AdminCreateCommander;
import com.tining.anvilpanel.command.dispatcher.AdminGroupCommander;
import com.tining.anvilpanel.command.dispatcher.AdminListCommander;
import com.tining.anvilpanel.command.dispatcher.AdminReloadCommander;
import com.tining.anvilpanel.gui.V1.TemplateListGUIV1;
import org.bukkit.entity.Player;

/**
 * 管理员指令集集合
 * @author tinga
 */

public enum AdminRouter {
    /**
     * 创建
     */
    CREATE {
        @Override
        public void deal(CommandPack commandPack) {
            new AdminCreateCommander().deal(commandPack);
        }
    },
    /**
     * 展示
     */
    LIST {
        @Override
        public void deal(CommandPack commandPack) {
            new AdminListCommander().deal(commandPack);
        }
    },
    /**
     * 组管理
     */
    GROUP{
        @Override
        public void deal(CommandPack commandPack) {
            new AdminGroupCommander().deal(commandPack);
        }
    },
    RELOAD{
        @Override
        public void deal(CommandPack commandPack) {
            new AdminReloadCommander().deal(commandPack);
        }
    },
    TEST{
        @Override
        public void deal(CommandPack commandPack) {
            new TemplateListGUIV1((Player) commandPack.getSender());
            return;
        }
    }

    ;

    public abstract void deal(CommandPack commandPack);
}
