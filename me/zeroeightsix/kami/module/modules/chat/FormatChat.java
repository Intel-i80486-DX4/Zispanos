//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.chat;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import java.util.function.*;
import net.minecraft.client.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.network.play.client.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.network.*;

@Info(name = "FormatChat", description = "Add colour and linebreak support to upstream chat packets", category = Category.HIDDEN)
public class FormatChat extends Module
{
    @EventHandler
    public Listener<PacketEvent.Send> sendListener;
    
    public FormatChat() {
        this.sendListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            if (event.getPacket() instanceof CPacketChatMessage) {
                String message = ((CPacketChatMessage)event.getPacket()).message;
                if (message.contains("&") || message.contains("#n")) {
                    message = message.replaceAll("&", "§");
                    message = message.replaceAll("#n", "\n");
                    Wrapper.getPlayer().connection.sendPacket((Packet)new CPacketChatMessage(message));
                    event.cancel();
                }
            }
        }, new Predicate[0]);
    }
    
    public void onEnable() {
        if (Minecraft.getMinecraft().getCurrentServerData() == null) {
            Command.sendWarningMessage(this.getChatName() + " &6&lWarning: &r&6This does not work in singleplayer");
            this.disable();
        }
        else {
            Command.sendWarningMessage(this.getChatName() + " &6&lWarning: &r&6This will kick you on most servers!");
        }
    }
}
