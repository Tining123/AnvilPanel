package com.tining.anvilpanel.gui;

import com.tining.anvilpanel.AnvilPanel;
import com.tining.anvilpanel.common.MyStringUtil;
import com.tining.anvilpanel.model.Group;
import com.tining.anvilpanel.model.Panel;
import com.tining.anvilpanel.model.enums.PlaceholderEnum;
import com.tining.anvilpanel.storage.GroupReader;
import com.tining.anvilpanel.storage.LangReader;
import com.tining.anvilpanel.storage.PanelReader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import net.wesjd.anvilgui.AnvilGUI;

import java.util.*;

/**
 * 一个最典型的铁砧面板
 *
 * @author tinga
 */
public class UserUseGUI extends AbstractGUI {


    public UserUseGUI(Player player, Panel panel) {
        super(player, InventoryType.ANVIL, "Example Anvil");
    }

    /**
     * 获取面板
     *
     * @param player
     * @return
     */
    public static boolean getGui(Player player, String panelName) {

        // 获取panel
        Panel panel = PanelReader.getPanel(panelName);
        if (Objects.isNull(panel)) {
            player.sendMessage(LangReader.get("命令不存在"));
            return false;
        }

        boolean ava = false;
        ava = avaFree(player, panel) || avaOath(player, panel) || avaGroup(player, panel) || avaUser(player, panel);
        if(!ava){
            player.sendMessage(LangReader.get("权限不足:") + "anvilpanel.use." + panel.getName());
            return true;
        }

        if (!panel.getCommand().contains(PlaceholderEnum.PARAM_TEXT.getText())) {
            executeCommand(player, panel, new ArrayList<>());
        }

        forgeCommand(player, panel, new ArrayList<>());

        return true;
    }

    /**
     * 构建语句
     *
     * @param player
     * @param panel
     * @param param
     */
    public static void forgeCommand(Player player, Panel panel, List<String> param) {
        String title = MyStringUtil.getWordsAroundT(panel.getCommand(),
                param.size() + 1);

        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    param.add(stateSnapshot.getText());
                    List<AnvilGUI.ResponseAction> res = new ArrayList<>();
                    res.add(AnvilGUI.ResponseAction.close());
                    if (param.size() < StringUtils.countMatches(panel.getCommand(), PlaceholderEnum.PARAM_TEXT.getText())) {
                        res.add(AnvilGUI.ResponseAction.run(() ->
                                forgeCommand(player, panel, param)));
                    } else {
                        res.add(AnvilGUI.ResponseAction.run(() -> {
                            executeCommand(player, panel, param);
                        }));
                    }
                    // 记录名称并且打开下一个
                    return res;

                })
                .title(title)
                .text(panel.getSubtitle().get(param.size()))
                .plugin(AnvilPanel.getInstance())
                .open(player);
    }

    /**
     * 执行命令
     *
     * @param player
     */
    private static void executeCommand(Player player, Panel panel, List<String> param) {

        String command = panel.getCommand();

        command = command.replace(PlaceholderEnum.PLAYER_NAME.getText(), player.getName());

        String paramRegex = PlaceholderEnum.PARAM_TEXT.getText().replace("[", "\\[")
                .replace("]", "\\]");


        for (String number : param) {
            command = command.replaceFirst(paramRegex, number);
        }

        List<String> commandList = Arrays.asList(command.split(PlaceholderEnum.SPLITER.getText()));

        for (String subCommand : commandList) {
            if (StringUtils.isBlank(subCommand)) {
                continue;
            }
            if (subCommand.startsWith(PlaceholderEnum.OP_COMMAND.getText())) {
                CommandSender console = Bukkit.getConsoleSender();
                subCommand = subCommand.replaceFirst(PlaceholderEnum.OP_COMMAND.getText(), "");
                Bukkit.dispatchCommand(console, subCommand);
            } else {
                Bukkit.dispatchCommand(player, subCommand);
            }

        }

    }

    /**
     * 是否可以自由执行
     *
     * @param player
     * @param panel
     * @return
     */
    private static boolean avaFree(Player player, Panel panel) {
        return panel.isFree();
    }

    /**
     * 权限节点
     *
     * @param player
     * @param panel
     * @return
     */
    private static boolean avaOath(Player player, Panel panel) {
        // 如果没开默认锁死权限节点
        if (!panel.isOath()) {
            return false;
        }
        // 如果开了则检查
        return panel.isOath() && player.hasPermission("anvilpanel.use." + panel.getName());
    }

    /**
     * 权限组
     *
     * @param player
     * @param panel
     * @return
     */
    private static boolean avaGroup(Player player, Panel panel) {
        for (String grouName : panel.getGroup()) {
            Group group = GroupReader.get(grouName);
            if (Objects.isNull(group)) {
                continue;
            }

            if (CollectionUtils.isNotEmpty(group.getUsers())) {
                if (group.getUsers().contains(player.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 用户组
     *
     * @param player
     * @param panel
     * @return
     */
    private static boolean avaUser(Player player, Panel panel) {
        if (CollectionUtils.isEmpty(panel.getUsers())) {
            return false;
        }
        return panel.getUsers().contains(player.getName());
    }
}