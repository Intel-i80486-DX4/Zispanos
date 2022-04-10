//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.setting.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import java.util.function.*;
import me.zeroeightsix.kami.module.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;

@Info(name = "AutoTotem", category = Category.COMBAT)
public class AutoTotemLunar extends Module
{
    int totems;
    boolean moving;
    boolean returnI;
    boolean kill;
    boolean offhand;
    boolean shouldSwitch;
    private Setting<Boolean> soft;
    private Setting<Double> Hearts;
    private static AutoTotemLunar INSTANCE;
    
    public AutoTotemLunar() {
        this.moving = false;
        this.returnI = false;
        this.kill = false;
        this.offhand = false;
        this.shouldSwitch = false;
        this.soft = this.register(Settings.b("Soft"));
        this.Hearts = this.register(Settings.d("Health", 11.0));
        AutoTotemLunar.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (AutoTotemLunar.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        this.totems = AutoTotemLunar.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (AutoTotemLunar.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++this.totems;
        }
        else {
            if (this.soft.getValue() && !AutoTotemLunar.mc.player.getHeldItemOffhand().isEmpty && AutoTotemLunar.mc.player.getHealth() + AutoTotemLunar.mc.player.getAbsorptionAmount() >= this.Hearts.getValue()) {
                return;
            }
            if (!AutoTotemLunar.mc.player.getHeldItemOffhand().isEmpty) {
                this.offhand = true;
            }
            else {
                this.offhand = false;
            }
            if (AutoTotemLunar.mc.player.inventory.itemStack.isEmpty()) {
                if (this.totems == 0) {
                    return;
                }
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (AutoTotemLunar.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                        t = i;
                        break;
                    }
                }
                if (t == -1 || (AutoTotemLunar.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL && ModuleManager.isModuleEnabled("AutoCrystalLunar"))) {
                    return;
                }
                AutoTotemLunar.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotemLunar.mc.player);
                AutoTotemLunar.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoTotemLunar.mc.player);
                AutoTotemLunar.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotemLunar.mc.player);
                this.moving = true;
            }
        }
    }
    
    public static double health() {
        return AutoTotemLunar.INSTANCE.Hearts.getValue();
    }
    
    static {
        AutoTotemLunar.INSTANCE = new AutoTotemLunar();
    }
}
