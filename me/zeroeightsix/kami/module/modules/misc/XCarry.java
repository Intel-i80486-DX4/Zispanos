//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import java.util.function.*;
import net.minecraft.network.play.client.*;

@Info(name = "XCarry", category = Category.PLAYER, description = "Store items in crafting slots", showOnArray = ShowOnArray.OFF)
public class XCarry extends Module
{
    @EventHandler
    private Listener<PacketEvent.Send> l;
    
    public XCarry() {
        this.l = (Listener<PacketEvent.Send>)new Listener(event -> {
            if (event.getPacket() instanceof CPacketCloseWindow) {
                event.cancel();
            }
        }, new Predicate[0]);
    }
}
