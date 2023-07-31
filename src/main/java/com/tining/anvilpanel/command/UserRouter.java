package com.tining.anvilpanel.command;

import com.tining.anvilpanel.command.dispatcher.UserUseCommander;

public enum UserRouter {
    /**
     * 开启
     */
    USE {
        @Override
        public void deal(CommandPack commandPack) {
            // your implementation for CREATE command
            new UserUseCommander().deal(commandPack);
        }
    },
    ;

    public abstract void deal(CommandPack commandPack);
}
