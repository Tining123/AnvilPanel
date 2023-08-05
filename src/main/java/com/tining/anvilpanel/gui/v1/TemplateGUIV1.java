package com.tining.anvilpanel.gui.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

public class TemplateGUIV1 extends AbstractGUIV1{
    public TemplateGUIV1(Player player, String guiName) {
        super(player, guiName);
    }

    @Override
    protected void drawPage(Inventory inventory, int pageNum, Player player) {

    }

    /**
     * 获取内部枚举列表
     *
     * @param player
     * @return
     */
    @Override
    public List<SignEnumInterfaceV1> getEnumList(Player player) {
        return Arrays.asList(SignEnum.values());
    }

    @AllArgsConstructor
    public enum SignEnum implements SignEnumInterfaceV1{
        //---事件区---//
        ADD(-1,"name"){
            @Override
            public void deal(Inventory inventory, Player player) {
                player.closeInventory();
                unRegister(player);
            }
        },
        ;
        @Getter
        private int slot;
        @Getter
        private String label;
    }
}
