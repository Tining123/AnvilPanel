package com.tining.anvilpanel.gui.admin.panel;

import com.tining.anvilpanel.AnvilPanel;
import com.tining.anvilpanel.common.MyStringUtil;
import com.tining.anvilpanel.gui.AbstractGUI;
import com.tining.anvilpanel.model.Panel;
import com.tining.anvilpanel.model.enums.PlaceholderEnum;
import com.tining.anvilpanel.event.storage.LangReader;
import com.tining.anvilpanel.event.storage.PanelReader;
import net.wesjd.anvilgui.AnvilGUI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.*;

public class AdminPanelCreateGUI extends AbstractGUI {
    /**
     * 构造GUI
     *
     * @param player
     */
    public AdminPanelCreateGUI(Player player) {
        super(player, InventoryType.ANVIL, "");
    }

    /**
     * 开始创建菜单并且设置标题
     *
     * @param player
     */
    public void openAnvilGUI(Player player) {
        Panel panel = new Panel();
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    panel.setName(stateSnapshot.getText());

                    if(Objects.nonNull(PanelReader.getInstance().get(panel.getName()))){
                        return Arrays.asList(
                                AnvilGUI.ResponseAction.close(),
                                AnvilGUI.ResponseAction.run(() ->
                                        player.sendMessage(LangReader.get("命令已存在!"))));
                    }

                    // 记录名称并且打开下一个
                    return Arrays.asList(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(() ->
                                    setCommand(player, panel)));
                })
                .title(LangReader.get("创建命令"))
                .text(LangReader.get("请输入名称"))
                .plugin(AnvilPanel.getInstance())
                .open(player);
    }

    /**
     * 设置完标题开始设置
     *
     * @param player
     */
    public void setCommand(Player player, Panel panel) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    // 记录名称并且打开下一个
                    List<AnvilGUI.ResponseAction> res = new ArrayList<>();
                    res.add(AnvilGUI.ResponseAction.close());
                    if (stateSnapshot.getText().contains(PlaceholderEnum.PARAM_TEXT.getText())) {
                        panel.setCommand(stateSnapshot.getText());
                        res.add(AnvilGUI.ResponseAction.run(() ->
                                setTips(player, new ArrayList<>(), panel)));
                    } else {
                        res.add(AnvilGUI.ResponseAction.run(() ->
                                end(player, panel)));
                    }
                    return res;
                })
                .title(LangReader.get("创建命令"))
                .text(LangReader.get("请输入命令内容"))
                .plugin(AnvilPanel.getInstance())
                .open(player);
    }

    /**
     * 设置完标题开始设置
     *
     * @param player
     */
    private void setTips(Player player, List<String> tips, Panel panel) {
        String title = MyStringUtil.getWordsAroundT(panel.getCommand(),
                tips.size() + 1);

        new AnvilGUI.Builder()
                .onClick((slot, StateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    tips.add(StateSnapshot.getText());
                    List<AnvilGUI.ResponseAction> res = new ArrayList<>();
                    res.add(AnvilGUI.ResponseAction.close());
                    if (tips.size() < StringUtils.countMatches(panel.getCommand(), PlaceholderEnum.PARAM_TEXT.getText())) {
                        res.add(AnvilGUI.ResponseAction.run(() ->
                                setTips(player, tips, panel)));
                    } else {
                        res.add(AnvilGUI.ResponseAction.run(() ->{
                                panel.setSubtitle(tips);
                                end(player, panel);
                        }));
                    }
                    // 记录名称并且打开下一个
                    return res;

                })
                .title(title)
                .text(LangReader.get("请为参数命名"))
                .plugin(AnvilPanel.getInstance())
                .open(player);
    }

    private void end(Player player, Panel panel) {
        // panel默认权限节点控制
        panel.setOath(true);
        PanelReader.getInstance().addToList(panel);
        player.sendMessage(LangReader.get("创建命令成功"));
    }
}
