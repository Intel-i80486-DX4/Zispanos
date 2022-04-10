//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.player;

import me.zeroeightsix.kami.module.*;
import net.minecraft.network.play.client.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import net.minecraft.client.entity.*;
import java.util.*;
import java.util.function.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import net.minecraft.entity.player.*;

@Info(name = "Blink", category = Category.PLAYER, description = "Cancels server side packets")
public class Blink extends Module
{
    Queue<CPacketPlayer> packets;
    @EventHandler
    public Listener<PacketEvent.Send> listener;
    private EntityOtherPlayerMP clonedPlayer;
    
    public Blink() {
        this.packets = new LinkedList<CPacketPlayer>();
        this.listener = (Listener<PacketEvent.Send>)new Listener(event -> {
            if (this.isEnabled() && event.getPacket() instanceof CPacketPlayer) {
                event.cancel();
                this.packets.add((CPacketPlayer)event.getPacket());
            }
        }, new Predicate[0]);
    }
    
    @Override
    protected void onEnable() {
        if (Blink.mc.player != null) {
            (this.clonedPlayer = new EntityOtherPlayerMP((World)Blink.mc.world, Blink.mc.getSession().getProfile())).copyLocationAndAnglesFrom((Entity)Blink.mc.player);
            this.clonedPlayer.rotationYawHead = Blink.mc.player.rotationYawHead;
            Blink.mc.world.addEntityToWorld(-100, (Entity)this.clonedPlayer);
        }
    }
    
    @Override
    protected void onDisable() {
        while (!this.packets.isEmpty()) {
            Blink.mc.player.connection.sendPacket((Packet)this.packets.poll());
        }
        final EntityPlayer localPlayer = (EntityPlayer)Blink.mc.player;
        if (localPlayer != null) {
            Blink.mc.world.removeEntityFromWorld(-100);
            this.clonedPlayer = null;
        }
    }
    
    @Override
    public String getHudInfo() {
        return String.valueOf(this.packets.size());
    }
}
