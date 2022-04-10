//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import java.util.function.*;
import net.minecraft.network.play.client.*;

@Info(name = "AntiHunger", category = Category.MOVEMENT, description = "Reduces hunger lost when moving around")
public class AntiHunger extends Module
{
    @EventHandler
    public Listener<PacketEvent.Send> packetListener;
    
    public AntiHunger() {
        this.packetListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            if (event.getPacket() instanceof CPacketPlayer) {
                ((CPacketPlayer)event.getPacket()).onGround = false;
            }
        }, new Predicate[0]);
    }
}
