//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import java.util.function.*;
import net.minecraft.network.play.client.*;
import io.netty.buffer.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.network.*;

@Info(name = "BeaconSelector", category = Category.HIDDEN, description = "Choose any of the 5 beacon effects regardless of beacon base height")
public class BeaconSelector extends Module
{
    public static int effect;
    private boolean doCancelPacket;
    @EventHandler
    public Listener<PacketEvent.Send> packetListener;
    
    public BeaconSelector() {
        this.doCancelPacket = true;
        this.packetListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            if (event.getPacket() instanceof CPacketCustomPayload && ((CPacketCustomPayload)event.getPacket()).getChannelName().equals("MC|Beacon") && this.doCancelPacket) {
                this.doCancelPacket = false;
                final PacketBuffer data = ((CPacketCustomPayload)event.getPacket()).getBufferData();
                final int i1 = data.readInt();
                final int k1 = data.readInt();
                event.cancel();
                final PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
                buf.writeInt(BeaconSelector.effect);
                buf.writeInt(k1);
                Wrapper.getPlayer().connection.sendPacket((Packet)new CPacketCustomPayload("MC|Beacon", buf));
                this.doCancelPacket = true;
            }
        }, new Predicate[0]);
    }
    
    static {
        BeaconSelector.effect = -1;
    }
}
