package com.tining.anvilpanel.gui.admin.panel;

import com.google.common.base.Joiner;
import com.tining.anvilpanel.AnvilPanel;
import com.tining.anvilpanel.common.MyStringUtil;
import com.tining.anvilpanel.model.Panel;
import com.tining.anvilpanel.model.enums.PanelCreateTypeEnum;
import com.tining.anvilpanel.model.enums.PlaceholderEnum;
import com.tining.anvilpanel.storage.LangReader;
import com.tining.anvilpanel.storage.PanelReader;
import net.wesjd.anvilgui.AnvilGUI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.stream.Collectors;

public class AdminPanelCreateGUI {

    /**
     * 所在的面板的实体
     */
    public Inventory inventory;

    /**
     * 持有者
     */
    Player player;

    /**
     * 懒人变量完成标志
     */
    Boolean tDone;

    /**
     * 复用变量完成标志
     */
    Boolean vDone;

    /**
     * 类型
     */
    PanelCreateTypeEnum panelCreateTypeEnum;

    /**
     * 操作panel
     */
    Panel panel;

    /**
     * 构造GUI
     *
     * @param player
     */
    public AdminPanelCreateGUI(Player player, Panel panel, PanelCreateTypeEnum panelCreateTypeEnum) {
        tDone = false;
        vDone = false;
        this.player = player;
        this.panelCreateTypeEnum = panelCreateTypeEnum;
        this.panel = panel;
    }

    /**
     * 开始创建菜单并且设置标题
     *
     */
    public void openAnvilGUI() {
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
                            AnvilGUI.ResponseAction.run(() ->{
                                if(panelCreateTypeEnum.equals(PanelCreateTypeEnum.NORMAL)){
                                    setCommand();
                                }else if(panelCreateTypeEnum.equals(PanelCreateTypeEnum.LONG)){
                                    setStepCommand(new ArrayList<>());
                                }
                            }));

                })
                .title(LangReader.get("创建命令"))
                .text(LangReader.get("请输入名称"))
                .plugin(AnvilPanel.getInstance())
                .open(player);
    }

    /**
     * 设置完标题开始设置
     *
     */
    public void setCommand() {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    List<AnvilGUI.ResponseAction> res = new ArrayList<>();
                    res.add(AnvilGUI.ResponseAction.close());
                    panel.setCommand(stateSnapshot.getText());
                    res.add(AnvilGUI.ResponseAction.run(() -> {
                        configureRoute(panel);
                    }));
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
     */
    public void setStepCommand(List<String> stepCommands) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    // 记录名称并且打开下一个
                    List<AnvilGUI.ResponseAction> res = new ArrayList<>();
                    res.add(AnvilGUI.ResponseAction.close());
                    res.add(AnvilGUI.ResponseAction.run(() -> {
                        String text =stateSnapshot.getText();
                        stepCommands.add(text);
                        if(StringUtils.isNotBlank(text)){
                            setStepCommand(stepCommands);
                        }else{
                            panel.setCommand(Joiner.on(";").join(stepCommands));
                            configureRoute(panel);
                        }
                    }));

                    return res;
                })
                .title(LangReader.get("创建命令"))
                .text(LangReader.get("如已完成请留空"))
                .plugin(AnvilPanel.getInstance())
                .open(player);
    }

    /**
     * 配置路由
     */
    private void configureRoute(Panel panel) {
        String command = panel.getCommand();

        // 复用变量环节
        if (!vDone) {
            Set<String> vars = MyStringUtil.getVarPlaceholders(command);
            if (!vars.isEmpty()) {
                panel.getVars().putAll(vars.stream().collect(Collectors.toMap(varStr -> varStr, varStr -> varStr)));
                setVars(new ArrayList<>(panel.getVars().keySet()), new TreeMap<>(), panel);
            }
            vDone = true;
        }

        // 懒人变量环节
        if (!tDone && !command.contains(PlaceholderEnum.PARAM_TEXT.getText())) {
            setTips(new ArrayList<>(), panel);
            tDone = true;
        }

        // 收尾
        end(panel);
    }


    /**
     * 优先设置变量
     * @param varsMap
     * @param panel
     */
    private void setVars(List<String> varsList, Map<String,String> varsMap, Panel panel){
        String varNow = varsList.get(varsMap.size());
        String title = MyStringUtil.getWordsAroundV(panel.getCommand(), panel.getVars().get(varNow),
                varsList.size() + 1);
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
                                setVars(varsList, varsMap, panel)));
                    }else{
                        panel.setVars(varsMap);
                        vDone = true;
                        configureRoute(panel);
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
     * 设置完标题开始设置
     *
     */
    private void setTips(List<String> tips, Panel panel) {
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
                                setTips(tips, panel)));
                    } else {
                        res.add(AnvilGUI.ResponseAction.run(() ->{
                                panel.setSubtitle(tips);
                                // 结束了，返回路由
                                tDone = true;
                                configureRoute(panel);
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

    private void end(Panel panel) {
        // panel默认权限节点控制
        panel.setOath(true);
        PanelReader.getInstance().addToList(panel);
        player.sendMessage(LangReader.get("创建命令成功"));
    }
}
