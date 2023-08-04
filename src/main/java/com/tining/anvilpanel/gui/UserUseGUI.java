package com.tining.anvilpanel.gui;

import com.tining.anvilpanel.AnvilPanel;
import com.tining.anvilpanel.common.MyStringUtil;
import com.tining.anvilpanel.storage.GroupReader;
import com.tining.anvilpanel.model.Group;
import com.tining.anvilpanel.model.Panel;
import com.tining.anvilpanel.model.enums.PlaceholderEnum;
import com.tining.anvilpanel.storage.LangReader;
import com.tining.anvilpanel.storage.PanelReader;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.wesjd.anvilgui.AnvilGUI;

import java.util.*;

/**
 * 一个最典型的铁砧面板
 *
 * @author tinga
 */
public class UserUseGUI{

    /**
     * 懒人变量完成标志
     */
    Boolean tDone;

    /**
     * 复用变量完成标志
     */
    Boolean vDone;

    /**
     * 基本信息
     */
    String panelName;
    Player player;
    Panel panel;

    /**
     * 参数收集
     */
    ParamCollector paramCollector;

    /**
     * 获取面板
     *
     * @param player
     * @return
     */
    public boolean getGui(Player player, String panelName) {
        this.player = player;
        this.panelName = panelName;
        this.vDone =false;
        this.tDone = false;
        // 获取panel
        Panel panel = PanelReader.getInstance().get(panelName);
        if (Objects.isNull(panel)) {
            player.sendMessage(LangReader.get("命令不存在"));
            return false;
        }

        this.panel = panel;
        paramCollector = new ParamCollector();

        boolean ava = false;
        ava = avaFree(player, panel) || avaOath(player, panel) || avaGroup(player, panel) || avaUser(player, panel);
        if(!ava){
            player.sendMessage(LangReader.get("权限不足:") + "anvilpanel.use." + panel.getName());
            return true;
        }

//        if (!panel.getCommand().contains(PlaceholderEnum.PARAM_TEXT.getText())
//        && CollectionUtils.isEmpty(MyStringUtil.getVarPlaceholders(panel.getCommand()))) {
//            executeCommand();
//        }

//        forgeParam(new ArrayList<>());
        configureRoute();
        return true;
    }

    /**
     * 配置路由
     */
    private void configureRoute(){
        // 复用变量环节
        if (!vDone) {
            Set<String> vars = panel.getVars().keySet();
            if(!CollectionUtils.isEmpty(vars)){
                forgeVarParam(new ArrayList<>(panel.getVars().keySet()), new TreeMap<>());
            }
            vDone = true;
        }

        // 懒人变量环节
        if (!tDone && !panel.getCommand().contains(PlaceholderEnum.PARAM_TEXT.getText())) {
            forgeLazyParam(new ArrayList<>());
            tDone = true;
        }


        executeCommand();
    }

    /**
     * 构建变量
     * @param varsList
     * @param varsMap
     */
    private void forgeVarParam(List<String> varsList, Map<String,String> varsMap){
        String varNow = varsList.get(varsMap.size());
        String text = panel.getVars().get(varNow);
        String title = "§4" + text;
        new AnvilGUI.Builder()
                .onClick((slot, StateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    varsMap.put(varNow, StateSnapshot.getText());
                    List<AnvilGUI.ResponseAction> res = new ArrayList<>();
                    res.add(AnvilGUI.ResponseAction.close());
                    if(varsMap.size() < varsList.size()){
                        res.add(AnvilGUI.ResponseAction.run(() ->
                                forgeVarParam(varsList, varsMap)));
                    }else{
                        panel.setVars(varsMap);
                        vDone = true;
                        configureRoute();
                    }

                    // 记录名称并且打开下一个
                    return res;

                })
                .title(title)
                .text(LangReader.get("请为参数命名"))
                .plugin(AnvilPanel.getInstance())
                .open(player);
    }

    /**
     * 构建懒人变量
     *
     * @param param
     */
    private void forgeLazyParam(List<String> param) {
//        String title = MyStringUtil.getWordsAroundT(panel.getCommand(),
//                param.size() + 1);
        String text = panel.getSubtitle().get(param.size());
        String title = "§4" + text;
        if(StringUtils.isBlank(text)){
            text = "";
        }

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
                                forgeLazyParam(param)));
                    } else {
                        res.add(AnvilGUI.ResponseAction.run(() -> {
                            paramCollector.setLazyParam(param);
                            tDone = true;
                            configureRoute();
                        }));
                    }
                    // 记录名称并且打开下一个
                    return res;

                })
                .title(title)
                .text(text)
                .plugin(AnvilPanel.getInstance())
                .open(player);
    }

    /**
     * 执行命令
     *
     */
    private void executeCommand() {

        String command = panel.getCommand();

        command = command.replace(PlaceholderEnum.PLAYER_NAME.getText(), player.getName());

        // TODO: 替换复用变量

        // 替换懒人变量
        String paramRegex = PlaceholderEnum.PARAM_TEXT.geRexText();
        for (String number : paramCollector.getLazyParam()) {
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


    //--------权限区域--------//
    /**
     * 是否可以自由执行
     *
     * @param player
     * @param panel
     * @return
     */
    private boolean avaFree(Player player, Panel panel) {
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
    private boolean avaGroup(Player player, Panel panel) {
        for (String grouName : panel.getGroup()) {
            Group group = GroupReader.getInstance().get(grouName);
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
    private boolean avaUser(Player player, Panel panel) {
        if (CollectionUtils.isEmpty(panel.getUsers())) {
            return false;
        }
        return panel.getUsers().contains(player.getName());
    }

    @Data
    //-----参数封装包类-----//
    public static class ParamCollector{

        /**
         * 变量表
         */
        Map<String,String> varParam = new TreeMap();

        /**
         * 懒人参数
         */
        List<String> lazyParam = new ArrayList<>();
    }
}
