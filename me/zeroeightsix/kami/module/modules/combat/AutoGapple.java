//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import java.util.function.*;

@Info(name = "AutoOffhandGap", category = Category.COMBAT)
public class AutoGapple extends Module
{
    int totems;
    boolean moving;
    boolean returnI;
    boolean kill;
    boolean offhand;
    boolean shouldSwitch;
    private Setting<Double> Hearts;
    
    public AutoGapple() {
        this.moving = false;
        this.returnI = false;
        this.kill = false;
        this.offhand = false;
        this.shouldSwitch = false;
        this.Hearts = this.register(Settings.d("Health", 11.0));
    }
    
    @Override
    public void onUpdate() {
        if (AutoGapple.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.returnI) {
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (AutoGapple.mc.player.inventory.getStackInSlot(i).isEmpty) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            AutoGapple.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoGapple.mc.player);
            this.returnI = false;
        }
        this.totems = AutoGapple.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        if (!AutoGapple.mc.player.getHeldItemOffhand().isEmpty) {
            this.offhand = true;
        }
        else {
            this.offhand = false;
        }
        if (AutoGapple.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
            ++this.totems;
        }
        else {
            if (AutoGapple.mc.player.getHealth() + AutoGapple.mc.player.getAbsorptionAmount() <= this.Hearts.getValue()) {
                return;
            }
            if (this.totems == 0) {
                return;
            }
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (AutoGapple.mc.player.inventory.getStackInSlot(i).getItem() == Items.GOLDEN_APPLE) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            AutoGapple.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoGapple.mc.player);
            AutoGapple.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoGapple.mc.player);
            if (this.offhand) {
                AutoGapple.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoGapple.mc.player);
            }
            this.moving = true;
        }
    }
    
    public void onDisable() {
        int t = -1;
        for (int i = 0; i < 45; ++i) {
            if (AutoGapple.mc.player.inventory.getStackInSlot(i).isEmpty || AutoGapple.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                t = i;
                break;
            }
        }
        AutoGapple.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoGapple.mc.player);
        AutoGapple.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoGapple.mc.player);
        AutoGapple.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoGapple.mc.player);
    }
}
