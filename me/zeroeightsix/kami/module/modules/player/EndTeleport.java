//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.player;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.client.network.*;
import java.util.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.text.*;

@Info(name = "EndTeleport", category = Category.HIDDEN, description = "Allows for teleportation when going through end portals")
public class EndTeleport extends Module
{
    private Setting<Boolean> confirmed;
    @EventHandler
    public Listener<PacketEvent.Receive> receiveListener;
    
    public EndTeleport() {
        this.confirmed = this.register(Settings.b("Confirm", true));
        this.receiveListener = (Listener<PacketEvent.Receive>)new Listener(event -> {
            if (event.getPacket() instanceof SPacketRespawn && ((SPacketRespawn)event.getPacket()).getDimensionID() == 1 && this.confirmed.getValue()) {
                Objects.requireNonNull(Wrapper.getMinecraft().getConnection()).handleDisconnect(new SPacketDisconnect((ITextComponent)new TextComponentString("Attempting teleportation exploit")));
                this.disable();
            }
        }, new Predicate[0]);
    }
    
    public void onEnable() {
        if (Wrapper.getMinecraft().getCurrentServerData() == null) {
            Command.sendWarningMessage(this.getChatName() + "This module does not work in singleplayer");
            this.disable();
        }
        else if (!this.confirmed.getValue()) {
            Command.sendWarningMessage(this.getChatName() + "This module will kick you from the server! It is part of the exploit and cannot be avoided");
        }
    }
}
