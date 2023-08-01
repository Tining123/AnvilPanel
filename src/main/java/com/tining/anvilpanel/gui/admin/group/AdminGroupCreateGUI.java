package com.tining.anvilpanel.gui.admin.group;

import com.tining.anvilpanel.AnvilPanel;
import com.tining.anvilpanel.event.storage.GroupReader;
import com.tining.anvilpanel.event.storage.IListReader;
import com.tining.anvilpanel.model.Group;
import com.tining.anvilpanel.event.storage.LangReader;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

/**
 * 创建一个新的组
 *
 * @author tinga
 */
public class AdminGroupCreateGUI {
    /**
     * 开始创建菜单并且设置标题
     *
     * @param player
     */
    public void openAnvilGUI(Player player) {
        Group group = new Group();
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    if (Objects.nonNull(GroupReader.getInstance().get(stateSnapshot.getText()))) {
                        return Arrays.asList(
                                AnvilGUI.ResponseAction.run(() ->
                                        player.sendMessage(LangReader.get("组已存在!"))));
                    }

                    // 记录名称
                    group.setName(stateSnapshot.getText());
                    return Arrays.asList(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(() -> {
                                GroupReader.getInstance().addToList(group);
                                player.sendMessage(LangReader.get("创建组成功"));
                            }));
                })
                .title(LangReader.get("创建组"))
                .text(LangReader.get("请输入名称"))
                .plugin(AnvilPanel.getInstance())
                .open(player);
    }
}
