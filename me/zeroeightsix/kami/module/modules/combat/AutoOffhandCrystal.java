//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import java.util.function.*;

@Info(name = "AutoOffhandCrystal", category = Category.COMBAT)
public class AutoOffhandCrystal extends Module
{
    int crystals;
    boolean moving;
    boolean returnI;
    
    public AutoOffhandCrystal() {
        this.moving = false;
        this.returnI = false;
    }
    
    @Override
    public void onUpdate() {
        if (AutoOffhandCrystal.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.returnI) {
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (AutoOffhandCrystal.mc.player.inventory.getStackInSlot(i).isEmpty) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            AutoOffhandCrystal.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoOffhandCrystal.mc.player);
            this.returnI = false;
        }
        this.crystals = AutoOffhandCrystal.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        if (AutoOffhandCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            ++this.crystals;
        }
        else {
            if (AutoOffhandCrystal.mc.player.getHealth() + AutoOffhandCrystal.mc.player.getAbsorptionAmount() <= AutoTotem.health()) {
                return;
            }
            if (this.moving) {
                AutoOffhandCrystal.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoOffhandCrystal.mc.player);
                this.moving = false;
                if (!AutoOffhandCrystal.mc.player.inventory.itemStack.isEmpty()) {
                    this.returnI = true;
                }
                return;
            }
            if (AutoOffhandCrystal.mc.player.inventory.itemStack.isEmpty()) {
                if (this.crystals == 0) {
                    return;
                }
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (AutoOffhandCrystal.mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                AutoOffhandCrystal.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoOffhandCrystal.mc.player);
                this.moving = true;
            }
            else {
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (AutoOffhandCrystal.mc.player.inventory.getStackInSlot(i).isEmpty) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                AutoOffhandCrystal.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoOffhandCrystal.mc.player);
            }
        }
    }
    
    public void onDisable() {
        if (AutoOffhandCrystal.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        this.crystals = AutoOffhandCrystal.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (AutoOffhandCrystal.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
            if (this.crystals == 0) {
                return;
            }
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (AutoOffhandCrystal.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            AutoOffhandCrystal.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoOffhandCrystal.mc.player);
            AutoOffhandCrystal.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoOffhandCrystal.mc.player);
            AutoOffhandCrystal.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoOffhandCrystal.mc.player);
        }
    }
}
