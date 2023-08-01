package com.tining.anvilpanel;

import com.tining.anvilpanel.command.AdminCommand;
import com.tining.anvilpanel.command.UserCommand;
import com.tining.anvilpanel.common.Metrics;
import com.tining.anvilpanel.event.*;
import com.tining.anvilpanel.event.storage.ConfigReader;
import com.tining.anvilpanel.event.storage.LangReader;
import com.tining.anvilpanel.gui.V1.TemplateGuiEventV1;
import com.tining.anvilpanel.gui.V1.TemplateListGuiEventV1;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class AnvilPanel extends JavaPlugin {

    /**
     * 自身实例
     */
    private static AnvilPanel instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        // register event
        registerEvent();
        // register executor
        setExecutor();
        // register bstate
        int pluginId = 19235;
        Metrics metrics = new Metrics(this, pluginId);

        // 释放配置文件
        saveDefaultConfig();
        ConfigReader.initRelease();
        ConfigReader.reloadConfig();
        // 如果有设置强制预言，加载强制语言
        if (!Objects.isNull(ConfigReader.getLanguage()) && !StringUtils.isEmpty(ConfigReader.getLanguage())) {
            LangReader.setLanguage(ConfigReader.getLanguage());
        }
        LangReader.initRelease();
        LangReader.reloadLang();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getScheduler().cancelTasks(this);
        getLogger().info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    /**
     * 注册事件
     */
    public void registerEvent() {
        PluginManager pm = Bukkit.getPluginManager();
        //pm.registerEvents(new AnvilGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminPanelListGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminPanelSettingGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminGroupListGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminGroupUserListGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminPanelGroupListGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminPanelGroupAddGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminGroupAddUserWayGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminGroupAddUserChestGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminGroupDeleteGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminGroupUserDeleteGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminPanelGroupDeleteGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminPanelUserListGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminPanelAddUserWayGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminPanelUserDeleteGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminPanelAddUserChestGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new AdminPanelDeleteGuiEvent(), AnvilPanel.getInstance());
        pm.registerEvents(new TemplateListGuiEventV1(), AnvilPanel.getInstance());
        pm.registerEvents(new TemplateGuiEventV1(), AnvilPanel.getInstance());
    }

    public void setExecutor() {
        if (Bukkit.getPluginCommand("ap") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("ap")).setExecutor(new UserCommand());
        }
        if (Bukkit.getPluginCommand("anvilpanel") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("anvilpanel")).setExecutor(new UserCommand());
        }
        if (Bukkit.getPluginCommand("apadmin") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("apadmin")).setExecutor(new AdminCommand());
        }
        if (Bukkit.getPluginCommand("anvilpaneladmin") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("anvilpaneladmin")).setExecutor(new AdminCommand());
        }
    }

    public static AnvilPanel getInstance() {
        return instance;
    }

}
