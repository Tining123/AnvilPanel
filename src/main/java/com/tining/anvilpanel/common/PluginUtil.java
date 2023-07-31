package com.tining.anvilpanel.common;

import com.tining.anvilpanel.AnvilPanel;
import com.tining.anvilpanel.gui.AdminGroupListGUI;
import com.tining.anvilpanel.model.enums.PublicSignEnumInterface;
import com.tining.anvilpanel.model.enums.SignMaterialEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PluginUtil {

    /**
     * nms版本
     */
    private static String nmsVersion;

    public static String cipherKey = "AnvilPanel";

    /**
     * 设置标签
     *
     * @param signEnum
     * @param signMaterialEnum
     * @param label
     * @param lore
     */
    public static void setSign(Inventory inventory, PublicSignEnumInterface signEnum, SignMaterialEnum signMaterialEnum
            , String label, List<String> lore) {
        Material material = signMaterialEnum.getMaterial();
        if (Objects.isNull(material)) {
            return;
        }
        ItemStack itemStack = new ItemStack(material);
        if (!CollectionUtils.isEmpty(lore)) {
            PluginUtil.addLore(itemStack, lore);
        }
        PluginUtil.setName(itemStack, label);
        inventory.setItem(signEnum.getSlot(), itemStack);
    }

    /**
     * 为物品快速加上lore，不复制
     * @param itemStack
     * @param addOnLore
     * @return
     */
    public static ItemStack addLore(ItemStack itemStack, List<String> addOnLore){
        if(Objects.isNull(itemStack)){
            return itemStack;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemMeta.getLore();
        if(CollectionUtils.isEmpty(lore)){
            lore = new ArrayList<>();
        }
        lore.addAll(addOnLore);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * 为物品快速加上名称
     * @param itemStack
     * @return
     */
    public static ItemStack setName(ItemStack itemStack, String name){
        if(Objects.isNull(itemStack)){
            return itemStack;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * 获取nms版本
     *
     * @return
     */
    public static String getNMSVersion() {
        if (StringUtils.isEmpty(nmsVersion)) {
            nmsVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        }
        return nmsVersion;
    }

    /**
     * 获取字典存储的nbt名称key
     *
     * @param is
     * @return
     */
    public static String getKeyName(ItemStack is) {
        String nbtinfo = "";
        try {
            ItemStack newIs = is.clone();
            newIs.setAmount(1);
//            nbtinfo = JsonItemStack.getJsonAsNBTTagCompound(newIs);
            nbtinfo = itemStackSerialize(newIs);
            nbtinfo = compress(nbtinfo);
        }catch (Exception e){
            AnvilPanel.getInstance().getLogger().info(e.toString());
        }
        return nbtinfo;
    }

    /**
     * 使用yml序列化
     * @param itemStack
     * @return
     */
    public static String itemStackSerialize(ItemStack itemStack) {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("item", itemStack);
        return yml.saveToString();
    }

    /**
     * 使用yml反序列化
     * @param str
     * @return
     */
    public static ItemStack itemStackDeserialize(String str) {
        YamlConfiguration yml = new YamlConfiguration();
        ItemStack item;
        try {
            yml.loadFromString(str);
            item = yml.getItemStack("item");
        } catch (InvalidConfigurationException ex) {
            item = new ItemStack(Material.AIR, 1);
        }
        return item;
    }

    /**
     * 对NBT编码反解
     *
     * @param code
     * @return
     */
    public static String getNBTBack(String code) {
        return decompress(code);
    }

    /**
     * 使用gzip进行压缩
     */
    public static String compress(String primStr) {
        if (primStr == null || primStr.length() == 0) {
            return primStr;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(primStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Base64.getUrlEncoder().encodeToString(out.toByteArray());
    }

    /**
     * 使用gzip进行解压缩
     */
    public static String decompress(String compressedStr) {
        if (compressedStr == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        byte[] compressed = null;
        String decompressed = null;
        try {
            compressed = Base64.getUrlDecoder().decode(compressedStr);
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);

            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            try {
                out.close();
            } catch (IOException e) {
            }
        }
        return decompressed;
    }

    /**
     * 根据名字获取一个物品堆
     *
     * @param name
     * @return
     */
    public static ItemStack getItem(String name) {
        if (Objects.isNull(name)) {
            return null;
        }
        Material material = Material.getMaterial(name);
        if(Objects.isNull(material)){
            return null;
        }
        return new ItemStack(material);
    }

    /**
     * 从存储的NBT物品信息恢复一个NBT物品，带“|”格式
     *
     * @param info
     * @return
     */
    public static ItemStack getItemStackFromSaveNBTString(String info) {
        return itemStackDeserialize(decompress(info));
    }

}
