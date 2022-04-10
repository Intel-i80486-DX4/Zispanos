//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.player;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import net.minecraft.client.settings.*;
import net.minecraft.util.*;

@Info(name = "AutoEat", description = "Automatically eat when hungry", category = Category.HIDDEN)
public class AutoEat extends Module
{
    private Setting<Integer> foodLevel;
    private int lastSlot;
    private boolean eating;
    
    public AutoEat() {
        this.foodLevel = this.register((Setting<Integer>)Settings.integerBuilder("Eat level").withValue(15).withMinimum(1).withMaximum(20).build());
        this.lastSlot = -1;
        this.eating = false;
    }
    
    private boolean isValid(final ItemStack stack, final int food) {
        return stack.getItem() instanceof ItemFood && this.foodLevel.getValue() - food >= ((ItemFood)stack.getItem()).getHealAmount(stack) && this.passItemCheck(stack.getItem());
    }
    
    private boolean passItemCheck(final Item item) {
        return item != Items.ROTTEN_FLESH && item != Items.SPIDER_EYE && item != Items.POISONOUS_POTATO && (item != Items.FISH || new ItemStack(Items.FISH).getItemDamage() != 3);
    }
    
    @Override
    public void onUpdate() {
        if (this.eating && !AutoEat.mc.player.isHandActive()) {
            if (this.lastSlot != -1) {
                AutoEat.mc.player.inventory.currentItem = this.lastSlot;
                this.lastSlot = -1;
            }
            this.eating = false;
            KeyBinding.setKeyBindState(AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            return;
        }
        if (this.eating) {
            return;
        }
        final FoodStats stats = AutoEat.mc.player.getFoodStats();
        if (this.isValid(AutoEat.mc.player.getHeldItemOffhand(), stats.getFoodLevel())) {
            AutoEat.mc.player.setActiveHand(EnumHand.OFF_HAND);
            this.eating = true;
            KeyBinding.setKeyBindState(AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            AutoEat.mc.rightClickMouse();
        }
        else {
            for (int i = 0; i < 9; ++i) {
                if (this.isValid(AutoEat.mc.player.inventory.getStackInSlot(i), stats.getFoodLevel())) {
                    this.lastSlot = AutoEat.mc.player.inventory.currentItem;
                    AutoEat.mc.player.inventory.currentItem = i;
                    this.eating = true;
                    KeyBinding.setKeyBindState(AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                    AutoEat.mc.rightClickMouse();
                    return;
                }
            }
        }
    }
}
