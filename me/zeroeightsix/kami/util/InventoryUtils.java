//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.util;

import net.minecraft.client.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import java.util.function.*;
import me.zeroeightsix.kami.module.modules.combat.*;

public class InventoryUtils
{
    private static final Minecraft mc;
    private static boolean moving;
    private static boolean returnI;
    
    public static void OffhandCrystal() {
        if (InventoryUtils.returnI) {
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (InventoryUtils.mc.player.inventory.getStackInSlot(i).isEmpty) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            InventoryUtils.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
            InventoryUtils.returnI = false;
        }
        int crystals = InventoryUtils.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        if (InventoryUtils.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            ++crystals;
        }
        else {
            if (InventoryUtils.mc.player.getHealth() + InventoryUtils.mc.player.getAbsorptionAmount() <= AutoTotemLunar.health()) {
                return;
            }
            if (InventoryUtils.moving) {
                InventoryUtils.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
                InventoryUtils.moving = false;
                if (!InventoryUtils.mc.player.inventory.itemStack.isEmpty()) {
                    InventoryUtils.returnI = true;
                }
                return;
            }
            if (InventoryUtils.mc.player.inventory.itemStack.isEmpty()) {
                if (crystals == 0) {
                    return;
                }
                int t2 = -1;
                for (int j = 0; j < 45; ++j) {
                    if (InventoryUtils.mc.player.inventory.getStackInSlot(j).getItem() == Items.END_CRYSTAL) {
                        t2 = j;
                        break;
                    }
                }
                if (t2 == -1) {
                    return;
                }
                InventoryUtils.mc.playerController.windowClick(0, (t2 < 9) ? (t2 + 36) : t2, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
                InventoryUtils.moving = true;
            }
            else {
                int t2 = -1;
                for (int j = 0; j < 45; ++j) {
                    if (InventoryUtils.mc.player.inventory.getStackInSlot(j).isEmpty) {
                        t2 = j;
                        break;
                    }
                }
                if (t2 == -1) {
                    return;
                }
                InventoryUtils.mc.playerController.windowClick(0, (t2 < 9) ? (t2 + 36) : t2, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
            }
        }
    }
    
    public static void OffhandCrystalReset() {
        final int crystals = InventoryUtils.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (InventoryUtils.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
            if (crystals == 0) {
                return;
            }
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (InventoryUtils.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            InventoryUtils.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
            InventoryUtils.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
            InventoryUtils.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
        }
    }
    
    static {
        mc = Minecraft.getMinecraft();
        InventoryUtils.moving = false;
        InventoryUtils.returnI = false;
    }
}
