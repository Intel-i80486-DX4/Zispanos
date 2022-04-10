//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.player;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import java.util.function.*;
import net.minecraft.network.play.server.*;

@Info(name = "AntiForceLook", category = Category.HIDDEN, description = "Stops server packets from turning your head")
public class AntiForceLook extends Module
{
    @EventHandler
    Listener<PacketEvent.Receive> receiveListener;
    
    public AntiForceLook() {
        this.receiveListener = (Listener<PacketEvent.Receive>)new Listener(event -> {
            if (AntiForceLook.mc.player == null) {
                return;
            }
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                final SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
                packet.yaw = AntiForceLook.mc.player.rotationYaw;
                packet.pitch = AntiForceLook.mc.player.rotationPitch;
            }
        }, new Predicate[0]);
    }
}
