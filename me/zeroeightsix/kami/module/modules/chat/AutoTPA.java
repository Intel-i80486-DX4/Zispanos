//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.chat;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import net.minecraft.network.play.server.*;
import me.zeroeightsix.kami.util.*;

@Info(name = "AutoTPA", description = "Automatically decline or accept TPA requests", category = Category.HIDDEN)
public class AutoTPA extends Module
{
    private Setting<mode> mod;
    @EventHandler
    public Listener<PacketEvent.Receive> receiveListener;
    
    public AutoTPA() {
        this.mod = this.register(Settings.e("Response", mode.DENY));
        this.receiveListener = (Listener<PacketEvent.Receive>)new Listener(event -> {
            final SPacketChat packet;
            if (event.getPacket() instanceof SPacketChat && (packet = (SPacketChat)event.getPacket()).getChatComponent().getUnformattedText().contains(" has requested to teleport to you.")) {
                switch (this.mod.getValue()) {
                    case ACCEPT: {
                        Wrapper.getPlayer().sendChatMessage("/tpaccept");
                        break;
                    }
                    case DENY: {
                        Wrapper.getPlayer().sendChatMessage("/tpdeny");
                        break;
                    }
                }
            }
        }, (Predicate[])new Predicate[0]);
    }
    
    public enum mode
    {
        ACCEPT, 
        DENY;
    }
}
