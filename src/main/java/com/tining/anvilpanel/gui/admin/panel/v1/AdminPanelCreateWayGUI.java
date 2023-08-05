package com.tining.anvilpanel.gui.admin.panel.v1;

import com.tining.anvilpanel.common.BeanUtils;
import com.tining.anvilpanel.gui.admin.panel.AdminPanelCreateGUI;
import com.tining.anvilpanel.gui.v1.AbstractGUIV1;
import com.tining.anvilpanel.gui.v1.SignEnumInterfaceV1;
import com.tining.anvilpanel.gui.v1.TemplateGUIV1;
import com.tining.anvilpanel.model.Panel;
import com.tining.anvilpanel.model.enums.PanelCreateActionEnum;
import com.tining.anvilpanel.model.enums.PanelCreateTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

public class AdminPanelCreateWayGUI extends AbstractGUIV1 {

    Panel panel;

    PanelCreateActionEnum panelCreateActionEnum;

    public AdminPanelCreateWayGUI(Player player, Panel panel, PanelCreateActionEnum panelCreateActionEnum) {
        super(player, "选择创建方式");
        Panel newPanel = new Panel();
        BeanUtils.copyBean(panel, newPanel);
        this.panel = newPanel;
        this.panelCreateActionEnum = panelCreateActionEnum;
    }

    @Override
    protected void drawPage(Inventory inventory, int pageNum, Player player) {

    }


    @Override
    public List<SignEnumInterfaceV1> getEnumList(Player player) {
        return Arrays.asList(TemplateGUIV1.SignEnum.values());
    }

    @AllArgsConstructor
    public enum SignEnum implements SignEnumInterfaceV1{
        //---事件区---//
        NORMAL(21,"通常命令"){
            @Override
            public void deal(Inventory inventory, Player player) {
                quit(player,inventory);
                AdminPanelCreateWayGUI me = (AdminPanelCreateWayGUI)getMe(player);
                if(me.panelCreateActionEnum.equals(PanelCreateActionEnum.NEW)){
                    // 新普通
                    new AdminPanelCreateGUI(player,new Panel(),PanelCreateTypeEnum.NORMAL).openAnvilGUI();
                }else if(me.panelCreateActionEnum.equals(PanelCreateActionEnum.RESET)){
                    // 新长命令
                    new AdminPanelCreateGUI(player,me.panel,PanelCreateTypeEnum.NORMAL).setCommand();
                }
            }
        },
        LONG(21,"长命令"){
            @Override
            public void deal(Inventory inventory, Player player) {
                quit(player,inventory);
                AdminPanelCreateWayGUI me = (AdminPanelCreateWayGUI)getMe(player);
                if(me.panelCreateActionEnum.equals(PanelCreateActionEnum.NEW)){
                    // 重置普通命令
                    new AdminPanelCreateGUI(player,new Panel(),PanelCreateTypeEnum.LONG).openAnvilGUI();
                }else if(me.panelCreateActionEnum.equals(PanelCreateActionEnum.RESET)){
                    // 重置长命令
                    new AdminPanelCreateGUI(player,me.panel,PanelCreateTypeEnum.LONG).setCommand();
                }
            }
        }
        ;
        @Getter
        private int slot;
        @Getter
        private String label;
    }
}
