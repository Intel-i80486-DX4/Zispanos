//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.hidden;

import me.zeroeightsix.kami.module.*;
import net.minecraft.init.*;
import net.minecraft.enchantment.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;

@Info(name = "Hidden:Pull32k", category = Category.HIDDEN, description = "Pulls 32ks out of hoppers automatically")
public class Pull32k extends Module
{
    boolean foundsword;
    
    public Pull32k() {
        this.foundsword = false;
    }
    
    @Override
    public void onUpdate() {
        boolean foundair = false;
        int enchantedSwordIndex = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack itemStack = (ItemStack)Pull32k.mc.player.inventory.mainInventory.get(i);
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, itemStack) >= 32767) {
                enchantedSwordIndex = i;
                this.foundsword = true;
            }
            if (!this.foundsword) {
                enchantedSwordIndex = -1;
                this.foundsword = false;
            }
        }
        if (enchantedSwordIndex != -1 && Pull32k.mc.player.inventory.currentItem != enchantedSwordIndex) {
            Pull32k.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(enchantedSwordIndex));
            Pull32k.mc.player.inventory.currentItem = enchantedSwordIndex;
            Pull32k.mc.playerController.updateController();
        }
        if (enchantedSwordIndex == -1 && Pull32k.mc.player.openContainer != null && Pull32k.mc.player.openContainer instanceof ContainerHopper && Pull32k.mc.player.openContainer.inventorySlots != null && !Pull32k.mc.player.openContainer.inventorySlots.isEmpty()) {
            for (int i = 0; i < 5; ++i) {
                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, Pull32k.mc.player.openContainer.inventorySlots.get(0).inventory.getStackInSlot(i)) >= 32767) {
                    enchantedSwordIndex = i;
                    break;
                }
            }
            if (enchantedSwordIndex == -1) {
                return;
            }
            if (enchantedSwordIndex != -1) {
                for (int i = 0; i < 9; ++i) {
                    final ItemStack itemStack = (ItemStack)Pull32k.mc.player.inventory.mainInventory.get(i);
                    if (itemStack.getItem() instanceof ItemAir) {
                        if (Pull32k.mc.player.inventory.currentItem != i) {
                            Pull32k.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(i));
                            Pull32k.mc.player.inventory.currentItem = i;
                            Pull32k.mc.playerController.updateController();
                        }
                        foundair = true;
                        break;
                    }
                }
            }
            if (foundair || this.checkStuff()) {
                Pull32k.mc.playerController.windowClick(Pull32k.mc.player.openContainer.windowId, enchantedSwordIndex, Pull32k.mc.player.inventory.currentItem, ClickType.SWAP, (EntityPlayer)Pull32k.mc.player);
            }
        }
    }
    
    public boolean checkStuff() {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, Pull32k.mc.player.inventory.getCurrentItem()) == 5;
    }
}
