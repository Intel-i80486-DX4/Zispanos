//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import java.util.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

@Info(name = "AntiAFK", category = Category.HIDDEN, description = "Prevents being kicked for AFK")
public class AntiAFK extends Module
{
    private Setting<Boolean> swing;
    private Setting<Boolean> turn;
    private Random random;
    
    public AntiAFK() {
        this.swing = this.register(Settings.b("Swing", true));
        this.turn = this.register(Settings.b("Turn", true));
        this.random = new Random();
    }
    
    @Override
    public void onUpdate() {
        if (AntiAFK.mc.playerController.getIsHittingBlock()) {
            return;
        }
        if (AntiAFK.mc.player.ticksExisted % 40 == 0 && this.swing.getValue()) {
            AntiAFK.mc.getConnection().sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        }
        if (AntiAFK.mc.player.ticksExisted % 15 == 0 && this.turn.getValue()) {
            AntiAFK.mc.player.rotationYaw = (float)(this.random.nextInt(360) - 180);
        }
        if (!this.swing.getValue() && !this.turn.getValue() && AntiAFK.mc.player.ticksExisted % 80 == 0) {
            AntiAFK.mc.player.jump();
        }
    }
}
