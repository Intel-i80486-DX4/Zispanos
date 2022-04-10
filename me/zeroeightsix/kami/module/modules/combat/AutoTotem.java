//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.setting.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import java.util.function.*;
import me.zeroeightsix.kami.module.*;

@Info(name = "AutoTotemGay", category = Category.HIDDEN)
public class AutoTotem extends Module
{
    private Setting modeSetting;
    int totems;
    boolean moving;
    boolean returnI;
    private Setting<Double> Hearts;
    private static AutoTotem INSTANCE;
    
    public AutoTotem() {
        this.Hearts = this.register(Settings.d("Health", 11.0));
        this.modeSetting = this.register(Settings.e("Mode", Mode.REPLACE_OFFHAND));
        this.moving = false;
        this.returnI = false;
    }
    
    @Override
    public void onUpdate() {
        if (this.modeSetting.getValue().equals(Mode.INVENTORY) || !(AutoTotem.mc.currentScreen instanceof GuiContainer)) {
            if (this.returnI) {
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (AutoTotem.mc.player.inventory.getStackInSlot(i).isEmpty) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                AutoTotem.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                this.returnI = false;
            }
            this.totems = AutoTotem.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
            if (AutoTotem.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                ++this.totems;
            }
            else {
                if (!this.modeSetting.getValue().equals(Mode.REPLACE_OFFHAND) && !AutoTotem.mc.player.getHeldItemOffhand().isEmpty) {
                    return;
                }
                if (this.moving) {
                    AutoTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                    this.moving = false;
                    if (!AutoTotem.mc.player.inventory.itemStack.isEmpty()) {
                        this.returnI = true;
                    }
                    return;
                }
                if (AutoTotem.mc.player.inventory.itemStack.isEmpty()) {
                    if (this.totems == 0) {
                        return;
                    }
                    int t = -1;
                    for (int i = 0; i < 45; ++i) {
                        if (AutoTotem.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                            t = i;
                            break;
                        }
                    }
                    if (t == -1) {
                        return;
                    }
                    AutoTotem.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                    this.moving = true;
                }
                else if (this.modeSetting.getValue().equals(Mode.REPLACE_OFFHAND)) {
                    int t = -1;
                    for (int i = 0; i < 45; ++i) {
                        if (AutoTotem.mc.player.inventory.getStackInSlot(i).isEmpty) {
                            t = i;
                            break;
                        }
                    }
                    if (t == -1) {
                        return;
                    }
                    if (t == -1 || (AutoTotem.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL && ModuleManager.isModuleEnabled("AutoCrystalLunar"))) {
                        return;
                    }
                    AutoTotem.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                }
            }
        }
    }
    
    @Override
    public String getHudInfo() {
        return String.valueOf(this.totems);
    }
    
    public static double health() {
        return AutoTotem.INSTANCE.Hearts.getValue();
    }
    
    static {
        AutoTotem.INSTANCE = new AutoTotem();
    }
    
    private enum Mode
    {
        NEITHER, 
        REPLACE_OFFHAND, 
        INVENTORY;
    }
}
