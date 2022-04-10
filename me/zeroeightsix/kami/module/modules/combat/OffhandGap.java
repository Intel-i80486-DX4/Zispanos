//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.module.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import java.util.function.*;

@Info(name = "OffhandGapGay", category = Category.HIDDEN)
public class OffhandGap extends Module
{
    int totems;
    boolean moving;
    boolean returnI;
    private Setting force;
    private Setting inv;
    private Setting minHealth;
    private boolean disabledAutoTotem;
    
    public OffhandGap() {
        this.moving = false;
        this.returnI = false;
        this.force = this.register(Settings.b("Replace Offhand", true));
        this.inv = this.register(Settings.b("Inventory", true));
        this.minHealth = this.register(Settings.d("MinHealth", 8.0));
    }
    
    public void onEnable() {
        this.disabledAutoTotem = false;
        if (OffhandGap.mc.player.getHealth() < this.minHealth.getValue()) {
            Command.sendChatMessage("Not enabling OffhandGap due to insufficient health");
            this.disable();
        }
        else {
            final Module m = ModuleManager.getModuleByName("AutoTotem");
            if (m.isEnabled()) {
                this.disabledAutoTotem = true;
                m.disable();
            }
        }
    }
    
    public void onDisable() {
        if (this.disabledAutoTotem) {
            final Module m = ModuleManager.getModuleByName("AutoTotem");
            m.disable();
            if (m.isDisabled()) {
                m.enable();
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.inv.getValue() || !(OffhandGap.mc.currentScreen instanceof GuiContainer)) {
            if (OffhandGap.mc.player.getHealth() < this.minHealth.getValue()) {
                Command.sendChatMessage("OffhandGap disabled due to insufficient health");
                this.disable();
            }
            if (this.returnI) {
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (OffhandGap.mc.player.inventory.getStackInSlot(i).isEmpty) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                OffhandGap.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.player);
                this.returnI = false;
            }
            this.totems = OffhandGap.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
            if (OffhandGap.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
                ++this.totems;
            }
            else {
                if (!this.force.getValue() && !OffhandGap.mc.player.getHeldItemOffhand().isEmpty) {
                    return;
                }
                if (this.moving) {
                    OffhandGap.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.player);
                    this.moving = false;
                    if (!OffhandGap.mc.player.inventory.itemStack.isEmpty()) {
                        this.returnI = true;
                    }
                    return;
                }
                if (OffhandGap.mc.player.inventory.itemStack.isEmpty()) {
                    if (this.totems == 0) {
                        return;
                    }
                    int t = -1;
                    for (int i = 0; i < 45; ++i) {
                        if (OffhandGap.mc.player.inventory.getStackInSlot(i).getItem() == Items.GOLDEN_APPLE) {
                            t = i;
                            break;
                        }
                    }
                    if (t == -1) {
                        return;
                    }
                    OffhandGap.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.player);
                    this.moving = true;
                }
                else if (this.force.getValue()) {
                    int t = -1;
                    for (int i = 0; i < 45; ++i) {
                        if (OffhandGap.mc.player.inventory.getStackInSlot(i).isEmpty) {
                            t = i;
                            break;
                        }
                    }
                    if (t == -1) {
                        return;
                    }
                    OffhandGap.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.player);
                }
            }
        }
    }
    
    @Override
    public String getHudInfo() {
        return String.valueOf(this.totems);
    }
}
