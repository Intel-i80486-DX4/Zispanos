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

@Info(name = "AutoTotemKAMI", category = Category.HIDDEN, description = "Refills your offhand with totems")
public class AutoTotemKAMI extends Module
{
    private Setting<Mode> modeSetting;
    private Setting<Double> healthSetting;
    private Setting<Double> Hearts;
    private static AutoTotemKAMI INSTANCE;
    int totems;
    boolean moving;
    boolean returnI;
    
    public AutoTotemKAMI() {
        this.modeSetting = this.register(Settings.e("Mode", Mode.REPLACE_OFFHAND));
        this.healthSetting = this.register(Settings.doubleBuilder("Max replace health").withValue(20.0).withVisibility(v -> this.modeSetting.getValue().equals(Mode.REPLACE_OFFHAND)));
        this.Hearts = this.register(Settings.d("Health", 11.0));
        this.moving = false;
        this.returnI = false;
    }
    
    public static double health() {
        return AutoTotemKAMI.INSTANCE.Hearts.getValue();
    }
    
    @Override
    public void onUpdate() {
        if (!this.passHealthCheck()) {
            return;
        }
        if (!this.modeSetting.getValue().equals(Mode.INVENTORY) && AutoTotemKAMI.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.returnI) {
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (AutoTotemKAMI.mc.player.inventory.getStackInSlot(i).isEmpty) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            AutoTotemKAMI.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotemKAMI.mc.player);
            this.returnI = false;
        }
        this.totems = AutoTotemKAMI.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (AutoTotemKAMI.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++this.totems;
        }
        else {
            if (!this.modeSetting.getValue().equals(Mode.REPLACE_OFFHAND) && !AutoTotemKAMI.mc.player.getHeldItemOffhand().isEmpty) {
                return;
            }
            if (this.moving) {
                AutoTotemKAMI.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoTotemKAMI.mc.player);
                this.moving = false;
                if (!AutoTotemKAMI.mc.player.inventory.itemStack.isEmpty()) {
                    this.returnI = true;
                }
                return;
            }
            if (AutoTotemKAMI.mc.player.inventory.itemStack.isEmpty()) {
                if (this.totems == 0) {
                    return;
                }
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (AutoTotemKAMI.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                AutoTotemKAMI.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotemKAMI.mc.player);
                this.moving = true;
            }
            else if (this.modeSetting.getValue().equals(Mode.REPLACE_OFFHAND)) {
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (AutoTotemKAMI.mc.player.inventory.getStackInSlot(i).isEmpty) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                if (t == -1 || (AutoTotemKAMI.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL && ModuleManager.isModuleEnabled("AutoCrystalLunar"))) {
                    return;
                }
                AutoTotemKAMI.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotemKAMI.mc.player);
            }
        }
    }
    
    private boolean passHealthCheck() {
        return !this.modeSetting.getValue().equals(Mode.REPLACE_OFFHAND) || AutoTotemKAMI.mc.player.getHealth() + AutoTotemKAMI.mc.player.getAbsorptionAmount() <= this.healthSetting.getValue();
    }
    
    @Override
    public String getHudInfo() {
        return String.valueOf(this.totems);
    }
    
    static {
        AutoTotemKAMI.INSTANCE = new AutoTotemKAMI();
    }
    
    private enum Mode
    {
        NEITHER, 
        REPLACE_OFFHAND, 
        INVENTORY;
    }
}
