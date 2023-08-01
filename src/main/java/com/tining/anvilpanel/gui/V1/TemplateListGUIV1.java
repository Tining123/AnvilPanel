package com.tining.anvilpanel.gui.V1;

import com.tining.anvilpanel.event.storage.GroupReader;
import com.tining.anvilpanel.model.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class TemplateListGUIV1 extends AbstractListGUIV1<Group> {

    public TemplateListGUIV1(Player player) {
        super(player,"测试界面",GroupReader.getInstance().getForList());
    }

    @Override
    protected void drawPage(Inventory inventory, int pageNum, Player player) {

    }

    /**
     * 决定选中的你内容
     *
     * @param inventory
     * @param slot
     * @param player
     */
    @Override
    public void decideSelectItem(Inventory inventory, int slot, Player player) {

    }

    @Override
    protected void beforeDraw(Inventory inventory, int pageNum, Player player) {

    }

    @Override
    protected void afterDraw(Inventory inventory, int pageNum, Player player) {

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
    public static enum SignEnum implements SignEnumInterfaceV1{
        //---事件区---//
        ADD(-1,"name"){
            @Override
            public void deal(Inventory inventory, Player player) {
                AbstractListGUIV1 me = (AbstractListGUIV1) AbstractGUIV1.getMe(player);
                me.turnPage(inventory,0,player);
            }
        },
        ;
        @Getter
        private int slot;
        @Getter
        private String label;
    }
}
