package com.tining.anvilpanel.gui.admin.panel;

import com.tining.anvilpanel.AnvilPanel;
import com.tining.anvilpanel.model.Panel;
import com.tining.anvilpanel.event.storage.LangReader;
import com.tining.anvilpanel.event.storage.PanelReader;
import net.wesjd.anvilgui.AnvilGUI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class AdminPanelAddUserAnvilGUI {
    /**
     * 开始创建菜单并且设置标题
     *
     * @param player
     */
    public void openGUI(Player player, Panel panel) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    String text = stateSnapshot.getText();
                    if (StringUtils.isBlank(text)) {
                        return Collections.singletonList(
                                AnvilGUI.ResponseAction.run(() ->
                                        player.sendMessage(LangReader.get("用户名非法"))));
                    }

                    if (Objects.isNull(panel.getUsers())) {
                        panel.setUsers(new ArrayList<>());
                    }
                    if (panel.getUsers().contains(text)) {
                        return Collections.singletonList(
                                AnvilGUI.ResponseAction.run(() ->
                                        player.sendMessage(LangReader.get("用户已存在"))));
                    }
                    // 记录名称并且打开下一个
                    return Arrays.asList(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(() -> {
                                        // 记录名称
                                        panel.getUsers().add(text);
                                        panel.setUsers(panel.getUsers().stream().distinct().collect(Collectors.toList()));
                                        PanelReader.getInstance().addToList(panel);
                                        player.sendMessage(LangReader.get("添加成功"));
                                    }
                            ));
                })
                .title(LangReader.get("添加用户"))
                .text(LangReader.get("请输入名称"))
                .plugin(AnvilPanel.getInstance())
                .open(player);
    }
}
