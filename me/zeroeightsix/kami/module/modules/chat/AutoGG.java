//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.chat;

import me.zeroeightsix.kami.module.*;
import java.util.concurrent.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import net.minecraftforge.event.entity.living.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.network.play.client.*;
import net.minecraft.world.*;

@Info(name = "AutoGG", category = Category.CHAT, description = "Announce killed Players")
public class AutoGG extends Module
{
    private ConcurrentHashMap<String, Integer> targetedPlayers;
    private Setting<Boolean> toxicMode;
    private Setting<Boolean> clientName;
    private Setting<Integer> timeoutTicks;
    @EventHandler
    public Listener<PacketEvent.Send> sendListener;
    @EventHandler
    public Listener<LivingDeathEvent> livingDeathEventListener;
    
    public AutoGG() {
        this.targetedPlayers = null;
        this.toxicMode = this.register(Settings.b("ToxicMode", false));
        this.clientName = this.register(Settings.b("ClientName", true));
        this.timeoutTicks = this.register(Settings.i("TimeoutTicks", 20));
        this.sendListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            if (AutoGG.mc.player == null) {
                return;
            }
            if (this.targetedPlayers == null) {
                this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
            }
            if (!(event.getPacket() instanceof CPacketUseEntity)) {
                return;
            }
            final CPacketUseEntity cPacketUseEntity = (CPacketUseEntity)event.getPacket();
            if (!cPacketUseEntity.getAction().equals((Object)CPacketUseEntity.Action.ATTACK)) {
                return;
            }
            final Entity targetEntity = cPacketUseEntity.getEntityFromWorld((World)AutoGG.mc.world);
            if (!EntityUtil.isPlayer(targetEntity)) {
                return;
            }
            this.addTargetedPlayer(targetEntity.getName());
        }, new Predicate[0]);
        this.livingDeathEventListener = (Listener<LivingDeathEvent>)new Listener(event -> {
            if (AutoGG.mc.player == null) {
                return;
            }
            if (this.targetedPlayers == null) {
                this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
            }
            final EntityLivingBase entity = event.getEntityLiving();
            if (entity == null) {
                return;
            }
            if (!EntityUtil.isPlayer((Entity)entity)) {
                return;
            }
            final EntityPlayer player = (EntityPlayer)entity;
            if (player.getHealth() > 0.0f) {
                return;
            }
            final String name = player.getName();
            if (this.shouldAnnounce(name)) {
                this.doAnnounce(name);
            }
        }, new Predicate[0]);
    }
    
    public void onEnable() {
        this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
    }
    
    public void onDisable() {
        this.targetedPlayers = null;
    }
    
    @Override
    public void onUpdate() {
        if (this.isDisabled() || AutoGG.mc.player == null) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        for (final Entity entity : AutoGG.mc.world.getLoadedEntityList()) {
            if (!EntityUtil.isPlayer(entity)) {
                continue;
            }
            final EntityPlayer player = (EntityPlayer)entity;
            if (player.getHealth() > 0.0f) {
                continue;
            }
            final String name2 = player.getName();
            if (this.shouldAnnounce(name2)) {
                this.doAnnounce(name2);
                break;
            }
        }
        this.targetedPlayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                this.targetedPlayers.remove(name);
            }
            else {
                this.targetedPlayers.put(name, timeout - 1);
            }
        });
    }
    
    private boolean shouldAnnounce(final String name) {
        return this.targetedPlayers.containsKey(name);
    }
    
    private void doAnnounce(final String name) {
        this.targetedPlayers.remove(name);
        final StringBuilder message = new StringBuilder();
        if (this.toxicMode.getValue()) {
            message.append("YOU JUST GOT NUTTED ON PUSSY ");
        }
        else {
            message.append("good fight ig my ca was to fast ");
        }
        message.append(name);
        message.append("!");
        if (this.clientName.getValue()) {
            message.append(" ");
            message.append("\u1d22\u026a\ua731\u1d18\u1d00\u0274\u1d0f\ua731 \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280");
            message.append(" ");
        }
        String messageSanitized = message.toString().replaceAll("§", "");
        if (messageSanitized.length() > 255) {
            messageSanitized = messageSanitized.substring(0, 255);
        }
        AutoGG.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(messageSanitized));
    }
    
    public void addTargetedPlayer(final String name) {
        if (Objects.equals(name, AutoGG.mc.player.getName())) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        this.targetedPlayers.put(name, this.timeoutTicks.getValue());
    }
}
