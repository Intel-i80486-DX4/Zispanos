//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import net.minecraft.init.*;
import me.zeroeightsix.kami.command.*;

@Info(name = "AutoExp", category = Category.HIDDEN, description = "Automatically mends armour")
public class AutoExp extends Module
{
    private Setting<Boolean> autoThrow;
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> autoDisable;
    private int initHotbarSlot;
    @EventHandler
    private Listener<PacketEvent.Receive> receiveListener;
    
    public AutoExp() {
        this.autoThrow = this.register(Settings.b("Auto Throw", true));
        this.autoSwitch = this.register(Settings.b("Auto Switch", true));
        this.autoDisable = this.register(Settings.booleanBuilder("Auto Disable").withValue(true).withVisibility(o -> this.autoSwitch.getValue()).build());
        this.initHotbarSlot = -1;
        this.receiveListener = (Listener<PacketEvent.Receive>)new Listener(event -> {
            if (AutoExp.mc.player != null && AutoExp.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE) {
                AutoExp.mc.rightClickDelayTimer = 0;
            }
        }, new Predicate[0]);
    }
    
    @Override
    protected void onEnable() {
        if (AutoExp.mc.player == null) {
            return;
        }
        if (this.autoSwitch.getValue()) {
            this.initHotbarSlot = AutoExp.mc.player.inventory.currentItem;
        }
    }
    
    @Override
    protected void onDisable() {
        if (AutoExp.mc.player == null) {
            return;
        }
        if (this.autoSwitch.getValue() && this.initHotbarSlot != -1 && this.initHotbarSlot != AutoExp.mc.player.inventory.currentItem) {
            AutoExp.mc.player.inventory.currentItem = this.initHotbarSlot;
        }
    }
    
    @Override
    public void onUpdate() {
        if (AutoExp.mc.player == null) {
            return;
        }
        if (this.autoSwitch.getValue() && AutoExp.mc.player.getHeldItemMainhand().getItem() != Items.EXPERIENCE_BOTTLE) {
            final int xpSlot = this.findXpPots();
            if (xpSlot == -1) {
                if (this.autoDisable.getValue()) {
                    Command.sendWarningMessage(this.getChatName() + " No XP in hotbar, disabling");
                    this.disable();
                }
                return;
            }
            AutoExp.mc.player.inventory.currentItem = xpSlot;
        }
        if (this.autoThrow.getValue() && AutoExp.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE) {
            AutoExp.mc.rightClickMouse();
        }
    }
    
    private int findXpPots() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (AutoExp.mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }
}
